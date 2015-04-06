package assignment.rssviewer.service;

import android.app.Application;

/**
 * Created by Prozacs on 25/03/2015.
 */
public class RssApplication extends Application
{
    private IDataService dataService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        dataService = new GreenDaoService(this);
    }

    public IDataService getDataService()
    {
        return dataService;
    }
}
