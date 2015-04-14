package assignment.rssviewer.service;

import android.app.Application;

import assignment.rssviewer.model.RssSource;

public class RssApplication extends Application
{
    private IDataService dataService;
    private IRssService rssService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        dataService = new GreenDaoService(this);
        rssService = new TempRssService();
    }

    public IDataService getDataService()
    {
        return dataService;
    }

    public IRssService getRssService()
    {
        return rssService;
    }

    private class TempRssService implements IRssService
    {
        @Override
        public RssSource parse(String url)
        {
            if (url != null && !url.isEmpty())
            {
                RssSource rssSource = new RssSource();
                rssSource.setUriString(url);
                rssSource.setName("Source from url");
                return rssSource;
            }
            return null;
        }
    }
}
