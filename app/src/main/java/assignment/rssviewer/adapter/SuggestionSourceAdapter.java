package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.model.SuggestionSource;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.TwoTextHolder;

public class SuggestionSourceAdapter extends ViewHolderAdapter<SuggestionSource>
{
    private final Action<SuggestionSource> onItemSelected;

    public SuggestionSourceAdapter(Context context, List<SuggestionSource> sources, Action<SuggestionSource> onItemSelected)
    {
        super(context, R.layout.list_item_rss_source_add, sources);
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
    protected void bindView(Object holder, SuggestionSource item)
    {
        TwoTextHolder tHolder = (TwoTextHolder) holder;
        tHolder.text1.setText(item.getName());
        tHolder.text2.setText(item.getUrlString());
    }
}
