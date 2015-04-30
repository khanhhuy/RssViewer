package assignment.rssviewer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import assignment.rssviewer.R;
import assignment.rssviewer.model.Article;


/**
 * Created by Huy on 4/1/2015.
 */
public class PostListAdapter extends ArrayAdapter<Article>
{
    private final List<Article> data;

    public PostListAdapter(Context context, int textViewResourceId, List<Article> objects)
    {
        super(context, textViewResourceId, R.id.postTitleLabel, objects);
        data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = super.getView(position, convertView, parent);

        Random rnd = new Random();
        int color = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        rowView.setBackgroundColor(color);

        TextView postTitleView = (TextView) rowView.findViewById(R.id.postTitleLabel);
        postTitleView.setText(data.get(position).getTitle());

        TextView postDateView = (TextView) rowView.findViewById(R.id.postDateLabel);
        postDateView.setText(data.get(position).getUrlString());

        return rowView;
    }
}