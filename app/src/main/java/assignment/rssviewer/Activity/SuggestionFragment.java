package assignment.rssviewer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.model.SuggestionCategory;
import assignment.rssviewer.model.SuggestionSource;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SuggestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SuggestionFragment extends Fragment
{
    private OnFragmentInteractionListener listener;
    private SuggestionCategoryFragment categoryFragment;
    private Activity parentActivity;
    private IDataService dataService;

    private SuggestionCategoryFragment.OnFragmentInteractionListener categoryListener = new SuggestionCategoryFragment.OnFragmentInteractionListener()
    {
        @Override
        public void onCategorySelected(SuggestionCategory category)
        {
            SuggestionSourceFragment sourceFragment =
                    (SuggestionSourceFragment) Fragment.instantiate(parentActivity, SuggestionSourceFragment.class.getName());
            sourceFragment.setListener(sourceListener);

            Bundle args = SuggestionSourceFragment.createArgs(category.getId());
            sourceFragment.setArguments(args);

            FragmentTransaction frmTx = getChildFragmentManager().beginTransaction();
            frmTx.hide(categoryFragment);
            frmTx.add(R.id.content_frame, sourceFragment);
            frmTx.show(sourceFragment);
            frmTx.commit();
        }
    };

    private SuggestionSourceFragment.OnFragmentInteractionListener sourceListener = new SuggestionSourceFragment.OnFragmentInteractionListener()
    {
        @Override
        public void onSourceSelected(SuggestionSource suggestionSource)
        {
            RssSource source = getSourceFromSuggestion(suggestionSource);
            if (listener != null)
                listener.onSourceSelected(source);
        }

        @Override
        public void onGoingBack(SuggestionSourceFragment fragment)
        {
            FragmentTransaction frmTx = getChildFragmentManager().beginTransaction();
            frmTx.remove(fragment);
            frmTx.show(categoryFragment);
            frmTx.commit();
        }

        @Override
        public void onAddCategory(final SuggestionCategory suggestionCategory)
        {
            final Category category = new Category();
            category.setName(suggestionCategory.getName());
            dataService.insertAsync(Category.class, category, new Action<AsyncResult<Category>>()
            {
                @Override
                public void execute(final AsyncResult<Category> categoryResult)
                {
                    if (categoryResult.isSuccessful())
                    {
                        for (SuggestionSource suggestionSource : suggestionCategory.getSources())
                        {
                            RssSource rssSource = getSourceFromSuggestion(suggestionSource);
                            rssSource.setCategoryId(category.getId());
                            category.getRssSources().add(rssSource);
                        }

                        dataService.insertAsync(RssSource.class, category.getRssSources(), new Action<AsyncResult<List<RssSource>>>()
                        {
                            @Override
                            public void execute(AsyncResult<List<RssSource>> sourceResult)
                            {
                                boolean t = sourceResult.isSuccessful();
                            }
                        });

                        if (listener != null)
                            listener.onCategoryAdded(category);
                    }
                }
            });
        }

        private RssSource getSourceFromSuggestion(SuggestionSource suggestionSource)
        {
            RssSource source = new RssSource();
            source.setName(suggestionSource.getName());
            source.setUrlString(suggestionSource.getUrlString());
            return source;
        }
    };

    public SuggestionFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);
        parentActivity = getActivity();
        RssApplication application = (RssApplication) parentActivity.getApplication();
        dataService = application.getDataService();

        FragmentManager frmMgr = getChildFragmentManager();
        FragmentTransaction frmTx = frmMgr.beginTransaction();

        categoryFragment = (SuggestionCategoryFragment) Fragment.instantiate(parentActivity, SuggestionCategoryFragment.class.getName());
        frmTx.add(R.id.content_frame, categoryFragment);
        frmTx.show(categoryFragment);

        frmTx.commit();

        categoryFragment.setListener(categoryListener);

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                                                 + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        public void onSourceSelected(RssSource rssSource);
        public void onCategoryAdded(Category category);
    }
}
