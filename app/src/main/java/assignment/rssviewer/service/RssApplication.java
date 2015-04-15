package assignment.rssviewer.service;

import android.app.Application;

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
}
