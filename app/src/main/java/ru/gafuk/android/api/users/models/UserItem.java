package ru.gafuk.android.api.users.models;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import ru.gafuk.android.api.users.interfaces.IUser;
import ru.gafuk.android.flowdb.AppDatabase;

/**
 * Created by Александр on 27.10.2017.
 */

@Table(database = AppDatabase.class, name = "users", allFields = true)
public class UserItem implements IUser {
    @PrimaryKey
    private int id;
    private String url;
    private String smallAvatar;
    private String avatar;
    private String nickname;
    private int reputation;

    public UserItem() {
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
    public String getSmallAvatar() {
        return smallAvatar;
    }

    @Override
    public void setSmallAvatar(String smallAvatar) {
        this.smallAvatar = smallAvatar;
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
    public int getReputation() {
        return reputation;
    }

    @Override
    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

}
