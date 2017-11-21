package ru.gafuk.android.api.users.models;

import ru.gafuk.android.api.users.interfaces.IUser;

/**
 * Created by Александр on 27.10.2017.
 */

public class UserItem implements IUser {

    private int id;
    private String url;
    private String avatar;
    private boolean online;
    private String nickname;
    private int reputation;

    public UserItem() {
    }

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

}
