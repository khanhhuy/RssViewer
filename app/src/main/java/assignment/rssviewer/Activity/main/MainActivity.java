package assignment.rssviewer.activity.main;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import java.util.HashMap;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.DrawerAdapter;
import assignment.rssviewer.utils.MainFragment;

public class MainActivity extends ActionBarActivity
{
    private final List<DrawerAdapter.DrawerItem> drawerItems = new ArrayList<>();
    private final HashMap<String, MainFragment> fragments = new HashMap<>();
    private int currentPos = 0;
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
        setContentView(R.layout.activity_main);

        initFragments(this, drawerItems, fragments, true);
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
                if (position != currentPos)
                {
                    hideFragment(drawerItems.get(currentPos).getFragmentName());
                    showFragment(drawerItems.get(position).getFragmentName(), false);
                    currentPos = position;
                }
                drawerLayout.closeDrawer(lvDrawer);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        showFragment(drawerItems.get(0).getFragmentName(), false);
        lvDrawer.setItemChecked(0, true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private static void initFragments(FragmentActivity activity,
                                      List<DrawerAdapter.DrawerItem> drawerItems,
                                      HashMap<String, MainFragment> fragments,
                                      boolean clearFirst)
    {
        if (clearFirst)
        {
            drawerItems.clear();
            fragments.clear();
        }

        Resources resources = activity.getResources();
        TypedArray fragmentClasses = resources.obtainTypedArray(R.array.fragmentClasses);
        FragmentTransaction frmTx = activity.getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < fragmentClasses.length(); i++)
        {
            try
            {
                String frmName = fragmentClasses.getString(i);
                MainFragment fragment = (MainFragment) Fragment.instantiate(activity, frmName);
                DrawerAdapter.DrawerItem drawerItem = new DrawerAdapter.DrawerItem();
                drawerItem.setTitle(fragment.getTitle());
                drawerItem.setIcon(fragment.getIconResource());
                drawerItem.setFragmentName(frmName);
                drawerItems.add(drawerItem);

                fragments.put(frmName, fragment);
                if (fragment.isStatic())
                {
                    frmTx.add(R.id.content_frame, fragment);
                    frmTx.hide(fragment);
                }
            }
            catch (Exception ex)
            {
                Log.e("Error", "Cannot initialize fragment", ex);
            }
        }

        fragmentClasses.recycle();
        frmTx.commit();
    }

    private void showFragment(String frmName, boolean addToBackStack)
    {
        MainFragment fragment = fragments.get(frmName);
        if (fragment != null)
        {
            FragmentTransaction frmTx = getSupportFragmentManager().beginTransaction();
            if (!fragment.isStatic())
            {
                frmTx.add(R.id.content_frame, fragment);
            }

            frmTx.show(fragment);
            this.setTitle(fragment.getTitle());

            if (addToBackStack)
                frmTx.addToBackStack(null);
            frmTx.commit();
        }
    }

    private void hideFragment(String frmName)
    {
        MainFragment fragment = fragments.get(frmName);
        if (fragment != null)
        {
            FragmentTransaction frmTx = getSupportFragmentManager().beginTransaction();
            if (fragment.isStatic())
            {
                frmTx.hide(fragment);
            }
            else
            {
                frmTx.remove(fragment);
            }
            frmTx.commit();
        }
    }
}
