package ru.gafuk.android.api.contacts.base;

import java.util.Date;

import ru.gafuk.android.api.contacts.interfaces.IMessage;

/**
 * Created by Александр on 29.10.2017.
 */

public abstract class BaseMessage implements IMessage {
    private int id;

    private int to_id;
    private int from_id;
    private Date senddate;
    private int is_new;
    private String message;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getTo_id() {
        return to_id;
    }

    @Override
    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    @Override
    public int getFrom_id() {
        return from_id;
    }

    @Override
    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    @Override
    public Date getSenddate() {
        return senddate;
    }

    @Override
    public void setSenddate(Date sendDate) {
        this.senddate = sendDate;
    }

    @Override
    public int getIs_new() {
        return is_new;
    }

    @Override
    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
