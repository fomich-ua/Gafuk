package ru.gafuk.android.rxapi.apiclasses;

import java.util.List;

import io.reactivex.Observable;
import ru.gafuk.android.api.Api;
import ru.gafuk.android.api.contacts.models.ContactItem;
import ru.gafuk.android.api.contacts.models.MessageContacts;

/**
 * Created by Александр on 25.10.2017.
 */

public class ContactsRx {

    public Observable<List<ContactItem>> getContacts() {
        return Observable.fromCallable(() -> Api.ContactsApi().getContacts());
    }

    public Observable<List<MessageContacts>> getContactMessages(int contact_id){
        return Observable.fromCallable(() -> Api.ContactsApi().getContactMessages(contact_id));
    }
}
