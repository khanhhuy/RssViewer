package assignment.rssviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.fragment.ConfirmDialog;
import assignment.rssviewer.fragment.EditCategoryDialog;
import assignment.rssviewer.lvadapter.CategoryAdapter;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;

public class CollectionActivity extends ActionBarActivity
{
    private final EditCategoryDialog editCategoryDialog = new EditCategoryDialog();
    private final ConfirmDialog confirmDeletionDialog = new ConfirmDialog();
    private ListView lvCategories;
    private CategoryAdapter categoryAdapter;
    private ProgressBar progressBar;
    private IDataService dataService;

    private AbsListView.MultiChoiceModeListener categoriesSelectionListener = new AbsListView.MultiChoiceModeListener()
    {
        @Override
        public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked)
        {
            MenuItem menuItem = mode.getMenu().findItem(R.id.action_edit);
            if (lvCategories.getCheckedItemCount() == 1)
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
                    removeCategories();
                    mode.finish();
                    return true;
                case R.id.action_edit:
                    editCategory(getFirstSelectedCategory());
                    mode.finish();
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

    private EditCategoryDialog.OnClosedListener editCategoryOnClosedListener = new EditCategoryDialog.OnClosedListener()
    {
        @Override
        public void onAccepted(boolean isNew, long id, final String content)
        {
            Category category;
            if (!isNew)
            {
                category = dataService.getEntityById(Category.class, id);
                category.setName(content);
                dataService.updateEntityAsync(Category.class, null, category);
            }
            else
            {
                setIsBusy(true);
                category = new Category();
                category.setName(content);

                dataService.insertEntityAsync(category, new Action<AsyncResult<Category>>()
                {
                    @Override
                    public void execute(AsyncResult<Category> result)
                    {
                        if (result.isSuccessful())
                        {
                            setIsBusy(false);
                            categoryAdapter.add(result.getResult());
                            showCategoryView(result.getResult().getId());
                        }
                    }
                });
            }
            notifyCategoriesChanged();
        }

        @Override
        public void onCanceled(boolean isNew, long id, String content)
        {
        }
    };

    private ConfirmDialog.OnClosedListener confirmDeletionOnClosedListener = new ConfirmDialog.OnClosedListener()
    {
        @Override
        @SuppressWarnings("unchecked")
        public void onAccepted()
        {
            setIsBusy(true);
            final Category[] selectedCategories = getSelectedCategories();

            dataService.deleteEntityAsync(Category.class, new Action<AsyncResult<Void>>()
            {
                @Override
                public void execute(AsyncResult<Void> result)
                {
                    if (result.isSuccessful())
                    {
                        for (Category c : selectedCategories)
                            categoryAdapter.remove(c);
                        setIsBusy(false);
                    }
                }
            }, selectedCategories);
        }

        @Override
        public void onCanceled()
        {
        }
    };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);

        RssApplication application = (RssApplication) getApplication();
        dataService = application.getDataService();
        lvCategories = (ListView) findViewById(R.id.lvCategories);

        setIsBusy(true);
        dataService.initializeAsync(new Action<AsyncResult<Void>>()
        {
            @Override
            public void execute(AsyncResult<Void> initResult)
            {
                if (initResult.isSuccessful())
                {
                    dataService.loadAllEntitiesAsync(Category.class, new Action<AsyncResult<List<Category>>>()
                    {
                        @Override
                        public void execute(AsyncResult<List<Category>> loadResult)
                        {
                            if (loadResult.isSuccessful())
                            {
                                categoryAdapter = new CategoryAdapter(CollectionActivity.this, loadResult.getResult());
                                lvCategories.setAdapter(categoryAdapter);
                                setIsBusy(false);
                            }
                        }
                    });
                }
            }
        });

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Category selectedCategory = (Category) parent.getItemAtPosition(position);
                showCategoryView(selectedCategory.getId());
            }
        });

        lvCategories.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvCategories.setMultiChoiceModeListener(categoriesSelectionListener);

        editCategoryDialog.setOnClosedListener(editCategoryOnClosedListener);
        confirmDeletionDialog.setOnClosedListener(confirmDeletionOnClosedListener);
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

    private void showCategoryView(long categoryId)
    {
        Intent categoryIntent = new Intent(CollectionActivity.this, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", categoryId);
        categoryIntent.putExtras(bundle);
        startActivity(categoryIntent);
    }

    private void createNewCategory()
    {
        Bundle bundle = new Bundle();
        bundle.putString("title", "New Category");
        bundle.putString("content", "");
        bundle.putBoolean("isNew", true);
        bundle.putLong("id", 0);
        editCategoryDialog.setArguments(bundle);
        editCategoryDialog.show(getFragmentManager(), "editCategoryDialog");
    }

    private void editCategory(Category category)
    {
        if (category != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("title", "Edit Category");
            bundle.putString("content", category.getName());
            bundle.putBoolean("isNew", false);
            bundle.putLong("id", category.getId());
            editCategoryDialog.setArguments(bundle);
            editCategoryDialog.show(getFragmentManager(), "editCategoryDialog");
        }
    }

    private void removeCategories()
    {
        Bundle bundle = new Bundle();
        bundle.putString("title", "Confirm Deletion");
        bundle.putString("content", "Are you sure you want to remove these categories?");
        confirmDeletionDialog.setArguments(bundle);
        confirmDeletionDialog.show(getFragmentManager(), "confirmDeletionDialog");
    }

    private Category getFirstSelectedCategory()
    {
        if (lvCategories != null)
        {
            SparseBooleanArray checkedPositions = lvCategories.getCheckedItemPositions();
            int pos = -1;
            for (int i = 0; i < lvCategories.getCount(); i++)
                if (checkedPositions.get(i))
                {
                    pos = i;
                    break;
                }

            if (pos >= 0)
            {
                return (Category) lvCategories.getAdapter().getItem(pos);
            }
        }
        return null;
    }

    private Category[] getSelectedCategories()
    {
        List<Category> selectedCategories = new ArrayList<>();

        if (lvCategories != null)
        {
            SparseBooleanArray checkedPositions = lvCategories.getCheckedItemPositions();
            for (int i = 0; i < lvCategories.getCount(); i++)
            {
                if (checkedPositions.valueAt(i))
                {
                    int pos = checkedPositions.keyAt(i);
                    Category c = (Category) lvCategories.getAdapter().getItem(pos);
                    selectedCategories.add(c);
                }
                else break;
            }
        }

        Category[] result = new Category[selectedCategories.size()];
        result = selectedCategories.toArray(result);

        return result;
    }

    private void setIsBusy(boolean value)
    {
        if (progressBar == null)
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(value);
    }

    private void notifyCategoriesChanged()
    {
        ((BaseAdapter) lvCategories.getAdapter()).notifyDataSetChanged();
    }
}
