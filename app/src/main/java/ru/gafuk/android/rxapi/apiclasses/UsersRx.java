package ru.gafuk.android.rxapi.apiclasses;

import java.util.List;

import io.reactivex.Observable;
import ru.gafuk.android.api.Api;
import ru.gafuk.android.api.users.models.SearchUsersResult;
import ru.gafuk.android.api.users.models.UserItem;

/**
 * Created by Александр on 27.10.2017.
 */

public class UsersRx {
    public Observable<List<UserItem>> getUsers(int pageNumber) {
        return Observable.fromCallable(() -> Api.UsersApi().getUsers(pageNumber));
    }

    public Observable<SearchUsersResult> getSearchResult(String nickname){
        return Observable.fromCallable(() -> Api.UsersApi().getSearchResult(nickname));
    }

    public Observable<List<UserItem>> cancelSearch() {
        return Observable.fromCallable(() -> Api.UsersApi().cancelSearch());
    }
}
