package assignment.rssviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.SourceViewAdapter;
import assignment.rssviewer.adapter.ViewHolderAdapter;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;

public class CategoryActivity extends ActionBarActivity
{
    public static final String ID_KEY = "id";
    private IDataService dataService;
    private Category currentCategory;
    private ViewHolderAdapter<RssSource> sourceAdapter;

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

        switch (item.getItemId())
        {
            case R.id.action_new:
                newSource();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        RssApplication application = (RssApplication) getApplication();
        dataService = application.getDataService();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        long categoryId = bundle.getLong(ID_KEY);

        displayData(categoryId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sourceAdapter.notifyDataSetChanged();
    }

    private void displayData(long categoryId)
    {
        currentCategory = dataService.loadById(Category.class, categoryId);
        if (currentCategory != null)
        {
            setTitle(currentCategory.getName());
            ListView lvSources = (ListView) findViewById(R.id.lvSources);
            sourceAdapter = new SourceViewAdapter(this, currentCategory.getRssSources());
            lvSources.setAdapter(sourceAdapter);

            lvSources.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    RssSource rssSource = sourceAdapter.getItem(position);
                    editSource(rssSource.getId());
                }
            });
        }
    }

    private void newSource()
    {
        Bundle args = AddSourceActivity.createArgs(currentCategory.getId());
        Intent intent = new Intent(this, AddSourceActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    private void editSource(long sourceId)
    {
        Bundle args = EditSourceActivity.createArgs(sourceId);
        Intent intent = new Intent(this, EditSourceActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }
}
