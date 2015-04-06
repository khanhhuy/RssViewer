package assignment.rssviewer.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class GreenDAOGenerator
{
    public static void main(String[] args) throws Exception
    {
        Schema schema = new Schema(1, "assignment.rssviewer.model");
        schema.setDefaultJavaPackageTest("assignment.rssviewer.model.test");
        schema.setDefaultJavaPackageDao("assignment.rssviewer.model");

        Entity rssSource = schema.addEntity("RssSource");
        rssSource.addIdProperty().autoincrement();
        rssSource.addStringProperty("name");
        rssSource.addStringProperty("uriString");

        Entity category = schema.addEntity("Category");
        category.addIdProperty();
        category.addStringProperty("name");

        Entity article = schema.addEntity("Article");
        article.addIdProperty().autoincrement();
        article.addDateProperty("publishDate");
        article.addStringProperty("title");
        article.addBooleanProperty("isRead");
        article.addStringProperty("description");
        article.addStringProperty("uriString");

        // 1 rssSource -> 1 category
        Property categoryIdProp = rssSource.addLongProperty("categoryId").notNull().getProperty();
        rssSource.addToOne(category, categoryIdProp);

        // 1 category -> n rssSource
        ToMany categoryToRssSources = category.addToMany(rssSource, categoryIdProp);
        categoryToRssSources.setName("rssSources");

        // 1 article -> 1 rssSource
        Property rssSourceIdProp = article.addLongProperty("rssSourceId").notNull().getProperty();
        article.addToOne(rssSource, rssSourceIdProp);

        // generates classes
        DaoGenerator daoGenerator = new DaoGenerator();
        daoGenerator.generateAll(schema, "app/src/main/java");
    }
}
