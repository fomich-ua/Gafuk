package ru.gafuk.android.api.dialogs.models;

import java.util.Date;

import ru.gafuk.android.api.dialogs.interfaces.IMessage;
import ru.gafuk.android.api.users.models.UserItem;

/**
 * Created by aborz on 18.11.2017.
 */

public class Message implements IMessage {

    private int id;

    private int to_id;
    private int from_id;
    private Date senddate;
    private int is_new;
    private String message;
    private int to_del;
    private int from_del;

    private UserItem user;

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTo_id() {
        return to_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public Date getSenddate() {
        return senddate;
    }

    public void setSenddate(Date senddate) {
        this.senddate = senddate;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTo_del() {
        return to_del;
    }

    public void setTo_del(int to_del) {
        this.to_del = to_del;
    }

    public int getFrom_del() {
        return from_del;
    }

    public void setFrom_del(int from_del) {
        this.from_del = from_del;
    }
}
