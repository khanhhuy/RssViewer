package assignment.rssviewer.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.Func;

/**
 * Provides a general purpose adapter that delegates the getView method to the actual view binding action.
 *
 * @param <T> Data type for the adapter
 */
public abstract class ViewHolderAdapter<T> extends ArrayAdapter<T>
{
    protected final LayoutInflater inflater;
    protected final int layoutResource;
    private List<T> originalList, viewList;
    private AsyncTask filterTask;

    /**
     * Initializes a ViewHolderAdapter instance.
     *
     * @param context        The context in which the adapter is created
     * @param layoutResource The layout resource id for each item
     * @param objects        The list of objects to display
     */
    public ViewHolderAdapter(Context context, int layoutResource, List<T> objects)
    {
        super(context, layoutResource, objects);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.viewList = objects;
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

        bindView(convertView.getTag(), super.getItem(position));
        return convertView;
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

        if (predicate != null)
        {
            filterTask = new AsyncTask<Void, Void, List<T>>()
            {
                @Override
                protected List<T> doInBackground(Void... params)
                {
                    if (originalList == null)
                    {
                        originalList = new ArrayList<>(viewList);
                    }

                    List<T> results = new ArrayList<>();

                    for (T item : originalList)
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

                @Override
                protected void onPostExecute(List<T> results)
                {
                    if (!isCancelled())
                    {
                        ViewHolderAdapter.this.clear();
                        ViewHolderAdapter.this.addAll(results);
                        if (callBack != null)
                            callBack.execute(null);
                    }
                }
            }.execute();
        }
        else
        {
            filterTask = null;
            this.clear();
            this.addAll(originalList);
        }
    }

    protected abstract Object createHolder(View view, int position);

    protected abstract void bindView(Object holder, T item);
}
