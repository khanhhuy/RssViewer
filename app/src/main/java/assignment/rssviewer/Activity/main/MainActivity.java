package assignment.rssviewer.activity.main;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.DrawerAdapter;

/**
 * The list item should be static, so the all-category item should not be placed in the list,
 * such functionality should be implemented in the FeedListFragment instead.
 * The drawer should be used for navigating between top-level activities only.
 */
public class MainActivity extends ActionBarActivity
{
    // used for caching the drawer list items
    // this is static because it doesn't change at runtime, which means any extended classes get the same items.
    private final List<DrawerAdapter.DrawerItem> drawerItems = new ArrayList<>();
    // statically stores info for current activity position, used to consistently highlight selected item in drawer list
    // because the drawer list is an instance member, so different activities have different drawer list instances
    // default value is 0 -> when user first launches the app, the position 0 item is highlighted, which means the first activity
    // in the list should launch at startup
    private int currentPosition = 0;
    //private ListView drawerListView;
    //private ArrayList<DrawerAdapter.DrawerItem> mlistTitle;
    //private DrawerAdapter drawerAdapter;
    //private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //inflateChildView();
        fillDrawerItems(drawerItems, true);

        final ListView lvDrawer = (ListView) findViewById(R.id.left_drawer);
        DrawerAdapter drawerAdapter = new DrawerAdapter(this, drawerItems);
        lvDrawer.setAdapter(drawerAdapter);
        lvDrawer.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.basedrawer_layout);

        lvDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener()
                {
                    @Override
                    public void onDrawerClosed(View drawerView)
                    {
                        super.onDrawerClosed(drawerView);

                        if (currentPosition != position)
                        {
                            startFragment(drawerItems.get(position));
                            currentPosition = position;
                        }
                    }
                });
                drawerLayout.closeDrawer(lvDrawer);
            }
        });

        startFragment(drawerItems.get(0));
        lvDrawer.setItemChecked(0, true);
    }

    /**
     * Crate Item in the left Drawer
     * if DRAWER_ITEMS contains no items, we have to fill it with information in drawer_list.xml
     * otherwise return the cached list.
     */
    private void fillDrawerItems(List<DrawerAdapter.DrawerItem> drawerItems, boolean clearFirst)
    {
        if (clearFirst)
            drawerItems.clear();

        Resources resources = getResources();
        TypedArray titles = resources.obtainTypedArray(R.array.title);
        TypedArray icons = resources.obtainTypedArray(R.array.icon);
        TypedArray activityClasses = resources.obtainTypedArray(R.array.activityClasses);

        Log.d("Array length", (Integer.valueOf(titles.length()).toString()));

        for (int i = 0; i < titles.length(); i++)
        {
            try
            {
                DrawerAdapter.DrawerItem drawerItem = new DrawerAdapter.DrawerItem();
                drawerItem.setTitle(titles.getString(i));
                drawerItem.setIcon(icons.getResourceId(i, -1));
                drawerItem.setFragmentName(activityClasses.getString(i));
                Log.d("Title" + i + " ", drawerItem.getTitle());
                Log.d("Thumbnail" + i + " ", drawerItem.getIcon().toString());
                drawerItems.add(drawerItem);
            }
            catch (Exception ex)
            {
                Log.e("Error", "Error creating drawer items", ex);
            }
        }

        titles.recycle();
        icons.recycle();
        activityClasses.recycle();
    }

    private void startFragment(DrawerAdapter.DrawerItem drawerItem)
    {
        try
        {
            FragmentTransaction frmTx = getSupportFragmentManager().beginTransaction();
            frmTx.replace(R.id.content_frame, Fragment.instantiate(this, drawerItem.getFragmentName()));
            frmTx.commit();
            this.setTitle(drawerItem.getTitle());
        }
        catch (Exception ex)
        {
            Log.e("Error", "Error showing fragment.", ex);
        }
    }
    //protected abstract void onSetContentView(View rootView); <-- not necessary anymore

    //Dropdown in drawer
    //http://stackoverflow.com/questions/23195740/how-to-implement-android-navigation-drawer-like-this#

    /*protected abstract int getChildViewLayout();

    *//**
 * Inflate child View and add it to Framelayout
 *//*
    private void inflateChildView()
    {
        int childViewid = getChildViewLayout();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(childViewid, null);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        frameLayout.addView(view);
    }*/

    /**
     * Start Activity from drawer
     * the activity associated with the drawer item will be launched.
     *
     * @param position
     */

    /*private void selectItem(int position)
    {
        if (CURRENT_POSITION != position)
        {
            DrawerAdapter.DrawerItem drawerItem = drawerAdapter.getItem(position);
            Class<?> activityClass = drawerItem.getFragmentName();
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

    *//**
 * Handle click on drawer's item
 *//*

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }*/
}
