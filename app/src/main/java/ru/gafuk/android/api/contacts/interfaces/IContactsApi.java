package ru.gafuk.android.api.contacts.interfaces;

import java.util.List;

/**
 * Created by Александр on 25.10.2017.
 */

public interface IContactsApi {
    List<? extends IContact> getContacts() throws Exception;

    List<? extends IMessageContacts> getContactMessages(int contact_id) throws Exception;
}
