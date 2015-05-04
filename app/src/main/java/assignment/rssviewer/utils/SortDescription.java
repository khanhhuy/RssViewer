package assignment.rssviewer.utils;

import java.util.Comparator;

import de.greenrobot.dao.Property;

/**
 * Created by Prozacs on 06/04/2015.
 */
public abstract class SortDescription<T> implements Comparator<T>
{
    private Property property;
    private SortOrder order;

    public SortDescription(Property property, SortOrder order)
    {
        this.property = property;
        this.order = order;
    }

    public Property getProperty()
    {
        return property;
    }

    public SortOrder getOrder()
    {
        return order;
    }

    public enum SortOrder
    {
        ASCENDING,
        DESCENDING
    }
}
