package assignment.rssviewer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import assignment.rssviewer.R;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;

public class AddSourceActivity extends ActionBarActivity
        implements CustomSourceFragment.OnFragmentInteractionListener, SourceSuggestionFragment.OnFragmentInteractionListener
{
    private FragmentTabHost tabHost;
    private Category currentCategory;
    private IDataService dataService;

    private static final String ID_KEY = "id";

    public static Bundle createArgs(long categoryId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(ID_KEY, categoryId);
        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_source, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_source);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("suggestion").setIndicator("Suggestion"),
                       SourceSuggestionFragment.class, null);

        tabHost.addTab(tabHost.newTabSpec("add").setIndicator("Add"),
                       CustomSourceFragment.class, null);

        Bundle args = getIntent().getExtras();
        long categoryId = args.getLong(ID_KEY);

        dataService = ((RssApplication)getApplication()).getDataService();
        currentCategory = dataService.getEntityById(Category.class, categoryId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSourceSelected(RssSource rssSource)
    {
        rssSource.setCategoryId(currentCategory.getId());
        dataService.insertEntityAsync(rssSource, null);
        currentCategory.getRssSources().add(rssSource);

        Toast.makeText(this, String.format("Source added"), Toast.LENGTH_SHORT).show();
    }
}
