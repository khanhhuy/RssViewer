package assignment.rssviewer.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table ARTICLE.
 */
public class ArticleDao extends AbstractDao<Article, Long>
{

    public static final String TABLENAME = "ARTICLE";
    private DaoSession daoSession;
    ;
    private String selectDeep;


    public ArticleDao(DaoConfig config)
    {
        super(config);
    }

    public ArticleDao(DaoConfig config, DaoSession daoSession)
    {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists)
    {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'ARTICLE' (" + //
                           "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                           "'PUBLISH_DATE' INTEGER," + // 1: publishDate
                           "'TITLE' TEXT," + // 2: title
                           "'IS_READ' INTEGER," + // 3: isRead
                           "'DESCRIPTION' TEXT," + // 4: description
                           "'URI_STRING' TEXT," + // 5: uriString
                           "'RSS_SOURCE_ID' INTEGER NOT NULL );"); // 6: rssSourceId
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists)
    {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ARTICLE'";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, Article entity)
    {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null)
        {
            stmt.bindLong(1, id);
        }

        java.util.Date publishDate = entity.getPublishDate();
        if (publishDate != null)
        {
            stmt.bindLong(2, publishDate.getTime());
        }

        String title = entity.getTitle();
        if (title != null)
        {
            stmt.bindString(3, title);
        }

        Boolean isRead = entity.getIsRead();
        if (isRead != null)
        {
            stmt.bindLong(4, isRead ? 1l : 0l);
        }

        String description = entity.getDescription();
        if (description != null)
        {
            stmt.bindString(5, description);
        }

        String uriString = entity.getUriString();
        if (uriString != null)
        {
            stmt.bindString(6, uriString);
        }
        stmt.bindLong(7, entity.getRssSourceId());
    }

    @Override
    protected void attachEntity(Article entity)
    {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long readKey(Cursor cursor, int offset)
    {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Article readEntity(Cursor cursor, int offset)
    {
        Article entity = new Article( //
                                      cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                                      cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // publishDate
                                      cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
                                      cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // isRead
                                      cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // description
                                      cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // uriString
                                      cursor.getLong(offset + 6) // rssSourceId
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, Article entity, int offset)
    {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPublishDate(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIsRead(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setDescription(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUriString(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRssSourceId(cursor.getLong(offset + 6));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(Article entity, long rowId)
    {
        entity.setId(rowId);
        return rowId;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(Article entity)
    {
        if (entity != null)
        {
            return entity.getId();
        }
        else
        {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable()
    {
        return true;
    }

    protected String getSelectDeep()
    {
        if (selectDeep == null)
        {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRssSourceDao().getAllColumns());
            builder.append(" FROM ARTICLE T");
            builder.append(" LEFT JOIN RSS_SOURCE T0 ON T.'RSS_SOURCE_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }

    protected Article loadCurrentDeep(Cursor cursor, boolean lock)
    {
        Article entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        RssSource rssSource = loadCurrentOther(daoSession.getRssSourceDao(), cursor, offset);
        if (rssSource != null)
        {
            entity.setRssSource(rssSource);
        }

        return entity;
    }

    public Article loadDeep(Long key)
    {
        assertSinglePk();
        if (key == null)
        {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();

        String[] keyArray = new String[]{key.toString()};
        Cursor cursor = db.rawQuery(sql, keyArray);

        try
        {
            boolean available = cursor.moveToFirst();
            if (!available)
            {
                return null;
            }
            else if (!cursor.isLast())
            {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        }
        finally
        {
            cursor.close();
        }
    }

    /**
     * Reads all available rows from the given cursor and returns a list of new ImageTO objects.
     */
    public List<Article> loadAllDeepFromCursor(Cursor cursor)
    {
        int count = cursor.getCount();
        List<Article> list = new ArrayList<Article>(count);

        if (cursor.moveToFirst())
        {
            if (identityScope != null)
            {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try
            {
                do
                {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            }
            finally
            {
                if (identityScope != null)
                {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }

    protected List<Article> loadDeepAllAndCloseCursor(Cursor cursor)
    {
        try
        {
            return loadAllDeepFromCursor(cursor);
        }
        finally
        {
            cursor.close();
        }
    }

    /**
     * A raw-style query where you can pass any WHERE clause and arguments.
     */
    public List<Article> queryDeep(String where, String... selectionArg)
    {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }

    /**
     * Properties of entity Article.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties
    {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PublishDate = new Property(1, java.util.Date.class, "publishDate", false, "PUBLISH_DATE");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property IsRead = new Property(3, Boolean.class, "isRead", false, "IS_READ");
        public final static Property Description = new Property(4, String.class, "description", false, "DESCRIPTION");
        public final static Property UriString = new Property(5, String.class, "uriString", false, "URI_STRING");
        public final static Property RssSourceId = new Property(6, long.class, "rssSourceId", false, "RSS_SOURCE_ID");
    }

}
