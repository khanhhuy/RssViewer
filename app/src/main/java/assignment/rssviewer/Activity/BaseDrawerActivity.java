package assignment.rssviewer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.DrawerAdapter;


public abstract class BaseDrawerActivity extends ActionBarActivity {

    //private ArrayList<DrawerAdapter.DrawerItem> mlistTitle;
    private DrawerAdapter drawerListAdapter;
    private DrawerLayout drawerLayout;
    //private ListView drawerListView;

    private static int CURRENT_POSITION = 0;
    private static final List<DrawerAdapter.DrawerItem> DRAWER_ITEMS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_drawer_layout);
        inflateChildView();

        List<DrawerAdapter.DrawerItem> drawerItems = createDrawerItems(this);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListAdapter = new DrawerAdapter(this, R.layout.base_leftdrawer_item_layout, drawerItems);
        drawerListView.setAdapter(drawerListAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        drawerListView.setItemChecked(CURRENT_POSITION, true);

        drawerLayout = (DrawerLayout)findViewById(R.id.basedrawer_layout);

        //DrawerLayout rootView = (DrawerLayout) findViewById(R.id.basedrawer_layout);
        //onSetContentView(rootView);
    }

    protected abstract int getChildViewLayout();
    //protected abstract void onSetContentView(View rootView);

    //Dropdown in drawer
    //http://stackoverflow.com/questions/23195740/how-to-implement-android-navigation-drawer-like-this#

    /**
     * Crate Item in the left Drawer
     */
    private static List<DrawerAdapter.DrawerItem> createDrawerItems(Context context) {
        if (DRAWER_ITEMS.size() <= 0)
        {
            Resources resources = context.getResources();
            TypedArray titles = resources.obtainTypedArray(R.array.title);
            TypedArray icons = resources.obtainTypedArray(R.array.icon);
            TypedArray activityClasses = resources.obtainTypedArray(R.array.activityClasses);

            Log.d("Array length", (Integer.valueOf(titles.length()).toString()));

            for (int i = 0; i < titles.length(); i++) {
                DrawerAdapter.DrawerItem drawerItem = new DrawerAdapter.DrawerItem();
                drawerItem.setTitle(titles.getString(i));
                drawerItem.setIcon(icons.getResourceId(i, -1));
                try
                {
                    drawerItem.setActivityClass(Class.forName(activityClasses.getString(i)));
                }
                catch (Exception ex)
                {
                    Log.e("Activity", String.format("Activity class for %s is not found.", drawerItem.getTitle()));
                }
                Log.d("Title" + i + " ", drawerItem.getTitle());
                Log.d("Thumbnail" + i + " ", drawerItem.getIcon().toString());
                DRAWER_ITEMS.add(drawerItem);
            }

            titles.recycle();
            icons.recycle();
            activityClasses.recycle();
        }

        return DRAWER_ITEMS;
    }

    /**
     * Inflate child View and add it to Framelayout
     */
    private void inflateChildView() {
        int childViewid = getChildViewLayout();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(childViewid, null);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        frameLayout.addView(view);
    }

    /**
     * Handle click on drawer's item
     */

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Start Activity from drawer
     * @param position
     */

    private void selectItem(int position) {
        if (CURRENT_POSITION != position)
        {
            DrawerAdapter.DrawerItem drawerItem = drawerListAdapter.getItem(position);
            Class<?> activityClass = drawerItem.getActivityClass();
            if (activityClass != null)
            {
                CURRENT_POSITION = position;
                Intent intent = new Intent(this, activityClass);
                startActivity(intent);
            }
            else
            {
                Log.e("Activity", String.format("Activity class for %s is not found.", drawerItem.getTitle()));
            }
        }
        else
        {
            drawerLayout.closeDrawers();
        }
    }
}
