package ru.gafuk.android.api.comments.models;

import java.util.Date;

import ru.gafuk.android.api.comments.interfaces.IComment;

/**
 * Created by Александр on 13.11.2017.
 */

public class Comment implements IComment{
    private int id;
    private int level;
    private Date date;
    private int rating;
    private String user_avatar;
    private String user_nickname;
    private String user_url;
    private String text;

    public Comment(int id, int level, Date date, int rating, String user_avatar, String user_nickname, String user_url, String text) {
        this.id = id;
        this.level = level;
        this.date = date;
        this.rating = rating;
        this.user_avatar = user_avatar;
        this.user_nickname = user_nickname;
        this.user_url = user_url;
        this.text = text;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public String getUser_avatar() {
        return user_avatar;
    }

    @Override
    public String getUser_nickname() {
        return user_nickname;
    }

    @Override
    public String getUser_url() {
        return user_url;
    }

    @Override
    public String getText() {
        return text;
    }
}
