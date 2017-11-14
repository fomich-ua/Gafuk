package ru.gafuk.android.api.contacts.base;

import java.util.List;

import ru.gafuk.android.api.contacts.interfaces.IContact;
import ru.gafuk.android.api.contacts.interfaces.IContactsApi;
import ru.gafuk.android.api.contacts.interfaces.IMessage;

/**
 * Created by Александр on 25.10.2017.
 */

public abstract class BaseContactApi implements IContactsApi {

    @Override
    public abstract List<? extends IContact> getContacts() throws Exception;

    @Override
    public abstract List<? extends IMessage> getContactMessages(int contact_id) throws Exception;
}
