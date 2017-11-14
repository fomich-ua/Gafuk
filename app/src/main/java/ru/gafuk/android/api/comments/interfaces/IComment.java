package ru.gafuk.android.api.comments.interfaces;

import java.util.Date;

/**
 * Created by Александр on 14.11.2017.
 */

public interface IComment {

    int getId();

    int getLevel();

    Date getDate();

    int getRating();

    String getUser_avatar();

    String getUser_nickname();

    String getUser_url();

    String getText();

}
