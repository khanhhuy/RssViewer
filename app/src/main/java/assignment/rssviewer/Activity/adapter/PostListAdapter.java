package assignment.rssviewer.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import assignment.rssviewer.R;
import java.util.ArrayList;

import assignment.rssviewer.activity.model.PostData;


/**
 * Created by Huy on 4/1/2015.
 */
public class PostListAdapter extends ArrayAdapter<PostData> {

    private Activity myContext;
    private ArrayList<PostData> data;

    public PostListAdapter(Context context, int textViewResourceId, ArrayList<PostData> objects) {
        super(context, textViewResourceId, objects);
        myContext = (Activity) context;
        data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.feed_item_layout, null);
        }

        View rowView = convertView;

        ImageView thumbImageView = (ImageView) rowView.findViewById(R.id.imgThumbnail);
        if (data.get(position).postThumbUrl == null) {
            thumbImageView.setImageResource(R.drawable.ic_launcher);
        }

        TextView postTitleView = (TextView) rowView.findViewById(R.id.postTitleLabel);
        postTitleView.setText(data.get(position).postTitle);

        TextView postDateView = (TextView) rowView.findViewById(R.id.postDateLabel);
        postDateView.setText(data.get(position).postDate);

        return rowView;
    }


}