package assignment.rssviewer.service;

import java.util.List;

import assignment.rssviewer.model.Article;
import assignment.rssviewer.model.RssSource;

public interface IRssService
{
    public RssSource parse(String url);
    //public List<Article> parseArticles(List<RssSource> listRssSource);
}
