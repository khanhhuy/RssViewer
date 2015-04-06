package assignment.rssviewer.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession
{

    private final DaoConfig rssSourceDaoConfig;
    private final DaoConfig categoryDaoConfig;
    private final DaoConfig articleDaoConfig;

    private final RssSourceDao rssSourceDao;
    private final CategoryDao categoryDao;
    private final ArticleDao articleDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap)
    {
        super(db);

        rssSourceDaoConfig = daoConfigMap.get(RssSourceDao.class).clone();
        rssSourceDaoConfig.initIdentityScope(type);

        categoryDaoConfig = daoConfigMap.get(CategoryDao.class).clone();
        categoryDaoConfig.initIdentityScope(type);

        articleDaoConfig = daoConfigMap.get(ArticleDao.class).clone();
        articleDaoConfig.initIdentityScope(type);

        rssSourceDao = new RssSourceDao(rssSourceDaoConfig, this);
        categoryDao = new CategoryDao(categoryDaoConfig, this);
        articleDao = new ArticleDao(articleDaoConfig, this);

        registerDao(RssSource.class, rssSourceDao);
        registerDao(Category.class, categoryDao);
        registerDao(Article.class, articleDao);
    }

    public void clear()
    {
        rssSourceDaoConfig.getIdentityScope().clear();
        categoryDaoConfig.getIdentityScope().clear();
        articleDaoConfig.getIdentityScope().clear();
    }

    public RssSourceDao getRssSourceDao()
    {
        return rssSourceDao;
    }

    public CategoryDao getCategoryDao()
    {
        return categoryDao;
    }

    public ArticleDao getArticleDao()
    {
        return articleDao;
    }

}
