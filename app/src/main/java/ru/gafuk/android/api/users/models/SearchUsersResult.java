package ru.gafuk.android.api.users.models;

import java.util.List;

import ru.gafuk.android.api.users.interfaces.ISearchUsersResult;

/**
 * Created by Александр on 01.11.2017.
 */

public class SearchUsersResult implements ISearchUsersResult {

    private final int totalPages;
    private final List<UserItem> usersList;

    public SearchUsersResult() {
        super();
        totalPages = -1;
        usersList = null;
    }

    public SearchUsersResult(List<UserItem> usersList, int totalPages) {
        this.usersList = usersList;
        this.totalPages = totalPages;
    }

    @Override
    public List<UserItem> getUsers() {
        return usersList;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }
}
