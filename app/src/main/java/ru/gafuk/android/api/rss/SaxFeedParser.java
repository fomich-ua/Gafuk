package ru.gafuk.android.api.rss;

import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ru.gafuk.android.api.rss.interfaces.IFeedParser;
import ru.gafuk.android.api.rss.models.RssMessage;

/**
 * Created by Александр on 22.10.2017.
 */

public class SaxFeedParser implements IFeedParser {

    // names of the XML tags
    static final String TITLE = "title";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";
    static final String LINK = "link";
    static final String DESCRIPTION = "description";
    static final String CATEGORY = "category";
    static final String COMMENTS = "comments";
    static final String ENCLOSURE = "enclosure";

    static final String ITEM = "item";

    final URL feedUrl;

    public SaxFeedParser(String feedUrl) {
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getInputStream() {
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RssMessage> parse() throws Exception{
        RssHandler handler = new RssHandler();
        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return handler.getRssMessages();
    }
}
