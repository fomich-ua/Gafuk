package ru.gafuk.android.api;

import ru.gafuk.android.api.auth.AuthApi;
import ru.gafuk.android.api.blogs.BlogsApi;
import ru.gafuk.android.api.contacts.ContactsApi;
import ru.gafuk.android.api.dialogs.DialogsApi;
import ru.gafuk.android.api.news.NewsApi;
import ru.gafuk.android.api.users.UsersApi;

/**
 * Created by Александр on 25.10.2017.
 */

public class Api {
    private static Api INSTANCE = null;
    private static NewsApi newsApi = null;
    private static BlogsApi blogsApi = null;
    private static ContactsApi contactsApi = null;
    private static DialogsApi dialogsApi = null;
    private static AuthApi authApi = null;
    private static UsersApi usersApi = null;

    public static Api getInstance() {
        if (INSTANCE == null) INSTANCE = new Api();
        return INSTANCE;
    }

    public static NewsApi NewsApi() {
        if (newsApi == null) newsApi = new NewsApi();
        return newsApi;
    }

    public static BlogsApi BlogsApi() {
        if (blogsApi == null) blogsApi = new BlogsApi();
        return blogsApi;
    }

    public static ContactsApi ContactsApi() {
        if (contactsApi == null) contactsApi = new ContactsApi();
        return contactsApi;
    }

    public static DialogsApi DialogsApi() {
        if (dialogsApi == null) dialogsApi = new DialogsApi();
        return dialogsApi;
    }

    public static AuthApi AuthApi(){
        if (authApi == null) authApi = new AuthApi();
        return authApi;
    }

    public static UsersApi UsersApi(){
        if ((usersApi == null)) usersApi = new UsersApi();
        return usersApi;
    }
}
