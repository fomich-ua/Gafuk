package ru.gafuk.android.api.users.interfaces;

/**
 * Created by Александр on 21.11.2017.
 */

public interface IUser {

    int getId();

    void setId(int id);

    String getUrl();

    void setUrl(String url);

    String getAvatar();

    void setAvatar(String avatar);

    boolean isOnline();

    void setOnline(boolean online);

    String getNickname();

    void setNickname(String nickname);

    int getReputation();

    void setReputation(int reputation);
}
