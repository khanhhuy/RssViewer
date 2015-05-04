package assignment.rssviewer.service;

import android.os.AsyncTask;

import java.util.Collection;
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

    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>> loadAllAsync(Class<TEntity> entityClass,
                                                                                    Action<AsyncResult<List<TEntity>>> onCompleted);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>> loadAllAsync(Class<TEntity> entityClass,
                                                                                    Action<AsyncResult<List<TEntity>>> onCompleted,
                                                                                    SortDescription<TEntity> orderBy);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<TEntity>> insertAsync(Class<TEntity> entityClass,
                                                                             TEntity entity,
                                                                             Action<AsyncResult<TEntity>> onCompleted);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>> insertAsync(Class<TEntity> entityClass,
                                                                                   Iterable<TEntity> entites,
                                                                                   Action<AsyncResult<List<TEntity>>> onCompleted);

    public <TEntity, TKey> TEntity loadById(final Class<TEntity> entityClass, TKey id);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> deleteAsync(Class<TEntity> entityClass,
                                                                          TEntity entities,
                                                                          Action<AsyncResult<Void>> onCompleted);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> deleteAsync(Class<TEntity> entityClass,
                                                                          Iterable<TEntity> entities,
                                                                          Action<AsyncResult<Void>> onCompleted);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> updateAsync(Class<TEntity> entityClass,
                                                                          TEntity entities,
                                                                          Action<AsyncResult<Void>> onCompleted);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> updateAsync(Class<TEntity> entityClass,
                                                                          Iterable<TEntity> entities,
                                                                          Action<AsyncResult<Void>> onCompleted);
}
