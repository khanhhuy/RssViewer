package assignment.rssviewer.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.CategoryListAdapter;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.SharedDataKey;

public class AddSourceActivity extends FragmentActivity
        implements CustomSourceFragment.OnFragmentInteractionListener, SuggestionFragment.OnFragmentInteractionListener
{
    private static final String ID_KEY = "id";
    private static final String SUGGESTION_TAB = "suggestion";
    private static final String CUSTOM_TAB = "custom";
    private Category currentCategory;
    private IDataService dataService;
    private SuggestionFragment suggestionFragment;
    private CustomSourceFragment customFragment;

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
        dataService.insertAsync(RssSource.class, rssSource, null);
        currentCategory.getRssSources().add(rssSource);
        Toast.makeText(this, String.format("Added to %s", currentCategory.getName()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryAdded(Category category)
    {
        Toast.makeText(this, String.format("Category %s added", category.getName()), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_source);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec(SUGGESTION_TAB)
                              .setIndicator("Suggestion")
                              .setContent(new DummyTabContentFactory(this)));

        tabHost.addTab(tabHost.newTabSpec(CUSTOM_TAB)
                              .setIndicator("Add")
                              .setContent(new DummyTabContentFactory(this)));

        tabHost.setCurrentTabByTag(SUGGESTION_TAB);

        suggestionFragment = (SuggestionFragment) Fragment.instantiate(this, SuggestionFragment.class.getName());
        customFragment = (CustomSourceFragment) Fragment.instantiate(this, CustomSourceFragment.class.getName());

        FragmentTransaction frmTx = getSupportFragmentManager().beginTransaction();
        frmTx.add(android.R.id.tabcontent, suggestionFragment, SUGGESTION_TAB);
        frmTx.add(android.R.id.tabcontent, customFragment, CUSTOM_TAB);
        frmTx.hide(customFragment);
        frmTx.commit();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId)
            {
                hideKeyboard(getCurrentFocus());
                showTabContent(tabId);
            }
        });

        Bundle args = getIntent().getExtras();
        long categoryId = args.getLong(ID_KEY);

        dataService = ((RssApplication) getApplication()).getDataService();
        currentCategory = dataService.loadById(Category.class, categoryId);
    }

    private void hideKeyboard(View view)
    {
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showTabContent(String tabId)
    {
        FragmentTransaction frmTx = getSupportFragmentManager().beginTransaction();
        switch (tabId)
        {
            case SUGGESTION_TAB:
                frmTx.show(suggestionFragment);
                frmTx.hide(customFragment);
                break;
            case CUSTOM_TAB:
                frmTx.show(customFragment);
                frmTx.hide(suggestionFragment);
                break;
        }
        frmTx.commit();
    }

    static class DummyTabContentFactory implements TabHost.TabContentFactory
    {
        private Context context;

        private DummyTabContentFactory(Context context)
        {
            this.context = context;
        }

        @Override
        public View createTabContent(String tag)
        {
            View v = new View(context);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }
}
