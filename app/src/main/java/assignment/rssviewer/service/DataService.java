package assignment.rssviewer.service;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.RssSource;

public class DataService
{
    static DataService instance;
    private Random random = new Random();

    public static void initService()
    {
        if (instance == null)
            instance = new DataService();
    }

    public static LinkedHashMap<Integer, Category> getAllCategories()
    {
        if (instance != null)
            return instance.getAllCategoriesInternal();
        else return new LinkedHashMap<>();
    }

    private LinkedHashMap<Integer, Category> getAllCategoriesInternal()
    {
        LinkedHashMap<Integer, Category> categories = new LinkedHashMap<>();

        for (int i = 0; i < 3; i++)
        {
            Category c = new Category();
            c.id = i;
            c.name = "Category " + random.nextInt(10);
            c.rssSources = new ArrayList<>();

            for (int j = 0; j < 3; j++)
            {
                RssSource s = getRssSource(c);
                c.rssSources.add(s);
            }

            categories.put(c.id, c);
        }

        return categories;
    }

    private RssSource getRssSource(Category c)
    {
        RssSource s = new RssSource();
        s.category = c;
        s.id = random.nextInt(10);
        s.name = "Source " + random.nextInt(10);
        s.sourceUri = Uri.parse("http://rss.source" + random.nextInt(10) + ".com");

        return s;
    }
}
