package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.TwoTextHolder;

public class SourceAddAdapter extends ViewHolderAdapter<RssSource>
{
    private final Action<RssSource> onItemSelected;

    public SourceAddAdapter(Context context, List<RssSource> rssSource, Action<RssSource> onItemSelected)
    {
        super(context, R.layout.list_item_rss_source_add, rssSource);
        this.onItemSelected = onItemSelected;
    }

    @Override
    protected Object createHolder(View view, final int position)
    {
        TwoTextHolder holder = new TwoTextHolder();
        holder.text1 = (TextView) view.findViewById(android.R.id.text1);
        holder.text2 = (TextView) view.findViewById(android.R.id.text2);

        ImageButton addButton = (ImageButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onItemSelected.execute(getItem(position));
            }
        });

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
