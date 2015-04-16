package assignment.rssviewer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import assignment.rssviewer.R;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.model.SuggestionCategory;
import assignment.rssviewer.model.SuggestionSource;

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
        public void onSourceSelected(SuggestionSource source)
        {
            RssSource selectedSource = new RssSource();
            selectedSource.setName(source.getName());
            selectedSource.setUrlString(source.getUrlString());
            if (listener != null)
                listener.onSourceSelected(selectedSource);
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
        public void onAddCategory()
        {
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

        FragmentManager frmMgr = getChildFragmentManager();
        parentActivity = getActivity();
        categoryFragment = (SuggestionCategoryFragment) Fragment.instantiate(parentActivity, SuggestionCategoryFragment.class.getName());

        FragmentTransaction frmTx = frmMgr.beginTransaction();
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
        // TODO: Update argument type and name
        public void onSourceSelected(RssSource rssSource);
    }
}
