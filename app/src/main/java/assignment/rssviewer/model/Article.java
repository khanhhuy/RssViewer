package assignment.rssviewer.model;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * Entity mapped to table ARTICLE.
 */
public class Article
{

    private Long id;
    private java.util.Date publishDate;
    private String title;
    private Boolean isRead;
    private String description;
    private String uriString;
    private long rssSourceId;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient ArticleDao myDao;

    private RssSource rssSource;
    private Long rssSource__resolvedKey;


    public Article()
    {
    }

    public Article(Long id)
    {
        this.id = id;
    }

    public Article(Long id, java.util.Date publishDate, String title, Boolean isRead, String description, String uriString, long rssSourceId)
    {
        this.id = id;
        this.publishDate = publishDate;
        this.title = title;
        this.isRead = isRead;
        this.description = description;
        this.uriString = uriString;
        this.rssSourceId = rssSourceId;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession)
    {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getArticleDao() : null;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public java.util.Date getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(java.util.Date publishDate)
    {
        this.publishDate = publishDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Boolean getIsRead()
    {
        return isRead;
    }

    public void setIsRead(Boolean isRead)
    {
        this.isRead = isRead;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUriString()
    {
        return uriString;
    }

    public void setUriString(String uriString)
    {
        this.uriString = uriString;
    }

    public long getRssSourceId()
    {
        return rssSourceId;
    }

    public void setRssSourceId(long rssSourceId)
    {
        this.rssSourceId = rssSourceId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public RssSource getRssSource()
    {
        long __key = this.rssSourceId;
        if (rssSource__resolvedKey == null || !rssSource__resolvedKey.equals(__key))
        {
            if (daoSession == null)
            {
                throw new DaoException("Entity is detached from DAO context");
            }
            RssSourceDao targetDao = daoSession.getRssSourceDao();
            RssSource rssSourceNew = targetDao.load(__key);
            synchronized (this)
            {
                rssSource = rssSourceNew;
                rssSource__resolvedKey = __key;
            }
        }
        return rssSource;
    }

    public void setRssSource(RssSource rssSource)
    {
        if (rssSource == null)
        {
            throw new DaoException("To-one property 'rssSourceId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this)
        {
            this.rssSource = rssSource;
            rssSourceId = rssSource.getId();
            rssSource__resolvedKey = rssSourceId;
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
