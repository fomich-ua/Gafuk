package ru.gafuk.android.api.rss;

import android.util.Xml;

import java.util.List;

import ru.gafuk.android.api.rss.base.BaseFeedParser;
import ru.gafuk.android.api.rss.models.RssMessage;

/**
 * Created by Александр on 22.10.2017.
 */

public class SaxFeedParser extends BaseFeedParser {

    public SaxFeedParser(String feedUrl) {
        super(feedUrl);
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
