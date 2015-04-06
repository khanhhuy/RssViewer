package assignment.rssviewer.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

import assignment.rssviewer.R;
import assignment.rssviewer.model.DaoMaster;
import assignment.rssviewer.model.DaoSession;
import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;
import assignment.rssviewer.utils.SortDescription;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;

public class GreenDaoService implements IDataService
{
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
                        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, context.getResources().getString(R.string.database_name), null);
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
    loadAllEntitiesAsync(final Class<TEntity> entityClass,
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
    loadAllEntitiesAsync(final Class<TEntity> entityClass,
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
    insertEntityAsync(final TEntity entity,
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

    public <TEntity, TKey> TEntity getEntityById(final Class<TEntity> entityClass, TKey id)
    {
        return daoSession.load(entityClass, id);
    }

    public final <TEntity> AsyncTask<Void, Void, AsyncResult<Void>>
    deleteEntityAsync(final Class<TEntity> entityClass,
                      final Action<AsyncResult<Void>> onCompleted,
                      final TEntity... entities)
    {
        return new AsyncTask<Void, Void, AsyncResult<Void>>()
        {
            @Override
            protected final AsyncResult<Void> doInBackground(Void... params)
            {
                try
                {
                    for (TEntity e : entities)
                        daoSession.delete(e);
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

    @Override
    public final <TEntity> AsyncTask<Void, Void, AsyncResult<Void>>
    updateEntityAsync(final Class<TEntity> entityClass,
                      final Action<AsyncResult<Void>> onCompleted,
                      final TEntity... entities)
    {
        return new AsyncTask<Void, Void, AsyncResult<Void>>()
        {
            @Override
            protected final AsyncResult<Void> doInBackground(Void... params)
            {
                try
                {
                    for (TEntity e : entities)
                        daoSession.update(e);
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
}
