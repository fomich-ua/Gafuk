package ru.gafuk.android.api.contacts.models;

import ru.gafuk.android.api.contacts.interfaces.IContact;

/**
 * Created by Александр on 25.10.2017.
 */

public class ContactItem  implements IContact {
    public ContactItem() {
    }

    private int id;
    int count, online;
    private String  url;
    private String avatar;
    private String nickname;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public int getOnline() {
        return online;
    }

    @Override
    public void setOnline(int online) {
        this.online = online;
    }
}
