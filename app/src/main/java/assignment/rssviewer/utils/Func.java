package assignment.rssviewer.utils;

/**
 * Created by Prozacs on 06/04/2015.
 */
public interface Func<TParam, TResult>
{
    public TResult execute(TParam param);
}
