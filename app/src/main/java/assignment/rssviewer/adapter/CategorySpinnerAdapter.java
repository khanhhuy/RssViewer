package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.model.Category;
import assignment.rssviewer.utils.TwoTextHolder;

public class CategorySpinnerAdapter extends ViewHolderAdapter<Category>
{
    public CategorySpinnerAdapter(Context context, List<Category> categories)
    {
        super(context, android.R.layout.simple_list_item_1, categories);
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
