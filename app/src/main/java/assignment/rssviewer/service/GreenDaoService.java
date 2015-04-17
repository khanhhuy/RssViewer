package assignment.rssviewer.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
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
    //private static final IllegalStateException illegalStateException = new IllegalStateException("There is no session to perform this operation.");
    private final Context context;
    private DaoSession daoSession;

    public GreenDaoService(Context context)
    {
        this.context = context;
    }

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
                    return AsyncResult.FromResult(daoSession.loadAll(entityClass));
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

    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>>
    loadAllAsync(final Class<TEntity> entityClass,
                 final Action<AsyncResult<List<TEntity>>> onCompleted,
                 final SortDescription orderBy)
    {
        return new AsyncTask<Void, Void, AsyncResult<List<TEntity>>>()
        {
            @Override
            protected AsyncResult<List<TEntity>> doInBackground(Void... params)
            {
                try
                {
                    @SuppressWarnings("unchecked")
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

                    return AsyncResult.FromResult(queryBuilder.list());
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

    public <TEntity> AsyncTask<Void, Void, AsyncResult<TEntity>>
    insertAsync(Class<TEntity> entityClass,
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
                    onCompleted.execute(result);
            }
        }.execute();
    }

    @Override
    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>>
    insertAsync(Class<TEntity> entityClass,
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

                    daoSession.runInTx(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (TEntity entity : entities)
                            {
                                daoSession.insert(entity);
                                results.add(entity);
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
                    onCompleted.execute(result);
            }
        }.execute();
    }

    public <TEntity, TKey> TEntity loadById(final Class<TEntity> entityClass, TKey id)
    {
        return daoSession.load(entityClass, id);
    }

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
                        Category category = (Category)entity;
                        for (RssSource source : category.getRssSources())
                            daoSession.delete(source);
                    }
                    daoSession.delete(entity);
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
                    onCompleted.execute(result);
            }
        }.execute();
    }

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
                    daoSession.runInTx(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (TEntity e : entities)
                            {
                                if (entityClass == Category.class)
                                {
                                    Category category = (Category)e;
                                    for (RssSource source : category.getRssSources())
                                        daoSession.delete(source);
                                }
                                daoSession.delete(e);
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
                    onCompleted.execute(result);
            }
        }.execute();
    }

    public final <TEntity> AsyncTask<Void, Void, AsyncResult<Void>>
    updateAsync(final Iterable<TEntity> entities,
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
                    onCompleted.execute(result);
            }
        }.execute();
    }

    public final <TEntity> AsyncTask<Void, Void, AsyncResult<Void>>
    updateAsync(final TEntity entity,
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
                    onCompleted.execute(result);
            }
        }.execute();
    }

    /*@Override
    public void initDatabase()
    {
        if (daoSession == null)
        {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, context.getResources().getString(R.string.database_name), null);
            DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
            daoSession = daoMaster.newSession();
        }
    }

    @Override
    public <T> List<T> loadAll(Class<T> entityClass) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;

        return daoSession.loadAll(entityClass);
    }

    @Override
    public <T> List<T> loadAll(Class<T> entityClass, SortDescription... orderBy) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;

        AbstractDao<T, ?> dao = getDao(entityClass);
        QueryBuilder<T> queryBuilder = dao.queryBuilder();

        for (SortDescription condition : orderBy)
        {
            switch (condition.getOrder())
            {
                case ASCENDING:
                    queryBuilder = queryBuilder.orderAsc(condition.getProperty());
                    break;
                case DESCENDING:
                    queryBuilder = queryBuilder.orderDesc(condition.getProperty());
                    break;
            }
        }

        return queryBuilder.list();
    }

    @Override
    public <T> void insert(final Iterable<T> entities) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;

        daoSession.runInTx(new Runnable()
        {
            @Override
            public void run()
            {
                for (T e : entities)
                {
                    daoSession.insert(e);
                }
            }
        });
    }

    @Override
    public <T> T insert(T entity) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;

        daoSession.insert(entity);
        return entity;
    }

    @Override
    public <T, TKey> T loadByKey(Class<T> entityClass, TKey id) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;
        return daoSession.load(entityClass, id);
    }

    @Override
    public <T> void delete(final Iterable<T> entities) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;

        daoSession.runInTx(new Runnable()
        {
            @Override
            public void run()
            {
                for (T e : entities)
                {
                    daoSession.delete(e);
                }
            }
        });
    }

    @Override
    public <T> void delete(T entity) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;

        daoSession.delete(entity);
    }

    @Override
    public <T> void update(final Iterable<T> entities) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;

        daoSession.runInTx(new Runnable()
        {
            @Override
            public void run()
            {
                for (T e : entities)
                {
                    daoSession.update(e);
                }
            }
        });
    }

    @Override
    public <T> void update(T entity) throws IllegalStateException
    {
        if (daoSession == null)
            throw illegalStateException;

        daoSession.update(entity);
    }

    @SuppressWarnings("unchecked")
    private <T> AbstractDao<T, ?> getDao(Class<T> entityClass)
    {
        return (AbstractDao<T, ?>) daoSession.getDao(entityClass);
    }*/
}
