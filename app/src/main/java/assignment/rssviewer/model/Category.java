package assignment.rssviewer.model;

import java.util.List;

/**
 * Created by Prozacs on 25/03/2015.
 */
public class Category
{
    public int id;
    public String name;
    public List<RssSource> rssSources;

    @Override
    public String toString()
    {
        return name;
    }
}
