package ru.gafuk.android.api.dialogs.models;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

import ru.gafuk.android.api.users.models.UserItem;

/**
 * Created by aborz on 18.11.2017.
 */

public class Message implements IMessage {

    private String id;

    private int to_id;
    private int from_id;
    private Date createdAt;
    private int is_new;
    private String text;
    private UserItem user;

    public Message(String id, int to_id, int from_id, UserItem user, String text, Date createdAt) {
        this.id = id;
        this.to_id = to_id;
        this.from_id = from_id;

        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public UserItem getUser() {
        return user;
    }
}
