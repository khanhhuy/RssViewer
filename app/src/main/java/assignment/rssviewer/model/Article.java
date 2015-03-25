package assignment.rssviewer.model;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Prozacs on 25/03/2015.
 */
public class Article
{
    public Date date;
    public String title;
    public boolean isRead;
    public String description;
    public Uri contentUri;
    public RssSource rssSource;
}
