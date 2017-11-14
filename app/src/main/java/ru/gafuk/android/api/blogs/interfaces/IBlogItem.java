package ru.gafuk.android.api.blogs.interfaces;

import java.util.Date;

/**
 * Created by Александр on 23.10.2017.
 */

public interface IBlogItem {
    public int getId();

    public void setId(int id);

    public String getUrl();

    public void setUrl(String url);

    public String getTitle();

    public void setTitle(String title);

    public String getDescription();

    public void setDescription(String description);

    public String getAuthor();

    public void setAuthor(String author);

    public Date getDate();

    public void setDate(Date date);

    public String getImgUrl();

    public void setImgUrl(String imgUrl);

    public int getCommentsCount();

    public void setCommentsCount(int commentsCount);

    public String getAuthorRef();

    public void setAuthorRef(String authorRef);
}
