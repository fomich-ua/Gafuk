package ru.gafuk.android.api.contacts.interfaces;

import java.util.Date;

/**
 * Created by Александр on 29.10.2017.
 */

public interface IMessage {

    int getId();

    void setId(int id);

    int getTo_id();

    void setTo_id(int to_id);

    int getFrom_id();

    void setFrom_id(int from_id);

    Date getSenddate();

    void setSenddate(Date sendDate);

    int getIs_new();

    void setIs_new(int is_new);

    String getMessage();

    void setMessage(String message);

}
