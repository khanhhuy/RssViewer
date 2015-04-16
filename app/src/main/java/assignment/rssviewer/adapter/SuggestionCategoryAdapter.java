package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.model.SuggestionCategory;
import assignment.rssviewer.utils.TwoTextHolder;

public class SuggestionCategoryAdapter extends ViewHolderAdapter<SuggestionCategory>
{
    public SuggestionCategoryAdapter(Context context, List<SuggestionCategory> categories)
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
    protected void bindView(Object holder, SuggestionCategory item)
    {
        TwoTextHolder tHolder = (TwoTextHolder) holder;
        tHolder.text1.setText(item.getName());
        tHolder.text2.setText("Rss Sources: " + item.getSources().size());
    }
}
