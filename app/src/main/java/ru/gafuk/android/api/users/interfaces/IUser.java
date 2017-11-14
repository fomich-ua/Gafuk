package ru.gafuk.android.api.users.interfaces;

/**
 * Created by Александр on 27.10.2017.
 */

public interface IUser {

    String getUrl();

    void setUrl(String url);

    String getSmallAvatar();

    void setSmallAvatar(String smallAvatar);

    String getAvatar();

    void setAvatar(String avatar);

    String getNickname();

    void setNickname(String nickname);

    int getReputation();

    void setReputation(int reputation);

}
