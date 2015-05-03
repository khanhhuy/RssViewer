package assignment.rssviewer.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import assignment.rssviewer.R;
import assignment.rssviewer.model.Article;


/**
 * Created by Huy on 4/1/2015.
 */
public class PostListAdapter extends ArrayAdapter<Article>
{
    private final List<Article> data;
    private HashMap<String, Bitmap> bitmapHashMap;
    private Context context;

    public PostListAdapter(Context context, int textViewResourceId, List<Article> objects, HashMap<String, Bitmap> bitmapHashMap)
    {
        super(context, textViewResourceId, R.id.postTitleLabel, objects);
        this.context = context;
        data = objects;
        this.bitmapHashMap = bitmapHashMap;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_feed, null);
        }

        TextView postTitleView = (TextView) convertView.findViewById(R.id.postTitleLabel);
        postTitleView.setText(data.get(position).getTitle());

        TextView postDateView = (TextView) convertView.findViewById(R.id.postDateLabel);
        postDateView.setText(data.get(position).getPublishDate().toString().substring(0, 10));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageThumbnail);

        if (bitmapHashMap.size() > 0) {
            imageView.setImageBitmap(bitmapHashMap.get(data.get(position).getImageUrl()));
        }
        else
            imageView.setImageDrawable(null);


        return convertView;
    }


}