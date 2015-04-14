package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Provides a general purpose adapter that delegates the getView method to the actual view binding action.
 *
 * @param <T> Data type for the adapter
 */
public abstract class ViewHolderAdapter<T> extends ArrayAdapter<T>
{
    protected final LayoutInflater inflater;
    protected final int layoutResource;

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

    protected abstract Object createHolder(View view, int position);

    protected abstract void bindView(Object holder, T item);
}
