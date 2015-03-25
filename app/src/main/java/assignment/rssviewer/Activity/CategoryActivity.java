package assignment.rssviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import assignment.rssviewer.R;
import assignment.rssviewer.fragment.EditCategoryDialog;
import assignment.rssviewer.lvadapter.SourceAdapter;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.service.RssApplication;

public class CategoryActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);

        displayData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void displayData()
    {
        RssApplication application = (RssApplication)getApplication();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Category category = application.getCategoryById(bundle.getInt("id"));
        if (category != null)
        {
            setTitle(category.name);
            ListView lvSources = (ListView)findViewById(R.id.lvSources);
            SourceAdapter adapter = new SourceAdapter(this, category.rssSources);
            lvSources.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
}
