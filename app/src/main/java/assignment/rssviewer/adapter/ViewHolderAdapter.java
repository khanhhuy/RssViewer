package assignment.rssviewer.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.Func;

/**
 * Provides a general purpose adapter that delegates the getView method to the actual view binding action.
 *
 * @param <T> Data type for the adapter
 */
public abstract class ViewHolderAdapter<T> extends BaseAdapter
{
    protected final LayoutInflater inflater;
    protected final int layoutResource;
    private List<T> originalList, displayList;
    private Func<T, Boolean> filterPredicate;
    private AsyncTask filterTask;
    private final Object lockObject = new Object();
    private boolean notifyOnChanged = true;

    /**
     * Initializes a ViewHolderAdapter instance.
     *
     * @param context        The context in which the adapter is created
     * @param layoutResource The layout resource id for each item
     * @param objects        The list of objects to display
     */
    public ViewHolderAdapter(Context context, int layoutResource, List<T> objects)
    {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.displayList = this.originalList = objects;
    }

    @Override
    public T getItem(int position)
    {
        return displayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getCount()
    {
        return displayList.size();
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(layoutResource, parent, false);
            Object holder = createHolder(convertView, position);
            convertView.setTag(holder);
        }

        bindView(convertView.getTag(), getItem(position));
        return convertView;
    }

    public int getPosition(T item)
    {
        return displayList.indexOf(item);
    }

    public void filter(Func<T, Boolean> predicate)
    {
        this.filter(predicate, null);
    }

    public void filter(final Func<T, Boolean> predicate,
                       final Action<Void> callBack)
    {
        if (filterTask != null && filterTask.getStatus() == AsyncTask.Status.RUNNING)
            filterTask.cancel(true);
        filterPredicate = predicate;

        filterTask = new AsyncTask<Void, Void, List<T>>()
        {
            @Override
            protected List<T> doInBackground(Void... params)
            {
                if (predicate != null)
                {
                    List<T> values;
                    synchronized (lockObject)
                    {
                        values = new ArrayList<>(originalList);
                    }

                    List<T> results = new ArrayList<>();
                    for (T item : values)
                    {
                        if (isCancelled())
                            break;

                        if (predicate.execute(item))
                        {
                            results.add(item);
                        }
                    }
                    return results;
                }
                else
                {
                    return originalList;
                }
            }

            @Override
            protected void onPostExecute(List<T> results)
            {
                if (!isCancelled())
                {
                    displayList = results;
                    if (results.size() > 0)
                        notifyDataSetChanged();
                    else notifyDataSetInvalidated();

                    if (callBack != null)
                        callBack.execute(null);
                }
            }
        }.execute();
    }

    public void setNotifyOnChanged(boolean value)
    {
        this.notifyOnChanged = value;
    }

    public void add(T item)
    {
        synchronized (lockObject)
        {
            originalList.add(item); // if filterPredicate == null -> displayList == originalList
            if (filterPredicate != null && filterPredicate.execute(item))
                displayList.add(item);
        }
        if (notifyOnChanged) notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> items)
    {
        synchronized (lockObject)
        {
            originalList.addAll(items);
            if (filterPredicate != null)
            {
                for (T i : items)
                {
                    if (filterPredicate.execute(i))
                        displayList.add(i);
                }
            }
        }
        if (notifyOnChanged) notifyDataSetChanged();
    }

    public void insert(T item, int position)
    {
        synchronized (lockObject)
        {
            originalList.add(position, item);
            if (filterPredicate != null && filterPredicate.execute(item))
                displayList.add(position, item);
        }
        if (notifyOnChanged) notifyDataSetChanged();
    }

    public void remove(T item)
    {
        synchronized (lockObject)
        {
            originalList.remove(item);
            if (filterPredicate != null)
                displayList.remove(item);
        }
        if (notifyOnChanged) notifyDataSetChanged();
    }

    public void clear()
    {
        synchronized (lockObject)
        {
            originalList.clear();
            if (filterPredicate != null)
                displayList.clear();
        }
        if (notifyOnChanged) notifyDataSetChanged();
    }

    protected abstract Object createHolder(View view, int position);

    protected abstract void bindView(Object holder, T item);
}
