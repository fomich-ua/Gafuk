package ru.gafuk.android.api.users.models;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by Александр on 27.10.2017.
 */

public class UserItem implements IUser {
    private String id;
    private String url;
    private String avatar;
    private String nickname;
    private int reputation;

    public UserItem() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    @Override
    public String getName() {
        return getNickname();
    }
}
