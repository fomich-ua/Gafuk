package ru.gafuk.android.api.news.interfaces;

import java.util.Date;

/**
 * Created by Александр on 23.10.2017.
 */

public interface INewsItem {
    int getId();

    void setId(int id);

    String getUrl();

    void setUrl(String url);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    String getAuthor();

    void setAuthor(String author);

    Date getDate();

    void setDate(Date date);

    String getImgUrl();

    void setImgUrl(String imgUrl);

    int getCommentsCount();

    void setCommentsCount(int commentsCount);

    String getAuthorRef();

    void setAuthorRef(String authorRef);
}
