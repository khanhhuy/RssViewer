package assignment.rssviewer.service;

import assignment.rssviewer.model.RssSource;

public interface IRssService
{
    public RssSource parse(String url);
}
