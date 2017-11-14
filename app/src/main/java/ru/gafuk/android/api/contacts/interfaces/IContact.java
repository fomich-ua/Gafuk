package ru.gafuk.android.api.contacts.interfaces;

/**
 * Created by Александр on 24.09.2017.
 */

public interface IContact {

    int getId();

    void setId(int id);

    String getUrl();

    void setUrl(String url);

    void setAvatar(String avatar);

    String getAvatar();

    void setNickname(String nick);

    String getNickname();

    void setCount(int count);

    int getCount();

    int getOnline();

    void setOnline(int online);

}
