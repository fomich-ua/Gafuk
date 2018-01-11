package ru.gafuk.android.rxapi;

import ru.gafuk.android.rxapi.apiclasses.AuthRx;
import ru.gafuk.android.rxapi.apiclasses.BlogsRx;
import ru.gafuk.android.rxapi.apiclasses.CommentsRx;
import ru.gafuk.android.rxapi.apiclasses.ContactsRx;
import ru.gafuk.android.rxapi.apiclasses.NewsRx;
import ru.gafuk.android.rxapi.apiclasses.UsersRx;

/**
 * Created by Александр on 24.10.2017.
 */

public class RxApi {

    private static RxApi INSTANCE = null;
    private static NewsRx newsList = null;
    private static BlogsRx blogsList = null;
    private static ContactsRx contactsList = null;
    private static AuthRx authRx = null;
    private static UsersRx usersRx = null;
    private static CommentsRx commentsRx = null;

    public static RxApi getInstance() {
        if (INSTANCE == null) INSTANCE = new RxApi();
        return INSTANCE;
    }

    public static NewsRx NewsList(){
        if (newsList == null) newsList = new NewsRx();
        return newsList;
    }

    public static BlogsRx BlogsList(){
        if (blogsList == null) blogsList = new BlogsRx();
        return blogsList;
    }

    public static ContactsRx ContactsList(){
        if (contactsList == null) contactsList = new ContactsRx();
        return contactsList;
    }

    public static AuthRx Auth(){
        if (authRx == null) authRx = new AuthRx();
        return authRx;
    }

    public static UsersRx UsersList(){
        if (usersRx == null) usersRx = new UsersRx();
        return usersRx;
    }

    public static CommentsRx Comments(){
        if (commentsRx == null) commentsRx = new CommentsRx();
        return commentsRx;
    }
}
