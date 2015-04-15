package assignment.rssviewer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.CategorySpinnerAdapter;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;

public class EditSourceActivity extends Activity
{
    private static final String SOURCE_ID_KEY = "source_id";

    private IDataService dataService;
    private RssSource rssSource;
    private EditText titleText;
    private Spinner categorySpinner;
    private MenuItem saveMenuItem;

    private TextWatcher titleTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            notifyValueChanged();
        }
    };

    private AdapterView.OnItemSelectedListener categorySelectedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            notifyValueChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    };

    public static Bundle createArgs(long sourceId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(SOURCE_ID_KEY, sourceId);
        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_source, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        saveMenuItem = menu.findItem(R.id.action_save);
        setEnabled(saveMenuItem, false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save)
        {
            boolean isChanged = false;

            String newTitle = titleText.getText().toString();
            Category newCategory = (Category) categorySpinner.getSelectedItem();

            if (!rssSource.getName().equals(newTitle))
            {
                isChanged = true;
                rssSource.setName(newTitle);
            }

            if (rssSource.getCategoryId() != newCategory.getId())
            {
                Category oldCategory = rssSource.getCategory();
                oldCategory.getRssSources().remove(rssSource);
                newCategory.getRssSources().add(rssSource);
                rssSource.setCategoryId(newCategory.getId());
                isChanged = true;
            }

            if (isChanged)
            {
                dataService.updateAsync(RssSource.class, new Action<AsyncResult<Void>>()
                {
                    @Override
                    public void execute(AsyncResult<Void> voidAsyncResult)
                    {
                        EditSourceActivity.this.onBackPressed();
                    }
                }, rssSource);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rss_source);

        RssApplication application = (RssApplication) getApplication();
        dataService = application.getDataService();

        Bundle args = getIntent().getExtras();
        long sourceId = args.getLong(SOURCE_ID_KEY);
        rssSource = dataService.loadById(RssSource.class, sourceId);

        TextView uriText = (TextView) findViewById(R.id.uriText);
        uriText.setText(rssSource.getUrlString());

        titleText = (EditText) findViewById(R.id.titleText);
        titleText.setText(rssSource.getName());
        titleText.addTextChangedListener(titleTextWatcher);

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(categorySelectedListener);

        dataService.loadAllAsync(Category.class, new Action<AsyncResult<List<Category>>>()
        {
            @Override
            public void execute(AsyncResult<List<Category>> result)
            {
                if (result.isSuccessful())
                {
                    CategorySpinnerAdapter spinnerAdapter = new CategorySpinnerAdapter(EditSourceActivity.this, result.getResult());
                    categorySpinner.setAdapter(spinnerAdapter);
                    categorySpinner.setSelection(spinnerAdapter.getPosition(rssSource.getCategory()));
                }
            }
        });

        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setEnabled(MenuItem menuItem, boolean value)
    {
        if (menuItem != null)
        {
            menuItem.setEnabled(value);
            menuItem.getIcon().setAlpha(value ? 255 : 130);
        }
    }

    private void notifyValueChanged()
    {
        String newTitle = titleText.getText().toString();
        Category newCategory = (Category) categorySpinner.getSelectedItem();

        boolean isChanged = !rssSource.getName().equals(newTitle) ||
                (rssSource.getCategoryId() != newCategory.getId());

        setEnabled(saveMenuItem, isChanged);
    }
}
