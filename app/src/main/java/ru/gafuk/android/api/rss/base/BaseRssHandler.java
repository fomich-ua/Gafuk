package ru.gafuk.android.api.rss.base;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import ru.gafuk.android.api.rss.models.RssMessage;

/**
 * Created by Александр on 22.10.2017.
 */

public abstract class BaseRssHandler extends DefaultHandler{
    private List<RssMessage> rssMessages;
    private RssMessage currentRssMessage;
    private StringBuilder builder;

    public List<RssMessage> getRssMessages() {
        return this.rssMessages;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        super.endElement(uri, localName, name);
        if (this.currentRssMessage != null) {

            if (localName.equalsIgnoreCase(BaseFeedParser.TITLE)) {
                currentRssMessage.setTitle(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(BaseFeedParser.LINK)) {
                currentRssMessage.setLink(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(BaseFeedParser.DESCRIPTION)) {
                currentRssMessage.setDescription(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(BaseFeedParser.PUB_DATE)) {
                currentRssMessage.setDate(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(BaseFeedParser.GUID)) {
                currentRssMessage.setGuid(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(BaseFeedParser.CATEGORY)) {
                currentRssMessage.setCategory(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(BaseFeedParser.COMMENTS)) {
                currentRssMessage.setComments(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(BaseFeedParser.ENCLOSURE)) {
                currentRssMessage.setEnclosure(builder.toString().trim());
            } else if (localName.equalsIgnoreCase(BaseFeedParser.ITEM)) {
                rssMessages.add(currentRssMessage);
            }

            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        rssMessages = new ArrayList<>();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equalsIgnoreCase(BaseFeedParser.ITEM)) {
            this.currentRssMessage = new RssMessage();
            builder.setLength(0);
        }else if (localName.equalsIgnoreCase("enclosure")){
            builder.append(attributes.getValue("url"));
        }
    }
}
