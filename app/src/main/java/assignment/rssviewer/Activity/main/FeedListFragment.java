package assignment.rssviewer.activity.main;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import assignment.rssviewer.R;
import assignment.rssviewer.activity.WebViewActivity;
import assignment.rssviewer.adapter.CategorySwapperAdapter;
import assignment.rssviewer.adapter.PostListAdapter;
import assignment.rssviewer.model.Article;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.service.IDataService;
import assignment.rssviewer.service.RssApplication;
import assignment.rssviewer.service.RssParser;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;
import assignment.rssviewer.utils.ListViewHelper;

public class FeedListFragment extends BaseMainFragment {
    private List<Article> listArticle;
    private PostListAdapter adapter;
    private ListViewHelper.SupportWidget supportWidget;
    private final List<Category> categoryList = new ArrayList<>();
    private HashMap<String, Bitmap> categoryThumbnail = new HashMap<>();
    private Menu menu;
    private HashMap<String, Category> categoryOption = new HashMap<>();
    private AsyncTask<String, Integer, List<Article>> rssSourceTask = null;
    private AsyncTask<Object, Void, Void> rssBitmapTask = null;



    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Article article = listArticle.get(arg2);
            Bundle bundle = new Bundle();
            bundle.putString("url", article.getUrlString());
            bundle.putString("title", article.getTitle());

            Intent postviewIntent = new Intent(getActivity(), WebViewActivity.class);
            postviewIntent.putExtras(bundle);
            startActivity(postviewIntent);
        }
    };

    public FeedListFragment() {
        this.setTitle("Feed List");
        this.setIconResource(R.drawable.ic_action_expand);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);
        listArticle = new ArrayList<>();
        adapter = new PostListAdapter(activity, R.layout.list_item_feed, listArticle, categoryThumbnail);
        categoryList.clear();

        ListView listView = (ListView) view.findViewById(R.id.postListView);
        if (listView == null)
            Log.d("Add new View", "listView is null");
        listView.setAdapter(adapter);


        View busyIndicator = view.findViewById(R.id.busyIndicator);
        View emptyText = view.findViewById(R.id.emptyText);
        supportWidget = new ListViewHelper.SupportWidget(listView, null, busyIndicator, emptyText);

        this.setHasOptionsMenu(true);

        //addCategorySpinner(view);
        listView.setOnItemClickListener(onItemClickListener);

        return view;
    }

    @Override
    public void onResume() {
        Log.d("onResume", "Feedlist on resume");
        super.onResume();
        loadCategory();
    }

    @Override
    public void onDestroy() {
        Log.d("onDestroy", "destroy feed list fragment");
        cancelAsyncTask();
        super.onDestroy();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        return;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_feed_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.filter_header) {
            return true;
        }
        else {
            loadRssSource(categoryOption.get(item.getTitle().toString()));
            recreateCategoryFilter(item.getTitle().toString());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    /**
     * Dynamically create Category filter
     */

    private void recreateCategoryFilter(String header) {

        if (categoryOption.size() > 0) {
            MenuItem menuItem = menu.findItem(R.id.filter_header);
            if (menuItem == null) {
                Log.d("Menu Item", "Null");
                return;
            }


            menuItem.setTitle(categoryOption.get(header).getName());

            Menu subMenu = menu.getItem(0).getSubMenu();
            subMenu.clear();

            int i = 1;
            for (Map.Entry<String, Category> entry : categoryOption.entrySet()) {
                if (entry.getKey() != header) {
                    subMenu.add(0, i, i, entry.getValue().getName());
                    i++;
                }
            }

        }

        onPrepareOptionsMenu(menu);

    }

    private IDataService dataService;

    private void loadCategory() {

        if (categoryList.size() > 0)
            return;

        final Activity activity = getActivity();
        final RssApplication application = (RssApplication) activity.getApplication();
        dataService = application.getDataService();


        dataService.initializeAsync(new Action<AsyncResult<Void>>() {
            @Override
            public void execute(final AsyncResult<Void> initResult) {
                if (initResult.isSuccessful()) {
                    dataService.loadAllAsync(Category.class, new Action<AsyncResult<List<Category>>>() {
                        @Override
                        public void execute(AsyncResult<List<Category>> loadResult) {
                            if (loadResult.isSuccessful()) {
                                ArrayList<Category> catList = new ArrayList<Category>(loadResult.getResult());
                                categoryList.clear();
                                categoryOption.clear();
                                for (Category category : catList) {
                                    categoryList.add(category);
                                    categoryOption.put(category.getName(), category);
                                }
                                if (categoryList.size() > 0) {
                                    //spinnerAdapter.notifyDataSetChanged();

                                    recreateCategoryFilter(categoryList.get(0).getName());

                                    loadRssSource(categoryList.get(0));
                                    Log.d("Category size", new Integer(categoryList.size()).toString());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void cancelAsyncTask() {
        if (rssSourceTask != null && !rssSourceTask.isCancelled()){
            rssSourceTask.cancel(true);
            Log.d("Cancel", "RSS Source");
        }
        if (rssBitmapTask != null && !rssBitmapTask.isCancelled()){
            rssBitmapTask.cancel(true);
            Log.d("Cancel", "RSS Bitmap");
        }
        categoryThumbnail.clear();
        listArticle.clear();
        adapter.notifyDataSetChanged();
    }

    private void loadRssSource(final Category category) {

        cancelAsyncTask();

        rssSourceTask = new AsyncTask<String, Integer, List<Article>>() {
            @Override
            protected void onPreExecute() {
                supportWidget.toggleStatus(ListViewHelper.Status.LOADING);
            }

            @Override
            protected List<Article> doInBackground(String... params) {
                RssParser parser = new RssParser();
                return parser.parseArticles(category.getRssSources());
            }

            @Override
            protected void onPostExecute(List<Article> result) {
                Log.d("Article List: ", Integer.valueOf(result.size()).toString());
                supportWidget.toggleStatus(ListViewHelper.Status.NORMAL);

                for (Article article : result) {
                    listArticle.add(article);
                }
                adapter.notifyDataSetChanged();
                loadBitmap();
            }
        };

        rssSourceTask.execute();


    }



    public void loadBitmap() {

        if (listArticle.size() > 0) {

            rssBitmapTask = new AsyncTask<Object, Void, Void>() {
                @Override
                protected Void doInBackground(Object... args) {

                    for (Article article : listArticle) {
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;

                        try {
                            if (article.getImageUrl() != null) {
                                Bitmap bitmap = BitmapFactory.decodeStream(
                                        (InputStream) new URL(article.getImageUrl()).getContent(), null, options);
                                if (bitmap != null)
                                    categoryThumbnail.put(article.getImageUrl(), bitmap);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    return null;
                }

                @Override
                protected void onPostExecute(Void obj) {
                    adapter.notifyDataSetChanged();
                }
            };

            rssBitmapTask.execute();
        }
    }

}