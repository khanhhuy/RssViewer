package assignment.rssviewer.service;

import android.util.Log;

import org.apache.http.ParseException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import assignment.rssviewer.model.Article;
import assignment.rssviewer.model.RssSource;
import assignment.rssviewer.utils.RssTag;

/**
 * Created by Huy on 4/14/2015.
 */
public class RssParser implements IRssService
{
    public RssParser()
    {
    }

    @Override
    public RssSource parse(String url)
    {
        InputStream is;
        RssSource rssSource = null;
        XMLTag currentTag = XMLTag.IGNORETAG;
        boolean isParsed = false;

        try
        {
            is = openConnection(url);

            if (is == null)
            {
                throw new IOException("Input Stream is null");
            }

            //parse XML code
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(is, null); //input and encoding

            int eventType = xpp.getEventType();
            Log.d("debug", "Start Parsing");

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if (eventType == XmlPullParser.START_DOCUMENT)
                {
                }
                else if (eventType == XmlPullParser.START_TAG)
                {
                    String tagName = xpp.getName();

                    switch (tagName)
                    {
                        case RssTag.RSS_CHANNEL:
                        case RssTag.ATOM_FEED:
                            rssSource = new RssSource();
                            rssSource.setUrlString(url);
                            currentTag = XMLTag.CHANNEL;
                            break;
                        case RssTag.RSSATOM_TITLE:
                            currentTag = XMLTag.TITLE;
                            break;
                        default:
                            break;
                    }
                }
                else if (eventType == XmlPullParser.TEXT)
                {
                    String content = xpp.getText();
                    content = content.trim();
                    Log.d("debug", content);
                    if (rssSource != null)
                    {
                        switch (currentTag)
                        {
                            case TITLE:
                                if (content.length() != 0)
                                {
                                    rssSource.setName(content);
                                    isParsed = true;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                eventType = xpp.next();
                if (isParsed)
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        Log.d("debug", "Parsing RSS Source done");

        return rssSource;
    }

    //@Override
    public List<Article> parseArticles(List<RssSource> listRssSource)
    {
        InputStream is;
        XMLTag currentTag = XMLTag.IGNORETAG;
        List<Article> articleList = new ArrayList<Article>();
        boolean isAtomEntry = false;

        for (RssSource source : listRssSource)
        {
            Article article = null;
            try
            {
                is = openConnection(source.getUrlString());

                if (is == null)
                {
                    throw new IOException("Input Stream is null");
                }

                //parse XML code
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, null); //input and encoding

                int eventType = xpp.getEventType();
                Log.d("debug", "Start Parsing");

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if (eventType == XmlPullParser.START_DOCUMENT)
                    {
                    }
                    else if (eventType == XmlPullParser.START_TAG)
                    {
                        String tagName = xpp.getName();

                        switch (tagName)
                        {
                            case RssTag.ATOM_ENTRY:
                                article = new Article();
                                isAtomEntry = true;
                                //Todo
                                // article.setSourceId here
                                currentTag = XMLTag.ITEM;
                                break;
                            case RssTag.RSS_ITEM:
                                article = new Article();
                                //Todo
                                // article.setSourceId here
                                currentTag = XMLTag.ITEM;
                                break;
                            case RssTag.RSSATOM_TITLE:
                                currentTag = XMLTag.TITLE;
                                break;
                            case RssTag.RSSATOM_LINK:
                                currentTag = XMLTag.LINK;
                                if ((xpp.getAttributeValue(null, "href") != null) && isAtomEntry)
                                {
                                    article.setUrlString(xpp.getAttributeValue(null, "href"));
                                }
                                break;
                            case RssTag.RSS_DESC:
                            case RssTag.ATOM_SUMMARY:
                            case RssTag.ATOM_CONTENT:
                                currentTag = XMLTag.DESCRIPTION;
                                break;
                            case RssTag.RSS_PUB_DATE:
                            case RssTag.ATOM_PUBLISHED:
                                currentTag = XMLTag.PUBDATE;
                                break;
                            default:
                                break;
                        }
                    }
                    else if (eventType == XmlPullParser.END_TAG)
                    {
                        if (xpp.getName().equals(RssTag.RSS_ITEM) || xpp.getName().equals(RssTag.ATOM_ENTRY))
                        {
                            articleList.add(article);
                            isAtomEntry = false;
                        }
                        else
                            currentTag = XMLTag.IGNORETAG;
                    }
                    else if (eventType == XmlPullParser.TEXT)
                    {
                        String content = xpp.getText();
                        content = content.trim();
                        Log.d("Content", content);
                        if (article != null)
                        {
                            switch (currentTag)
                            {
                                case TITLE:
                                    if (content.length() != 0)
                                    {
                                        article.setTitle(content);
                                    }
                                    break;
                                case LINK:
                                    if (content.length() != 0)
                                    {
                                        article.setUrlString(content);
                                    }
                                    break;
                                case DESCRIPTION:
                                    if (content.length() != 0)
                                    {
                                        article.setDescription(content);
                                    }
                                case PUBDATE:
                                    //Todo
                                    //Parse date
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    eventType = xpp.next();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (XmlPullParserException e)
            {
                e.printStackTrace();
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        Log.d("debug", "Parsing RSS Articles done");

        return articleList;
    }

    private InputStream openConnection(String stringUrl)
    {

        InputStream is = null;
        try
        {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int response = connection.getResponseCode();
            Log.d("debug", "The response is: " + response);
            is = connection.getInputStream();
            //connection.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return is;
    }

    public enum XMLTag
    {
        CHANNEL, TITLE, LINK, ITEM, PUBDATE, DESCRIPTION, CONTENT, IGNORETAG
    }
}
