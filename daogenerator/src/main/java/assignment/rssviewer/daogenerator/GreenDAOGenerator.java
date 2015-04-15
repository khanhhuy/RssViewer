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
        Schema schema = new Schema(2, "assignment.rssviewer.model");
        schema.setDefaultJavaPackageTest("assignment.rssviewer.model.test");
        schema.setDefaultJavaPackageDao("assignment.rssviewer.model");
        //schema.enableKeepSectionsByDefault();

        Entity rssSource = schema.addEntity("RssSource");
        rssSource.addIdProperty().autoincrement();
        rssSource.addStringProperty("name");
        rssSource.addStringProperty("urlString");

        Entity category = schema.addEntity("Category");
        category.addIdProperty().autoincrement();
        category.addStringProperty("name");

        Entity article = schema.addEntity("Article");
        article.addIdProperty().autoincrement();
        article.addDateProperty("publishDate");
        article.addStringProperty("title");
        article.addBooleanProperty("isRead");
        article.addStringProperty("description");
        article.addStringProperty("urlString");
        article.addStringProperty("imageUrl"); // <-- version 2

        // 1 rssSource -> 1 category
        Property categoryIdProp = rssSource.addLongProperty("categoryId").notNull().getProperty();
        rssSource.addToOne(category, categoryIdProp);

        // 1 category -> n rssSource
        ToMany categoryToRssSources = category.addToMany(rssSource, categoryIdProp);
        categoryToRssSources.setName("rssSources");

        // 1 article -> 1 rssSource
        Property rssSourceIdProp = article.addLongProperty("rssSourceId").notNull().getProperty();
        article.addToOne(rssSource, rssSourceIdProp);

        // suggestion <-- version 2
        Entity suggestionCategory = schema.addEntity("SuggestionCategory");
        suggestionCategory.addIdProperty().autoincrement();
        suggestionCategory.addStringProperty("name");

        Entity suggestionSource = schema.addEntity("SuggestionSource");
        suggestionSource.addIdProperty().autoincrement();
        suggestionSource.addStringProperty("name");
        suggestionSource.addStringProperty("urlString");

        Property suggCateIdProp = suggestionSource.addLongProperty("categoryId").notNull().getProperty();
        suggestionSource.addToOne(suggestionCategory, suggCateIdProp);

        ToMany suggCateToSuggSource = suggestionCategory.addToMany(suggestionSource, suggCateIdProp);
        suggCateToSuggSource.setName("sources");

        // generates classes
        DaoGenerator daoGenerator = new DaoGenerator();
        daoGenerator.generateAll(schema, "app/src/main/java");
    }
}
