package ru.gafuk.android.api.users.interfaces;

import java.util.List;

/**
 * Created by Александр on 27.10.2017.
 */

public interface IUsersApi {
    List<? extends IUser>getUsers(int pageNumber) throws Exception;
}
