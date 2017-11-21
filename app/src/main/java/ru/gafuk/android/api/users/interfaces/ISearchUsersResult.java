package ru.gafuk.android.api.users.interfaces;

import java.util.List;

/**
 * Created by Александр on 01.11.2017.
 */

public interface ISearchUsersResult {
    List<? extends IUser> getUsers();
    int getTotalPages();
}
