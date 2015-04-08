package assignment.rssviewer.activity.main;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.DrawerAdapter;

public class MainActivity extends ActionBarActivity
{
    private final List<DrawerAdapter.DrawerItem> drawerItems = new ArrayList<>();
    private int currentPosition = 0;
    private ActionBarDrawerToggle drawerToggle;
    private ListView lvDrawer;
    private DrawerLayout drawerLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        fillDrawerItems(drawerItems, true);
        lvDrawer = (ListView) findViewById(R.id.left_drawer);
        DrawerAdapter drawerAdapter = new DrawerAdapter(this, drawerItems);
        lvDrawer.setAdapter(drawerAdapter);
        lvDrawer.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        drawerLayout = (DrawerLayout) findViewById(R.id.basedrawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        lvDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                if (currentPosition != position)
                {
                    if (startFragment(drawerItems.get(position)))
                        currentPosition = position;
                }
                drawerLayout.closeDrawer(lvDrawer);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        startFragment(drawerItems.get(0));
        lvDrawer.setItemChecked(0, true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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

    /**
     * Start fragment when a drawer item is selected
     *
     * @param drawerItem is the selected item
     * @return true if fragment is started successfully, false otherwise
     */
    private boolean startFragment(DrawerAdapter.DrawerItem drawerItem)
    {
        try
        {
            FragmentTransaction frmTx = getSupportFragmentManager().beginTransaction();
            frmTx.replace(R.id.content_frame, Fragment.instantiate(this, drawerItem.getFragmentName()));
            frmTx.commit();
            this.setTitle(drawerItem.getTitle());
            return true;
        }
        catch (Exception ex)
        {
            Log.e("Error", "Error showing fragment.", ex);
            return false;
        }
    }
}
