package assignment.rssviewer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import assignment.rssviewer.R;


public class WebViewActivity extends ActionBarActivity {

    private String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle bun = getIntent().getExtras();
        if (bun != null)
            url = bun.getString("url");
        openByWebView(url);
        //openByWebBrowser(url);
        //WebView myWebView = (WebView)findViewById(R.id.webview);
        //myWebView.loadUrl("http://www.24h.com.vn/bong-da/tieu-diem-big-5-nha-v31-liverpool-man-city-giuong-co-trang-c48a701176.html");
    }

    private void openByWebView(String url){
        WebView myWebView = (WebView)findViewById(R.id.webview);
        myWebView.loadUrl(url);
    }

    private void openByWebBrowser(String url){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);

        MenuItem shareItem = (MenuItem) menu.findItem(R.id.action_share);
        ShareActionProvider mShare = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);

        mShare.setShareIntent(shareIntent);
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
}

