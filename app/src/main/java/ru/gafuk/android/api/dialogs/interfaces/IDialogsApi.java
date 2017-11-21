package ru.gafuk.android.api.dialogs.interfaces;

import java.util.List;

import ru.gafuk.android.api.dialogs.models.Dialog;

/**
 * Created by aborz on 18.11.2017.
 */

public interface IDialogsApi {

    List<Dialog> getDialogs() throws  Exception;

//    List<? extends IMessageContacts> getContactMessages(int contact_id) throws Exception;
}
