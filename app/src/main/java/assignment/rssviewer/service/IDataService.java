package assignment.rssviewer.service;

import android.os.AsyncTask;

import java.util.List;

import assignment.rssviewer.utils.Action;
import assignment.rssviewer.utils.AsyncResult;
import assignment.rssviewer.utils.SortDescription;

/**
 * Created by Prozacs on 06/04/2015.
 */
public interface IDataService
{
    public void initializeAsync(final Action<AsyncResult<Void>> onCompleted);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>> loadAllEntitiesAsync(Class<TEntity> entityClass,
                                                                                            Action<AsyncResult<List<TEntity>>> onCompleted);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>> loadAllEntitiesAsync(Class<TEntity> entityClass,
                                                                                            Action<AsyncResult<List<TEntity>>> onCompleted,
                                                                                            SortDescription orderBy);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<TEntity>> insertEntityAsync(TEntity entity, Action<AsyncResult<TEntity>> onCompleted);

    public <TEntity, TKey> TEntity getEntityById(final Class<TEntity> entityClass, TKey id);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> deleteEntityAsync(Class<TEntity> entityClass,
                                                                                Action<AsyncResult<Void>> onCompleted,
                                                                                TEntity... entities);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> updateEntityAsync(Class<TEntity> entityClass,
                                                                                Action<AsyncResult<Void>> onCompleted,
                                                                                TEntity... entities);
}
