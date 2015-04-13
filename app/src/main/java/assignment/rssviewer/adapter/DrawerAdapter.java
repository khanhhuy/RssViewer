package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.R;

/**
 * Created by Huy on 3/29/2015.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerAdapter.DrawerItem>
{
    private final List<DrawerItem> drawerItems;

    public DrawerAdapter(Context context, List<DrawerItem> drawerItems)
    {
        super(context, R.layout.list_item_nav_drawer, R.id.titleText, drawerItems);
        this.drawerItems = drawerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = super.getView(position, convertView, parent);

        ImageView thumbnailView = (ImageView) rowView.findViewById(R.id.iconview);
        thumbnailView.setImageResource(drawerItems.get(position).getIcon());

        TextView titleView = (TextView) rowView.findViewById(R.id.titleText);
        titleView.setText(drawerItems.get(position).getTitle());

        return rowView;
    }

    /**
     * Created by Huy on 3/29/2015.
     */
    public static class DrawerItem
    {
        private String title;
        private Integer icon;
        private String fragmentName;

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public Integer getIcon()
        {
            return icon;
        }

        public void setIcon(Integer icon)
        {
            this.icon = icon;
        }

        public String getFragmentName()
        {
            return fragmentName;
        }

        public void setFragmentName(String value)
        {
            this.fragmentName = value;
        }
    }
}
