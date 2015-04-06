package assignment.rssviewer.lvadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.model.RssSource;

/**
 * Created by Prozacs on 25/03/2015.
 */
public class SourceAdapter extends ArrayAdapter<RssSource>
{
    public SourceAdapter(Context context, List<RssSource> rssSources)
    {
        super(context, R.layout.source_list_item_layout, android.R.id.text1, rssSources);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

        RssSource source = super.getItem(position);
        text1.setText(source.getName());
        text2.setText(source.getUriString());

        return view;
    }
}
