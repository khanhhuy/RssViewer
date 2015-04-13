package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.model.Category;
import assignment.rssviewer.utils.TwoTextHolder;

/**
 * Created by Prozacs on 26/03/2015.
 */
public class CategoryListAdapter extends ViewHolderAdapter<Category>
{
    public CategoryListAdapter(Context context, List<Category> categories)
    {
        super(context, android.R.layout.simple_list_item_activated_2, categories);
    }

    @Override
    protected Object createHolder(View view, int position)
    {
        TwoTextHolder holder = new TwoTextHolder();
        holder.text1 = (TextView) view.findViewById(android.R.id.text1);
        holder.text2 = (TextView) view.findViewById(android.R.id.text2);
        return holder;
    }

    @Override
    protected void bindView(Object holder, Category item)
    {
        TwoTextHolder tHolder = (TwoTextHolder) holder;
        tHolder.text1.setText(item.getName());
        tHolder.text2.setText("RSS Sources: " + item.getRssSources().size());
    }
}
