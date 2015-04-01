package assignment.rssviewer.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import assignment.rssviewer.R;
import assignment.rssviewer.activity.adapter.PostListAdapter;
import assignment.rssviewer.activity.model.PostData;

public class FeedListActivity extends BaseDrawerActivity {

    ArrayList<PostData> listData;
    PostListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateDummyData();
        addNewView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNewView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_list_layout, null);
        adapter = new PostListAdapter(this, R.layout.feed_item_layout, listData);
        ListView listView = (ListView) view.findViewById(R.id.postListView);

        if (listView == null)
            Log.d("Add new View", "listView is null");

        listView.setAdapter(adapter);

        addContentView(view);
    }

    private void generateDummyData() {
        PostData data = null;
        listData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data = new PostData();
            data.postDate = "April 2, 2015";
            data.postTitle = "Post " + (i + 1)
                    + " Title: This is the Post Title from RSS Feed";
            data.postThumbUrl = null;
            listData.add(data);
        }
    }
}
