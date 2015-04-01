package assignment.rssviewer.activity;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

import assignment.rssviewer.R;
import assignment.rssviewer.activity.adapter.DrawerListAdapter;
import assignment.rssviewer.activity.model.DrawerData;


public class BaseDrawerActivity extends ActionBarActivity {

    private ArrayList<DrawerData> mlistTitle;
    private DrawerListAdapter drawerListAdapter;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_drawer_layout);

        populateListTitle();
        Log.d("DEBUG", "Finish populate list");

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawerListAdapter = new DrawerListAdapter(this, R.layout.base_leftdrawer_item_layout, mlistTitle);
        mDrawerList.setAdapter(drawerListAdapter);
    }

    //Dropdown in drawer
    //http://stackoverflow.com/questions/23195740/how-to-implement-android-navigation-drawer-like-this#

    @Override
    /**
     * Override this method for new option menu.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    /**
     * Override this method for new option menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateListTitle() {
        Resources resources = getResources();
        TypedArray titles = resources.obtainTypedArray(R.array.title);
        TypedArray icons = resources.obtainTypedArray(R.array.icon);

        mlistTitle = new ArrayList<DrawerData>();

        Log.d("Array length", (new Integer(titles.length()).toString()));

        for (int i = 0; i < titles.length(); i++) {
            DrawerData drawerTitle = new DrawerData();
            drawerTitle.setTitle(titles.getString(i));
            drawerTitle.setIcon(icons.getResourceId(i, -1));
            Log.d("Title" + i + " ", drawerTitle.getTitle());
            Log.d("Thumbnai" + i + " ", drawerTitle.getIcon().toString());
            mlistTitle.add(drawerTitle);
        }

    }

    /**
     * Create your custom view and pass it to this method
     * @param view
     */
    protected void addContentView(View view) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        frameLayout.addView(view);
        Log.d("Add new View", "Success");
    }
}
