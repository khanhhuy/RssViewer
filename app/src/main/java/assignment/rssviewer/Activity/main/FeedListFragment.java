package assignment.rssviewer.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.adapter.PostListAdapter;
import assignment.rssviewer.utils.PostData;

public class FeedListFragment extends Fragment
{
    private final List<PostData> listData = new ArrayList<>();
    private PostListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.feed_list_layout, container, false);

        generateDummyData(listData);
        adapter = new PostListAdapter(getActivity(), R.layout.feed_item_layout, listData);
        ListView listView = (ListView) view.findViewById(R.id.postListView);

        if (listView == null)
            Log.d("Add new View", "listView is null");

        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_feed_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    protected int getChildViewLayout()
    {
        return R.layout.feed_list_layout;
    }*/

    /*@Override
    protected void onSetContentView(View rootView) {
        generateDummyData();
        adapter = new PostListAdapter(this, R.layout.feed_item_layout, listData);
        ListView listView = (ListView) rootView.findViewById(R.id.postListView);

        if (listView == null)
            Log.d("Add new View", "listView is null");

        listView.setAdapter(adapter);
    }*/

    private void generateDummyData(List<PostData> listData)
    {
        listData.clear();

        PostData data;
        for (int i = 0; i < 20; i++)
        {
            data = new PostData();
            data.postDate = "April 2, 2015";
            data.postTitle = "Post " + (i + 1)
                    + " Title: This is the Post Title from RSS Feed";
            data.postThumbUrl = null;
            listData.add(data);
        }
    }
}
