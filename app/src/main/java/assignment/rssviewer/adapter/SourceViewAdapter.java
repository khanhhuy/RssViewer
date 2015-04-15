package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.utils.TwoTextHolder;

/**
 * Created by Prozacs on 25/03/2015.
 */
public class SourceViewAdapter extends ViewHolderAdapter<RssSource>
{
    public SourceViewAdapter(Context context, List<RssSource> rssSources)
    {
        super(context, R.layout.list_item_rss_source_view, rssSources);
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
    protected void bindView(Object holder, RssSource item)
    {
        TwoTextHolder tHolder = (TwoTextHolder) holder;

        tHolder.text1.setText(item.getName());
        tHolder.text2.setText(item.getUrlString());
    }
}
