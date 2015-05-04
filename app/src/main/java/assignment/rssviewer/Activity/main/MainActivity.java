package assignment.rssviewer.activity.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.DrawerAdapter;
import assignment.rssviewer.dialog.MessageDialog;

public class MainActivity extends Activity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private final List<DrawerAdapter.DrawerItem> drawerItems = new ArrayList<>();
    private final HashMap<String, BaseMainFragment> fragments = new HashMap<>();
    private int currentPos = 0;
    private ActionBarDrawerToggle drawerToggle;
    private ListView lvDrawer;
    private DrawerLayout drawerLayout;
    private GoogleApiClient ggApiClient;
    private boolean resolvingConnectionError = false;

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

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        showFragment(drawerItems.get(0).getFragmentName(), false);
        lvDrawer.setItemChecked(0, true);

        ggApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        ggApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1001)
        {
            resolvingConnectionError = false;
            if (resultCode == RESULT_OK)
            {
                if (!ggApiClient.isConnecting() && !ggApiClient.isConnected())
                {
                    ggApiClient.connect();
                }
            }
        }
    }

    private static void initFragments(Activity activity,
                                      List<DrawerAdapter.DrawerItem> drawerItems,
                                      HashMap<String, BaseMainFragment> fragments,
                                      boolean clearFirst)
    {
        if (clearFirst)
        {
            drawerItems.clear();
            fragments.clear();
        }

        Resources resources = activity.getResources();
        TypedArray fragmentClasses = resources.obtainTypedArray(R.array.fragmentClasses);
        FragmentTransaction frmTx = activity.getFragmentManager().beginTransaction();

        for (int i = 0; i < fragmentClasses.length(); i++)
        {
            try
            {
                String frmName = fragmentClasses.getString(i);
                BaseMainFragment fragment = (BaseMainFragment) Fragment.instantiate(activity, frmName);
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
        BaseMainFragment fragment = fragments.get(frmName);
        if (fragment != null)
        {
            android.app.FragmentTransaction frmTx = getFragmentManager().beginTransaction();
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
        BaseMainFragment fragment = fragments.get(frmName);
        if (fragment != null)
        {
            FragmentTransaction frmTx = getFragmentManager().beginTransaction();
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

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.i("connect", "connected to google");
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i("connect", "connection suspended with i = " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (!resolvingConnectionError)
        {
            if (connectionResult.hasResolution())
            {
                try
                {
                    resolvingConnectionError = true;
                    connectionResult.startResolutionForResult(this, 1001);
                }
                catch (IntentSender.SendIntentException e)
                {
                    ggApiClient.connect();
                }
            }
            else
            {
                ErrorDialogFragment errorDialog = ErrorDialogFragment.newInstance(
                        GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 1001),
                        new DialogInterface.OnCancelListener()
                        {
                            @Override
                            public void onCancel(DialogInterface dialog)
                            {
                                resolvingConnectionError = false;
                            }
                        });
                errorDialog.show(this.getFragmentManager(), "error_dialog");
                resolvingConnectionError = true;
            }
        }
    }
}
