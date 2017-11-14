package ru.gafuk.android.api.rss.base;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.gafuk.android.api.rss.interfaces.IRssMessage;

/**
 * Created by Александр on 22.10.2017.
 */

public abstract class BaseRssMessage implements IRssMessage, Comparable<BaseRssMessage> {

    protected final SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    private String title;
    private String link;
    private String description;
    private Date date;
    private String guid;
    private String category;
    private String comments;
    private String enclosure;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        while (!date.endsWith("00")){
            date += "0";
        }

        try {
            this.date = FORMATTER.parse(date.trim());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getGuid() {
        return guid;
    }

    @Override
    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String getEnclosure() {
        return enclosure;
    }

    @Override
    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    @Override
    public String toString() {
        return "Rss message {title=" + title + ", category=" + category + "}";
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }

    @Override
    public int compareTo(@NonNull BaseRssMessage anotherMessage) {
        return anotherMessage.getDate().compareTo(getDate());
    }
}
