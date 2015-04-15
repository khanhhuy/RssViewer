package assignment.rssviewer.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.activity.WebViewActivity;
import assignment.rssviewer.adapter.PostListAdapter;
import assignment.rssviewer.model.Article;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.service.RssParser;
import assignment.rssviewer.utils.ListViewHelper;

public class FeedListFragment extends BaseMainFragment
{
    private List<Article> listArticle;
    private PostListAdapter adapter;
    private ListViewHelper.SupportWidget supportWidget;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        {
            Article article = listArticle.get(arg2);
            Bundle articLink = new Bundle();
            articLink.putString("url", article.getUrlString());

            Intent postviewIntent = new Intent(getActivity(), WebViewActivity.class);
            postviewIntent.putExtras(articLink);
            startActivity(postviewIntent);
        }
    };

    public FeedListFragment()
    {
        this.setTitle("Feed List");
        this.setIconResource(R.drawable.ic_action_expand);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Activity activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);
        listArticle = new ArrayList<>();
        adapter = new PostListAdapter(activity, R.layout.list_item_feed, listArticle);

        ListView listView = (ListView) view.findViewById(R.id.postListView);
        if (listView == null)
            Log.d("Add new View", "listView is null");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);

        View busyIndicator = view.findViewById(R.id.busyIndicator);
        View emptyText = view.findViewById(R.id.emptyText);
        supportWidget = new ListViewHelper.SupportWidget(listView, null, busyIndicator, emptyText);

        //testing rss source Parser.
        final List<RssSource> rssSourceList = new ArrayList<>();
        rssSourceList.add(new RssSource(0L, "Gamespot", "http://www.gamespot.com/feeds/news/", 0));
        rssSourceList.add(new RssSource(0L, "Gamespot", "http://www.theverge.com/rss/index.xml", 0));
        new AsyncTask<String, Integer, List<Article>>()
        {
            @Override
            protected void onPreExecute()
            {
                supportWidget.toggleStatus(ListViewHelper.Status.LOADING);
            }

            @Override
            protected List<Article> doInBackground(String... params)
            {
                RssParser parser = new RssParser();
                return parser.parseArticles(rssSourceList);
            }

            @Override
            protected void onPostExecute(List<Article> result)
            {
                Log.d("Article List: ", Integer.valueOf(result.size()).toString());
                supportWidget.toggleStatus(ListViewHelper.Status.NORMAL);
                for (Article article : result)
                {
                    listArticle.add(article);
                }
                adapter.notifyDataSetChanged();
            }
        }.execute();

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

    @Override
    public boolean isStatic()
    {
        return true;
    }


    /*private void generateDummyData(List<PostData> listData)
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
    }*/
}
