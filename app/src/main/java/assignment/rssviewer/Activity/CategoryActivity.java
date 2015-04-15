package assignment.rssviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.SourceViewAdapter;
import assignment.rssviewer.adapter.ViewHolderAdapter;
import assignment.rssviewer.dialog.ConfirmDialog;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;
import assignment.rssviewer.utils.ListViewHelper;

public class CategoryActivity extends Activity
{
    public static final String ID_KEY = "id";
    private IDataService dataService;
    private Category currentCategory;
    private ViewHolderAdapter<RssSource> sourceAdapter;
    private ListView lvSources;
    private final ConfirmDialog confirmDeletionDialog = new ConfirmDialog();

    private AbsListView.MultiChoiceModeListener lvSourcesMultiChoiceListener = new AbsListView.MultiChoiceModeListener()
    {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
        {
            MenuItem menuItem = mode.getMenu().findItem(R.id.action_edit);
            if (lvSources.getCheckedItemCount() == 1)
            {
                menuItem.setEnabled(true);
                menuItem.getIcon().setAlpha(255);
            }
            else
            {
                menuItem.setEnabled(false);
                menuItem.getIcon().setAlpha(130);
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu_category, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.action_remove:
                    confirmDeletion();
                    mode.finish();
                    return true;
                case R.id.action_edit:
                    editSource(ListViewHelper.getFirstSelectedItem(RssSource.class, lvSources));
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
        }
    };

    private ConfirmDialog.OnClosedListener confirmDeletionOnClosedListener = new ConfirmDialog.OnClosedListener()
    {
        @Override
        public void onAccepted()
        {
            final List<RssSource> selectedSources = ListViewHelper.getSelectedItems(RssSource.class, lvSources);

            dataService.deleteAsync(RssSource.class, new Action<AsyncResult<Void>>()
            {
                @Override
                public void execute(AsyncResult<Void> result)
                {
                    if (result.isSuccessful())
                    {
                        for (RssSource s : selectedSources)
                            sourceAdapter.remove(s);
                    }
                }
            }, selectedSources);
        }

        @Override
        public void onCanceled()
        {
        }
    };

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
        confirmDeletionDialog.setOnClosedListener(confirmDeletionOnClosedListener);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
            lvSources = (ListView) findViewById(R.id.lvSources);
            sourceAdapter = new SourceViewAdapter(this, currentCategory.getRssSources());
            lvSources.setAdapter(sourceAdapter);
            lvSources.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            lvSources.setMultiChoiceModeListener(lvSourcesMultiChoiceListener);

            lvSources.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    RssSource rssSource = sourceAdapter.getItem(position);
                    editSource(rssSource);
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

    private void editSource(RssSource source)
    {
        if (source != null)
        {
            Bundle args = EditSourceActivity.createArgs(source.getId());
            Intent intent = new Intent(this, EditSourceActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        }
    }

    private void confirmDeletion()
    {
        Bundle bundle = ConfirmDialog.createArgs("Confirm Deletion", "Are you sure you want to remove these sources?");
        confirmDeletionDialog.setArguments(bundle);
        confirmDeletionDialog.show(getFragmentManager(), "confirmDeletionDialog");
    }
}
