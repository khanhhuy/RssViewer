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

import assignment.rssviewer.activity.model.DrawerData;

/**
 * Created by Huy on 3/29/2015.
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerData> {
    private Activity context;
    private ArrayList<DrawerData> drawerDatas;

    public  DrawerListAdapter(Context context, int textViewResourceId, ArrayList<DrawerData> objects) {
        super(context, textViewResourceId, objects);
        this.context = (Activity) context;
        this.drawerDatas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.base_leftdrawer_item_layout, null);
        }

        View rowView = convertView;

        ImageView thumbnailView = (ImageView) rowView.findViewById(R.id.iconview);
        thumbnailView.setImageResource(drawerDatas.get(position).getIcon());

        TextView titleView = (TextView) rowView.findViewById(R.id.titleText);
        titleView.setText(drawerDatas.get(position).getTitle());

        return  rowView;
    }

}
