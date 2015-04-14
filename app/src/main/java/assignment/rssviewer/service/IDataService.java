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

    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>> loadAllAsync(Class<TEntity> entityClass,
                                                                                    Action<AsyncResult<List<TEntity>>> onCompleted);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<List<TEntity>>> loadAllAsync(Class<TEntity> entityClass,
                                                                                    Action<AsyncResult<List<TEntity>>> onCompleted,
                                                                                    SortDescription orderBy);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<TEntity>> insertAsync(TEntity entity, Action<AsyncResult<TEntity>> onCompleted);

    public <TEntity, TKey> TEntity loadById(final Class<TEntity> entityClass, TKey id);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> deleteAsync(Class<TEntity> entityClass,
                                                                          Action<AsyncResult<Void>> onCompleted,
                                                                          TEntity entities);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> deleteAsync(Class<TEntity> entityClass,
                                                                          Action<AsyncResult<Void>> onCompleted,
                                                                          Iterable<TEntity> entities);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> updateAsync(Class<TEntity> entityClass,
                                                                          Action<AsyncResult<Void>> onCompleted,
                                                                          TEntity entities);

    public <TEntity> AsyncTask<Void, Void, AsyncResult<Void>> updateAsync(Class<TEntity> entityClass,
                                                                          Action<AsyncResult<Void>> onCompleted,
                                                                          Iterable<TEntity> entities);
    /*public void initDatabase();
    public <T> List<T> loadAll(Class<T> entityClass) throws IllegalStateException;
    public <T> List<T> loadAll(Class<T> entityClass, SortDescription... orderBy) throws IllegalStateException;
    public <T> void insert(Iterable<T> entities) throws IllegalStateException;
    public <T> T insert(T entity) throws IllegalStateException;
    public <T, TKey> T loadByKey(Class<T> entityClass, TKey id) throws IllegalStateException;
    public <T> void delete(Iterable<T> entities) throws IllegalStateException;
    public <T> void delete(T entity) throws IllegalStateException;
    public <T> void update(Iterable<T> entity) throws IllegalStateException;
    public <T> void update(T entity) throws IllegalStateException;*/
}
