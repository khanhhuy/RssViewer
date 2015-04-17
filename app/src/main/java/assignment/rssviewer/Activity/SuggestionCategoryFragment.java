package assignment.rssviewer.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.SuggestionCategoryAdapter;
import assignment.rssviewer.model.SuggestionCategory;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;
import assignment.rssviewer.utils.Func;
import assignment.rssviewer.utils.ListViewHelper;

public class SuggestionCategoryFragment extends Fragment
{
    private OnFragmentInteractionListener listener;
    private ListViewHelper.SupportWidget supportWidget;
    private SuggestionCategoryAdapter categoryAdapter;
    private final Handler searchHandler = new Handler();
    private CharSequence searchText;

    private TextWatcher searchTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            searchText = s;
            searchHandler.removeCallbacks(filterTask);
            searchHandler.postDelayed(filterTask, 0);
        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };

    private Runnable filterTask = new Runnable()
    {
        @Override
        public void run()
        {
            final CharSequence s = searchText;
            Func<SuggestionCategory, Boolean> filterPredicate = null;
            if (s != null && s.length() > 0)
            {
                filterPredicate = new Func<SuggestionCategory, Boolean>()
                {
                    @Override
                    public Boolean execute(SuggestionCategory category)
                    {
                        return category.getName().toLowerCase().startsWith(s.toString().toLowerCase());
                    }
                };
            }
            categoryAdapter.filter(filterPredicate);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_suggestion_category, container, false);

        final Activity activity = getActivity();
        RssApplication application = (RssApplication) activity.getApplication();
        IDataService dataService = application.getDataService();

        final ListView lvCategories = (ListView) view.findViewById(R.id.suggestionCategories);
        View parent = activity.findViewById(R.id.supportWidget);
        View busyIndicator = activity.findViewById(R.id.busyIndicator);
        View emptyText = activity.findViewById(R.id.emptyText);
        supportWidget = new ListViewHelper.SupportWidget(lvCategories, parent, busyIndicator, emptyText);

        supportWidget.toggleStatus(ListViewHelper.Status.LOADING);
        dataService.loadAllAsync(SuggestionCategory.class, new Action<AsyncResult<List<SuggestionCategory>>>()
        {
            @Override
            public void execute(AsyncResult<List<SuggestionCategory>> result)
            {
                if (result.isSuccessful())
                {
                    supportWidget.toggleStatus(ListViewHelper.Status.NORMAL);
                    categoryAdapter = new SuggestionCategoryAdapter(activity, result.getResult());
                    lvCategories.setAdapter(categoryAdapter);
                }
            }
        });

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                listener.onCategorySelected(categoryAdapter.getItem(position));
            }
        });

        EditText searchText = (EditText) view.findViewById(R.id.searchText);
        searchText.addTextChangedListener(searchTextWatcher);

        return view;
    }

    public void setListener(OnFragmentInteractionListener listener)
    {
        this.listener = listener;
    }

    public interface OnFragmentInteractionListener
    {
        public void onCategorySelected(SuggestionCategory category);
    }
}
