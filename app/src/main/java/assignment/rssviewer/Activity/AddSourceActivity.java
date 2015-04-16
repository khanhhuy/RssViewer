package assignment.rssviewer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost;
import android.widget.Toast;

import assignment.rssviewer.R;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;

public class AddSourceActivity extends FragmentActivity
        implements CustomSourceFragment.OnFragmentInteractionListener, SuggestionFragment.OnFragmentInteractionListener
{
    private static final String ID_KEY = "id";
    //private FragmentTabHost tabHost;
    private Category currentCategory;
    private IDataService dataService;

    public static Bundle createArgs(long categoryId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(ID_KEY, categoryId);
        return bundle;
    }

    @Override
    public void onSourceSelected(RssSource rssSource)
    {
        rssSource.setCategoryId(currentCategory.getId());
        dataService.insertAsync(rssSource, null);
        currentCategory.getRssSources().add(rssSource);
        Toast.makeText(this, String.format("Added to %s", currentCategory.getName()), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_source);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("suggestion").setIndicator("Suggestion"),
                       SuggestionFragment.class, null);

        tabHost.addTab(tabHost.newTabSpec("add").setIndicator("Add"),
                       CustomSourceFragment.class, null);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId)
            {
                hideKeyboard(getCurrentFocus());
            }
        });

        Bundle args = getIntent().getExtras();
        long categoryId = args.getLong(ID_KEY);

        dataService = ((RssApplication) getApplication()).getDataService();
        currentCategory = dataService.loadById(Category.class, categoryId);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void hideKeyboard(View view)
    {
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
