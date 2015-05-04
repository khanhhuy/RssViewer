package assignment.rssviewer.service;

import android.app.Application;
import android.webkit.MimeTypeMap;

import java.util.HashMap;

import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;

public class RssApplication extends Application
{
    private IDataService dataService;
    private IRssService rssService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        dataService = new GreenDaoService(this);
        rssService = new RssParser();
    }

    public IDataService getDataService()
    {
        return dataService;
    }

    public IRssService getRssService()
    {
        return rssService;
    }

    public void reCreateDataServiceAsync(Action<AsyncResult<Void>> onCompleted)
    {
        dataService = new GreenDaoService(this);
        dataService.initializeAsync(onCompleted);
    }
}
