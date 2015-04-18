package assignment.rssviewer.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.activity.CategoryActivity;
import assignment.rssviewer.adapter.CategoryListAdapter;
import assignment.rssviewer.dialog.ConfirmDialog;
import assignment.rssviewer.dialog.EditCategoryDialog;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.*;

public class MyCollectionFragment extends BaseMainFragment
{
    private final EditCategoryDialog editCategoryDialog = new EditCategoryDialog();
    private final ConfirmDialog confirmDeletionDialog = new ConfirmDialog();
    private ListViewHelper.SupportWidget supportWidget;
    private ListView lvCategories;
    private Menu menu;

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
                    confirmRemoving();
                    mode.finish();
                    return true;
                case R.id.action_edit:
                    editCategory(ListViewHelper.getFirstSelectedItem(Category.class, lvCategories));
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
    private ConfirmDialog.OnClosedListener confirmDeletionOnClosedListener = new ConfirmDialog.OnClosedListener()
    {
        @Override
        @SuppressWarnings("unchecked")
        public void onAccepted()
        {
            setIsBusy(true);
            final List<Category> selectedCategories = ListViewHelper.getSelectedItems(Category.class, lvCategories);

            dataService.deleteAsync(Category.class, selectedCategories, new Action<AsyncResult<Void>>()
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
            });
        }

        @Override
        public void onCanceled()
        {
        }
    };
    private CategoryListAdapter categoryAdapter;
    private ProgressBar progressBar;
    private IDataService dataService;
    private View thisView;
    private EditCategoryDialog.OnClosedListener editCategoryOnClosedListener = new EditCategoryDialog.OnClosedListener()
    {
        @Override
        public void onAccepted(boolean isNew, long id, final String content)
        {
            Category category;
            if (!isNew)
            {
                category = dataService.loadById(Category.class, id);
                category.setName(content);
                dataService.updateAsync(category, null);
            }
            else
            {
                setIsBusy(true);
                category = new Category();
                category.setName(content);

                dataService.insertAsync(Category.class, category, new Action<AsyncResult<Category>>()
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
        }

        @Override
        public void onCanceled(boolean isNew, long id, String content)
        {
        }
    };

    public MyCollectionFragment()
    {
        setTitle("My Library");
        setIconResource(R.drawable.ic_action_image_photo_library);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        thisView = inflater.inflate(R.layout.fragment_my_collection, container, false);

        final Activity activity = getActivity();
        final RssApplication application = (RssApplication) activity.getApplication();
        dataService = application.getDataService();
        lvCategories = (ListView) thisView.findViewById(R.id.lvCategories);

        View parent = activity.findViewById(R.id.supportWidget);
        View busyIndicator = activity.findViewById(R.id.busyIndicator);
        View emptyText = activity.findViewById(R.id.emptyText);
        supportWidget = new ListViewHelper.SupportWidget(lvCategories, parent, busyIndicator, emptyText);

        supportWidget.toggleStatus(ListViewHelper.Status.LOADING);
        dataService.initializeAsync(new Action<AsyncResult<Void>>()
        {
            @Override
            public void execute(final AsyncResult<Void> initResult)
            {
                if (initResult.isSuccessful())
                {
                    dataService.loadAllAsync(Category.class, new Action<AsyncResult<List<Category>>>()
                    {
                        @Override
                        public void execute(AsyncResult<List<Category>> loadResult)
                        {
                            if (loadResult.isSuccessful())
                            {
                                categoryAdapter = new CategoryListAdapter(activity, loadResult.getResult());
                                lvCategories.setAdapter(categoryAdapter);
                                supportWidget.toggleStatus(ListViewHelper.Status.NORMAL);
                                application.share(SharedDataKey.MAIN_CATEGORIES, categoryAdapter);
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

        return thisView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_collection, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    public boolean isStatic()
    {
        return false;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (categoryAdapter != null)
            categoryAdapter.notifyDataSetChanged();
    }

    private void showCategoryView(long categoryId)
    {
        Intent categoryIntent = new Intent(getActivity(), CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(CategoryActivity.ID_KEY, categoryId);
        categoryIntent.putExtras(bundle);
        startActivity(categoryIntent);
    }

    private void createNewCategory()
    {
        Bundle bundle = EditCategoryDialog.createArgs("New Category", "", true, 0);
        editCategoryDialog.setArguments(bundle);
        editCategoryDialog.show(getActivity().getFragmentManager(), "editCategoryDialog");
    }

    private void editCategory(Category category)
    {
        if (category != null)
        {
            Bundle bundle = EditCategoryDialog.createArgs("Edit Category", category.getName(), false, category.getId());
            editCategoryDialog.setArguments(bundle);
            editCategoryDialog.show(getActivity().getFragmentManager(), "editCategoryDialog");
        }
    }

    private void confirmRemoving()
    {
        Bundle bundle = ConfirmDialog.createArgs("Confirm Deletion", "Are you sure you want to remove these categories?");
        confirmDeletionDialog.setArguments(bundle);
        confirmDeletionDialog.show(getActivity().getFragmentManager(), "confirmDeletionDialog");
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
            progressBar = (ProgressBar) thisView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(value);
    }
}
