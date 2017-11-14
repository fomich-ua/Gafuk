package ru.gafuk.android.api.blogs.models;

import java.util.Date;

import ru.gafuk.android.api.blogs.interfaces.IBlogItem;

/**
 * Created by Александр on 23.10.2017.
 */

public class BlogItem implements IBlogItem {

    private int id = -1;
    private String url = "";
    private String title = "";
    private String description = "";
    private String author = "";
    private String authorRef = "";
    private Date date = new Date(0);
    private String imgUrl = "";
    private int commentsCount = 0;
    private int hits = 0;
    private int rating = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getAuthorRef() {
        return authorRef;
    }

    public void setAuthorRef(String authorRef) {
        this.authorRef = authorRef;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
