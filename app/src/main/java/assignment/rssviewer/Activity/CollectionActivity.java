package assignment.rssviewer.activity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import assignment.rssviewer.R;
import assignment.rssviewer.fragment.EditCategoryDialog;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.service.RssApplication;

public class CollectionActivity extends ActionBarActivity
    implements EditCategoryDialog.NoticeDialogLisnener
{
    private RssApplication application;
    private ListView lvCategories;

    private AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener()
    {
        @Override
        public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked)
        {
            MenuItem menuItem = mode.getMenu().findItem(R.id.action_edit);
            menuItem.setEnabled(lvCategories.getCheckedItemCount() == 1);
        }

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu_collection, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.action_remove:
                    return true;
                case R.id.action_edit:
                    editCategory(lvCategories.getCheckedItemPosition());
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode)
        {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);

        application = (RssApplication) getApplication();
        lvCategories = (ListView) findViewById(R.id.lvCategories);
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, application.getCategories());
        lvCategories.setAdapter(adapter);

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Category selectedCategory = (Category) parent.getItemAtPosition(position);
                showCategoryView(selectedCategory.id);
            }
        });

        lvCategories.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvCategories.setMultiChoiceModeListener(multiChoiceModeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_new)
        {
            createNewCategory();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCategoryView(int categoryId)
    {
        Intent categoryIntent = new Intent(CollectionActivity.this, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", categoryId);
        categoryIntent.putExtras(bundle);
        startActivity(categoryIntent);
    }

    private void createNewCategory()
    {
        EditCategoryDialog editCategoryDialog = new EditCategoryDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", "New Category");
        bundle.putString("content", "");
        bundle.putBoolean("isNew", true);
        bundle.putInt("id", 0);
        editCategoryDialog.setArguments(bundle);
        editCategoryDialog.show(getFragmentManager(), "editCategoryDialog");
    }

    private void editCategory(int position)
    {
        Category category = (Category)lvCategories.getAdapter().getItem(position);
        EditCategoryDialog editCategoryDialog = new EditCategoryDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Edit Category");
        bundle.putString("content", category.name);
        bundle.putBoolean("isNew", false);
        bundle.putInt("id", category.id);
        editCategoryDialog.setArguments(bundle);
        editCategoryDialog.show(getFragmentManager(), "editCategoryDialog");
    }

    @Override
    public void onPositiveClicked(boolean isNew, int id, String content)
    {
        Category category;
        if (!isNew)
        {
            category = application.getCategoryById(id);
            category.name = content;
        }
        else
        {
            category = application.createNewCategory(content);
        }
        showCategoryView(category.id);
    }

    @Override
    public void onNegativeClicked(boolean isNew, int id, String content)
    {
    }
}
