package assignment.rssviewer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import assignment.rssviewer.R;

import java.util.List;

/**
 * Created by Huy on 3/29/2015.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerAdapter.DrawerItem> {
    private Activity context;
    private List<DrawerItem> drawerDatas;

    public DrawerAdapter(Context context, int textViewResourceId, List<DrawerItem> objects) {
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

    /**
     * Created by Huy on 3/29/2015.
     */
    public static class DrawerItem
    {

        private String title;
        private Integer icon;
        private Class<?> activityClass;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getIcon() {
            return icon;
        }

        public void setIcon(Integer icon) {
            this.icon = icon;
        }

        public Class<?> getActivityClass()
        {
            return activityClass;
        }

        public void setActivityClass(Class<?> value)
        {
            this.activityClass = value;
        }
    }
}
