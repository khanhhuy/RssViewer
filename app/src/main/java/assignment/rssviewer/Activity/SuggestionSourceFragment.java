package assignment.rssviewer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.SuggestionSourceAdapter;
import assignment.rssviewer.model.SuggestionCategory;
import assignment.rssviewer.model.SuggestionSource;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.Action;

public class SuggestionSourceFragment extends Fragment
{
    private static final String ID_KEY = "id";
    private OnFragmentInteractionListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_suggestion_source, container, false);

        Activity activity = getActivity();
        RssApplication application = (RssApplication) activity.getApplication();
        IDataService dataService = application.getDataService();

        Button backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener != null)
                    listener.onGoingBack(SuggestionSourceFragment.this);
            }
        });

        Bundle args = getArguments();
        long categoryId = args.getLong(ID_KEY);
        SuggestionCategory category = dataService.loadById(SuggestionCategory.class, categoryId);

        ListView lvSources = (ListView) view.findViewById(R.id.lvSources);
        SuggestionSourceAdapter adapter = new SuggestionSourceAdapter(activity, category.getSources(), new Action<SuggestionSource>()
        {
            @Override
            public void execute(SuggestionSource suggestionSource)
            {
                if (listener != null)
                    listener.onSourceSelected(suggestionSource);
            }
        });
        lvSources.setAdapter(adapter);

        return view;
    }

    public static Bundle createArgs(long categoryId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(ID_KEY, categoryId);
        return bundle;
    }

    public void setListener(OnFragmentInteractionListener listener)
    {
        this.listener = listener;
    }

    public interface OnFragmentInteractionListener
    {
        public void onSourceSelected(SuggestionSource source);
        public void onGoingBack(SuggestionSourceFragment sender);
        public void onAddCategory();
    }
}
