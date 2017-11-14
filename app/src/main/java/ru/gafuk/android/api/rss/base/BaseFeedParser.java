package ru.gafuk.android.api.rss.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ru.gafuk.android.api.rss.interfaces.IFeedParser;

/**
 * Created by Александр on 22.10.2017.
 */

public abstract class BaseFeedParser implements IFeedParser {

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

    protected BaseFeedParser(String feedUrl) {
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
}
