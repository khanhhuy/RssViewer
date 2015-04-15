package assignment.rssviewer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.SourceAddAdapter;
import assignment.rssviewer.adapter.ViewHolderAdapter;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.service.IRssService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.utils.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomSourceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CustomSourceFragment extends Fragment
{
    private final Action<RssSource> onItemSelected = new Action<RssSource>()
    {
        @Override
        public void execute(RssSource rssSource)
        {
            mListener.onSourceSelected(rssSource);
        }
    };
    private OnFragmentInteractionListener mListener;
    /*private EditText searchText;
    private ListView resultList;*/
    private ViewHolderAdapter<RssSource> resultAdapter;
    private IRssService rssService;
    private ListViewHelper.SupportWidget supportWidget;

    public CustomSourceFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        RssApplication application = (RssApplication) getActivity().getApplication();
        rssService = application.getRssService();

        View view = inflater.inflate(R.layout.fragment_custom_source, container, false);
        EditText searchText = (EditText) view.findViewById(R.id.searchText);
        ListView resultList = (ListView) view.findViewById(R.id.resultList);

        resultAdapter = new SourceAddAdapter(getActivity(), new ArrayList<RssSource>(), onItemSelected);
        resultList.setAdapter(resultAdapter);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    performSearch(v.getText().toString());
                }
                return false;
            }
        });

        View parent = view.findViewById(R.id.supportWidget);
        View busyIndicator = view.findViewById(R.id.busyIndicator);
        View emptyText = view.findViewById(R.id.emptyText);
        supportWidget = new ListViewHelper.SupportWidget(resultList, parent, busyIndicator, emptyText);

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
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
        mListener = null;
    }

    private void performSearch(String text)
    {
        AsyncTaskHelper.execute(
                new Action<Void>() // preExecute
                {
                    @Override
                    public void execute(Void aVoid)
                    {
                        supportWidget.toggleStatus(ListViewHelper.Status.LOADING);
                    }
                },
                new Func<String, RssSource>() // doInBackground
                {
                    @Override
                    public RssSource execute(String s)
                    {
                        return rssService.parse(s);
                    }
                },
                new Action<AsyncResult<RssSource>>() // postExecute
                {
                    @Override
                    public void execute(AsyncResult<RssSource> result)
                    {
                        if (result.isSuccessful())
                        {
                            RssSource source = result.getResult();
                            resultAdapter.clear();
                            if (source != null)
                            {
                                supportWidget.toggleStatus(ListViewHelper.Status.NORMAL);
                                resultAdapter.add(source);
                            }
                            else
                            {
                                supportWidget.toggleStatus(ListViewHelper.Status.EMPTY);
                            }
                        }
                    }
                },
                text // param
        );
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
