package assignment.rssviewer.model;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * Entity mapped to table RSS_SOURCE.
 */
public class RssSource
{

    private Long id;
    private String name;
    private String uriString;
    private long categoryId;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient RssSourceDao myDao;

    private Category category;
    private Long category__resolvedKey;


    public RssSource()
    {
    }

    public RssSource(Long id)
    {
        this.id = id;
    }

    public RssSource(Long id, String name, String uriString, long categoryId)
    {
        this.id = id;
        this.name = name;
        this.uriString = uriString;
        this.categoryId = categoryId;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession)
    {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRssSourceDao() : null;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUriString()
    {
        return uriString;
    }

    public void setUriString(String uriString)
    {
        this.uriString = uriString;
    }

    public long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(long categoryId)
    {
        this.categoryId = categoryId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public Category getCategory()
    {
        long __key = this.categoryId;
        if (category__resolvedKey == null || !category__resolvedKey.equals(__key))
        {
            if (daoSession == null)
            {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryDao targetDao = daoSession.getCategoryDao();
            Category categoryNew = targetDao.load(__key);
            synchronized (this)
            {
                category = categoryNew;
                category__resolvedKey = __key;
            }
        }
        return category;
    }

    public void setCategory(Category category)
    {
        if (category == null)
        {
            throw new DaoException("To-one property 'categoryId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this)
        {
            this.category = category;
            categoryId = category.getId();
            category__resolvedKey = categoryId;
        }
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete()
    {
        if (myDao == null)
        {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update()
    {
        if (myDao == null)
        {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh()
    {
        if (myDao == null)
        {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

}
