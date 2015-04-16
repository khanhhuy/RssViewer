package assignment.rssviewer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.utils.MainFragment;

/**
 * Created by Huy on 4/16/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MainFragment> listHeader;
    private HashMap<String, List<String>> listChild;

    public ExpandableListAdapter(Context context, List<MainFragment> listHeader,
                                 HashMap<String, List<String>> listChild){
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(listHeader.get(groupPosition).getTitle()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listHeader.get(groupPosition).getTitle()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String title = ((MainFragment) getGroup(groupPosition)).getTitle();
        int iconResource = ((MainFragment) getGroup(groupPosition)).getIconResource();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_nav_drawer, null);
        }

        ImageView thumbImageView = (ImageView) convertView.findViewById(R.id.iconview);
        thumbImageView.setImageResource(iconResource);

        TextView header = (TextView) convertView.findViewById(R.id.titleText);
        header.setText(title);

        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childCategory = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child_nav_drawer, null);
        }

        TextView childText = (TextView) convertView.findViewById(R.id.childText);
        childText.setText(childCategory);

        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
