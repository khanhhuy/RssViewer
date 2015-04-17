package assignment.rssviewer.service;

import android.app.Application;

import java.util.HashMap;

public class RssApplication extends Application
{
    private IDataService dataService;
    private IRssService rssService;
    private HashMap<String, Object> sharedData;

    @Override
    public void onCreate()
    {
        super.onCreate();
        dataService = new GreenDaoService(this);
        rssService = new RssParser();
        sharedData = new HashMap<>();
    }

    public IDataService getDataService()
    {
        return dataService;
    }

    public IRssService getRssService()
    {
        return rssService;
    }

    public void share(String key, Object data)
    {
        sharedData.put(key, data);
    }

    public Object getData(String key)
    {
        return sharedData.get(key);
    }
}
