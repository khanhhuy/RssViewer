package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.model.Category;
import assignment.rssviewer.utils.TwoTextHolder;

public class CategorySpinnerAdapter extends ViewHolderAdapter<Category>
{
    public CategorySpinnerAdapter(Context context, List<Category> categories)
    {
        super(context, android.R.layout.simple_spinner_item, categories);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            Object holder = createHolder(convertView, position);
            convertView.setTag(holder);
        }

        bindView(convertView.getTag(), getItem(position));
        return convertView;
    }

    @Override
    protected Object createHolder(View view, int position)
    {
        TwoTextHolder holder = new TwoTextHolder();
        holder.text1 = (TextView) view.findViewById(android.R.id.text1);
        return holder;
    }

    @Override
    protected void bindView(Object holder, Category item)
    {
        TwoTextHolder tHolder = (TwoTextHolder) holder;
        tHolder.text1.setText(item.getName());
    }
}
