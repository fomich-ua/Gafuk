package ru.gafuk.android.api.rss.interfaces;

import java.io.InputStream;
import java.util.List;

import ru.gafuk.android.api.rss.models.RssMessage;

/**
 * Created by Александр on 22.10.2017.
 */

public interface IFeedParser{
    List<RssMessage> parse() throws Exception;

    InputStream getInputStream();
}
