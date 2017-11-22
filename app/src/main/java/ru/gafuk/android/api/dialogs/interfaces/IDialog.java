package ru.gafuk.android.api.dialogs.interfaces;

import java.util.List;

import ru.gafuk.android.api.users.models.UserItem;

/**
 * Created by Александр on 21.11.2017.
 */

public interface IDialog<MESSAGE extends IMessage> {

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

    int getNew_messages();

    public void setNew_messages(int new_messages);

    MESSAGE getLastMessage();

    void setLastMessage(MESSAGE message);

    List<UserItem> getUsers();

    void setUsers(List<UserItem> users);
}
