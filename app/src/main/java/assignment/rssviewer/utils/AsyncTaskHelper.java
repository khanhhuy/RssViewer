package assignment.rssviewer.utils;

import android.os.AsyncTask;

public class AsyncTaskHelper
{
    public static <TParam, TResult>
    AsyncTask<Void, Void, AsyncResult<TResult>> execute(final Action<Void> preExecutor,
                                                        final Func<TParam, TResult> backgroundWorker,
                                                        final Action<AsyncResult<TResult>> postExecutor,
                                                        final TParam param)
    {
        return new AsyncTask<Void, Void, AsyncResult<TResult>>()
        {
            @Override
            protected void onPreExecute()
            {
                if (preExecutor != null)
                    preExecutor.execute(null);
            }

            @Override
            protected final AsyncResult<TResult> doInBackground(Void... params)
            {
                try
                {
                    TResult result = backgroundWorker.execute(param);
                    return AsyncResult.FromResult(result);
                }
                catch (Exception ex)
                {
                    return AsyncResult.FromException(ex);
                }
            }

            @Override
            protected void onPostExecute(AsyncResult<TResult> result)
            {
                if (postExecutor != null)
                    postExecutor.execute(result);
            }
        }.execute();
    }
}
