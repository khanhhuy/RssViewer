package assignment.rssviewer.service;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import assignment.rssviewer.model.Category;

/**
 * Created by Prozacs on 25/03/2015.
 */
public class RssApplication extends Application
{
    private LinkedHashMap<Integer, Category> categories;
    private Random random = new Random();

    @Override
    public void onCreate()
    {
        super.onCreate();
        DataService.initService();
        categories = DataService.getAllCategories();
    }

    public Category getCategoryById(int id)
    {
        return categories.get(Integer.valueOf(id));
    }

    public List<Category> getCategories()
    {
        return new ArrayList<>(categories.values());
    }

    public Category createNewCategory(String name)
    {
        int id;
        while (true)
        {
            id = random.nextInt();
            if (!categories.keySet().contains(id))
                break;
        }

        Category category = new Category();
        category.id = id;
        category.name = name;
        category.rssSources = new ArrayList<>();
        categories.put(id, category);

        return category;
    }

    public void removeCategory(Category c)
    {
        this.removeCategory(c, true);
    }

    private void removeCategory(Category c, boolean saveToDb)
    {
        categories.remove(c.id);
        // TODO: if saveToDb == true -> save changes to database
    }

    public void removeCategories(List<Category> categoriesToRemove)
    {
        for (Category c : categoriesToRemove)
        {
            removeCategory(c, false);
        }
        // TODO: code to save changes to database here
    }
}