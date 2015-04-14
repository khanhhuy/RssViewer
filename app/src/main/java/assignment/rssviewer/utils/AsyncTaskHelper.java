package assignment.rssviewer.utils;

import android.os.AsyncTask;

public class AsyncTaskHelper
{
    public static <TParam, TResult>
    AsyncTask<Void, Void, AsyncResult<TResult>> execute(final Func<TParam, TResult> worker,
                                                        final Action<AsyncResult<TResult>> workCompleter,
                                                        final TParam param)
    {
        return new AsyncTask<Void, Void, AsyncResult<TResult>>()
        {
            @Override
            protected final AsyncResult<TResult> doInBackground(Void... params)
            {
                try
                {
                    TResult result = worker.execute(param);
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
                if (workCompleter != null)
                    workCompleter.execute(result);
            }
        }.execute();
    }
}
