package assignment.rssviewer.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.InputStreamReader;

import assignment.rssviewer.model.*;
import au.com.bytecode.opencsv.CSVReader;

public class RssDBOpenHelper extends DaoMaster.DevOpenHelper
{
    private final Context context;

    public RssDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory)
    {
        super(context, name, factory);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        super.onCreate(db);

        db.beginTransaction();
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStreamReader suggestionCategories = new InputStreamReader(assetManager.open("suggestion_categories.csv"));
            InputStreamReader suggestionSources = new InputStreamReader(assetManager.open("suggestion_sources.csv"));
            CSVReader categoriesReader = new CSVReader(suggestionCategories, ',');
            CSVReader sourcesReader = new CSVReader(suggestionSources, ',');

            String[] nextCategory;
            String[] nextSource = sourcesReader.readNext();
            while ((nextCategory = categoriesReader.readNext()) != null)
            {
                long id = db.insert(SuggestionCategoryDao.TABLENAME, null, categoryValues(nextCategory[0]));
                while (nextSource != null && Long.parseLong(nextSource[0]) == id)
                {
                    db.insert(SuggestionSourceDao.TABLENAME, null, sourceValues(nextSource[1], nextSource[2], id));
                    nextSource = sourcesReader.readNext();
                }
            }
            db.setTransactionSuccessful();
        }
        catch (Exception ex)
        {
            Log.e("error", "error reading initial data", ex);
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("greenDAO", "Downgrade schema from version " + oldVersion + " to " + newVersion + " by doing nothing.");
    }

    private ContentValues categoryValues(String name)
    {
        ContentValues values = new ContentValues();
        values.put(SuggestionCategoryDao.Properties.Name.columnName, name);
        return values;
    }

    private ContentValues sourceValues(String name, String url, long categoryId)
    {
        ContentValues values = new ContentValues();
        values.put(SuggestionSourceDao.Properties.Name.columnName, name);
        values.put(SuggestionSourceDao.Properties.UrlString.columnName, url);
        values.put(SuggestionSourceDao.Properties.CategoryId.columnName, categoryId);
        return values;
    }
}
