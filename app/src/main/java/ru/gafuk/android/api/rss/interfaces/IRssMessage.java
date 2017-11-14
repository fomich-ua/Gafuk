package ru.gafuk.android.api.rss.interfaces;

import java.util.Date;

/**
 * Created by Александр on 22.10.2017.
 */

public interface IRssMessage {

    String getTitle();

    void setTitle(String title);

    String getLink();

    void setLink(String link);

    String getDescription();

    void setDescription(String description);

    Date getDate();

    void setDate(String date);

    String getGuid();

    void setGuid(String guid);

    String getCategory();

    void setCategory(String category);

    String getComments();

    void setComments(String comments);

    String getEnclosure();

    void setEnclosure(String enclosure);

}
