package assignment.rssviewer.utils;

/**
 * Created by Prozacs on 06/04/2015.
 */
public class AsyncResult<TResult>
{
    private static final AsyncResult<Void> voidResult = FromResult((Void) null);
    private boolean isSuccessful;
    private Exception exception;
    private TResult result;

    private AsyncResult(Exception exception)
    {
        this.exception = exception;
        this.isSuccessful = false;
        this.result = null;
    }

    private AsyncResult(TResult result)
    {
        this.exception = null;
        this.isSuccessful = true;
        this.result = result;
    }

    public static <TResult> AsyncResult<TResult> FromException(Exception exception)
    {
        return new AsyncResult<>(exception);
    }

    public static <TResult> AsyncResult<TResult> FromResult(TResult result)
    {
        return new AsyncResult<>(result);
    }

    public static AsyncResult<Void> VoidResult()
    {
        return voidResult;
    }

    public boolean isSuccessful()
    {
        return isSuccessful;
    }

    public Exception getException()
    {
        return exception;
    }

    public TResult getResult()
    {
        return this.result;
    }
}
