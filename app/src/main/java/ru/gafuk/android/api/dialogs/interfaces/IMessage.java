package ru.gafuk.android.api.dialogs.interfaces;

import java.util.Date;

import ru.gafuk.android.api.users.models.UserItem;

/**
 * Created by Александр on 21.11.2017.
 */

public interface IMessage {

    int getId();

    void setId(int id);

    int getTo_id();

    void setTo_id(int to_id);

    int getFrom_id();

    void setFrom_id(int from_id);

    Date getSenddate();

    void setSenddate(Date senddate);

    int getIs_new();

    void setIs_new(int is_new);

    String getMessage();

    void setMessage(String message);

    int getTo_del();

    void setTo_del(int to_del);

    int getFrom_del();

    void setFrom_del(int from_del);

    UserItem getUser();

    void setUser(UserItem user);

}
