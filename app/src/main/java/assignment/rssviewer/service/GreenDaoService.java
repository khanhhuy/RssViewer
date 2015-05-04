package assignment.rssviewer.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.model.Category;
import assignment.rssviewer.model.DaoMaster;
import assignment.rssviewer.model.DaoSession;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;
import assignment.rssviewer.utils.SortDescription;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;

public class GreenDaoService implements IDataService
{
    private final Context context;
    private DaoSession daoSession;
    private final HashMap<Class<?>, List<?>> cachedLists;

    public GreenDaoService(Context context)
    {
        this.context = context;
        this.cachedLists = new HashMap<>();
    }

    @Override
    public void initializeAsync(final Action<AsyncResult<Void>> onCompleted)
    {
        if (daoSession == null)
        {
            new AsyncTask<Void, Void, AsyncResult<SQLiteDatabase>>()
            {
                @Override
                protected AsyncResult<SQLiteDatabase> doInBackground(Void... params)
                {
                    try
                    {
                        RssDBOpenHelper helper = new RssDBOpenHelper(context, context.getResources().getString(R.string.database_name), null);
                        return AsyncResult.FromResult(helper.getWritableDatabase());
                    }
                    catch (Exception e)
                    {
                        return AsyncResult.FromException(e);
                    }
                }

                @Override
                protected void onPostExecute(AsyncResult<SQLiteDatabase> result)
                {
                    if (result.isSuccessful())
                    {
                        DaoMaster daoMaster = new DaoMaster(result.getResult());
                        daoSession = daoMaster.newSession();
                        if (onCompleted != null)
                            onCompleted.execute(AsyncResult.VoidResult());
                    }
                    else
                    {
                        AsyncResult<Void> errorResult = AsyncResult.FromException(result.getException());
                        if (onCompleted != null)
                            onCompleted.execute(errorResult);
                    }
                }
            }.execute();
        }
        else if (onCompleted != null)
        {
            onCompleted.execute(AsyncResult.VoidResult());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>>
    loadAllAsync(final Class<TEntity> entityClass,
                 final Action<AsyncResult<List<TEntity>>> onCompleted)
    {
        return new AsyncTask<Void, Void, AsyncResult<List<TEntity>>>()
        {
            @Override
            protected AsyncResult<List<TEntity>> doInBackground(Void... params)
            {
                try
                {
                    List<TEntity> result =  (List<TEntity>) cachedLists.get(entityClass);
                    if (result == null)
                    {
                        result = daoSession.loadAll(entityClass);
                        cachedLists.put(entityClass, result);
                    }
                    return AsyncResult.FromResult(result);
                }
                catch (Exception e)
                {
                    return AsyncResult.FromException(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<List<TEntity>> result)
            {
                if (onCompleted != null)
                    onCompleted.execute(result);
            }
        }.execute();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>>
    loadAllAsync(final Class<TEntity> entityClass,
                 final Action<AsyncResult<List<TEntity>>> onCompleted,
                 final SortDescription<TEntity> orderBy)
    {
        return new AsyncTask<Void, Void, AsyncResult<List<TEntity>>>()
        {
            @Override
            protected AsyncResult<List<TEntity>> doInBackground(Void... params)
            {
                try
                {
                    List<TEntity> result = (List<TEntity>) cachedLists.get(entityClass);
                    if (result != null)
                    {
                        Collections.sort(result, orderBy);
                    }
                    else
                    {
                        AbstractDao<TEntity, ?> dao = (AbstractDao<TEntity, ?>) daoSession.getDao(entityClass);
                        QueryBuilder<TEntity> queryBuilder = dao.queryBuilder();
                        switch (orderBy.getOrder())
                        {
                            case ASCENDING:
                                queryBuilder = queryBuilder.orderAsc(orderBy.getProperty());
                                break;
                            case DESCENDING:
                                queryBuilder = queryBuilder.orderDesc(orderBy.getProperty());
                                break;
                            default:
                                return null;
                        }
                        result = queryBuilder.list();
                        cachedLists.put(entityClass, result);
                    }

                    return AsyncResult.FromResult(result);
                }
                catch (Exception e)
                {
                    return AsyncResult.FromException(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<List<TEntity>> result)
            {
                if (onCompleted != null)
                    onCompleted.execute(result);
            }
        }.execute();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TEntity> AsyncTask<Void, Void, AsyncResult<TEntity>>
    insertAsync(final Class<TEntity> entityClass,
                final TEntity entity,
                final Action<AsyncResult<TEntity>> onCompleted)
    {
        return new AsyncTask<Void, Void, AsyncResult<TEntity>>()
        {
            @Override
            protected AsyncResult<TEntity> doInBackground(Void... params)
            {
                try
                {
                    daoSession.insert(entity);
                    List cachedList = cachedLists.get(entityClass);
                    if (cachedList != null)
                        cachedList.add(entity);
                    return AsyncResult.FromResult(entity);
                }
                catch (Exception e)
                {
                    return AsyncResult.FromException(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<TEntity> result)
            {
                if (onCompleted != null)
                {
                    onCompleted.execute(result);
                }
            }
        }.execute();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>>
    insertAsync(final Class<TEntity> entityClass,
                final Iterable<TEntity> entities,
                final Action<AsyncResult<List<TEntity>>> onCompleted)
    {
        return new AsyncTask<Void, Void, AsyncResult<List<TEntity>>>()
        {
            @Override
            protected AsyncResult<List<TEntity>> doInBackground(Void... params)
            {
                try
                {
                    final List<TEntity> results = new ArrayList<>();
                    final List<TEntity> cachedList = (List<TEntity>) cachedLists.get(entityClass);

                    daoSession.runInTx(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (TEntity entity : entities)
                            {
                                daoSession.insert(entity);
                                results.add(entity);
                                if (cachedList != null)
                                    cachedList.add(entity);
                            }
                        }
                    });

                    return AsyncResult.FromResult(results);
                }
                catch (Exception e)
                {
                    return AsyncResult.FromException(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<List<TEntity>> result)
            {
                if (onCompleted != null)
                {
                    onCompleted.execute(result);
                }
            }
        }.execute();
    }

    @Override
    public <TEntity, TKey> TEntity loadById(final Class<TEntity> entityClass, TKey id)
    {
        return daoSession.load(entityClass, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <TEntity> AsyncTask<Void, Void, AsyncResult<Void>>
    deleteAsync(final Class<TEntity> entityClass,
                final TEntity entity,
                final Action<AsyncResult<Void>> onCompleted)
    {
        return new AsyncTask<Void, Void, AsyncResult<Void>>()
        {
            @Override
            protected final AsyncResult<Void> doInBackground(Void... params)
            {
                try
                {
                    if (entityClass == Category.class)
                    {
                        List<RssSource> cachedSources = (List<RssSource>) cachedLists.get(RssSource.class);
                        Category category = (Category) entity;
                        for (RssSource source : category.getRssSources())
                        {
                            daoSession.delete(source);
                            if (cachedSources != null)
                                cachedSources.remove(source);
                        }
                    }

                    daoSession.delete(entity);
                    List<TEntity> cachedList = (List<TEntity>) cachedLists.get(entityClass);
                    if (cachedList != null)
                        cachedList.remove(entity);

                    return AsyncResult.VoidResult();
                }
                catch (Exception e)
                {
                    return AsyncResult.FromException(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<Void> result)
            {
                if (onCompleted != null)
                {
                    onCompleted.execute(result);
                }
            }
        }.execute();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <TEntity> AsyncTask<Void, Void, AsyncResult<Void>>
    deleteAsync(final Class<TEntity> entityClass,
                final Iterable<TEntity> entities,
                final Action<AsyncResult<Void>> onCompleted)
    {
        return new AsyncTask<Void, Void, AsyncResult<Void>>()
        {
            @Override
            protected final AsyncResult<Void> doInBackground(Void... params)
            {
                try
                {
                    final List<TEntity> cachedList = (List<TEntity>) cachedLists.get(entityClass);
                    daoSession.runInTx(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (TEntity e : entities)
                            {
                                if (entityClass == Category.class)
                                {
                                    List<RssSource> cachedSources = (List<RssSource>) cachedLists.get(RssSource.class);
                                    Category category = (Category) e;
                                    for (RssSource source : category.getRssSources())
                                    {
                                        daoSession.delete(source);
                                        if (cachedSources != null)
                                            cachedSources.remove(source);
                                    }
                                }
                                daoSession.delete(e);
                                if (cachedList != null)
                                    cachedList.remove(e);
                            }
                        }
                    });

                    return AsyncResult.VoidResult();
                }
                catch (Exception e)
                {
                    return AsyncResult.FromException(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<Void> result)
            {
                if (onCompleted != null)
                {
                    onCompleted.execute(result);
                }
            }
        }.execute();
    }

    @Override
    public final <TEntity> AsyncTask<Void, Void, AsyncResult<Void>>
    updateAsync(final Class<TEntity> entityClass,
                final Iterable<TEntity> entities,
                final Action<AsyncResult<Void>> onCompleted)
    {
        return new AsyncTask<Void, Void, AsyncResult<Void>>()
        {
            @Override
            protected final AsyncResult<Void> doInBackground(Void... params)
            {
                try
                {
                    daoSession.runInTx(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (TEntity e : entities)
                                daoSession.update(e);
                        }
                    });
                    return AsyncResult.VoidResult();
                }
                catch (Exception e)
                {
                    return AsyncResult.FromException(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<Void> result)
            {
                if (onCompleted != null)
                {
                    onCompleted.execute(result);
                }
            }
        }.execute();
    }

    @Override
    public final <TEntity> AsyncTask<Void, Void, AsyncResult<Void>>
    updateAsync(final Class<TEntity> entityClass,
                final TEntity entity,
                final Action<AsyncResult<Void>> onCompleted)
    {
        return new AsyncTask<Void, Void, AsyncResult<Void>>()
        {
            @Override
            protected final AsyncResult<Void> doInBackground(Void... params)
            {
                try
                {
                    daoSession.update(entity);
                    return AsyncResult.VoidResult();
                }
                catch (Exception e)
                {
                    return AsyncResult.FromException(e);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<Void> result)
            {
                if (onCompleted != null)
                {
                    onCompleted.execute(result);
                }
            }
        }.execute();
    }
}
