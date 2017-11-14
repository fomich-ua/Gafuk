package ru.gafuk.android.rxapi.apiclasses;

import io.reactivex.Observable;
import ru.gafuk.android.api.Api;
import ru.gafuk.android.api.auth.interfaces.IAuthForm;

/**
 * Created by Александр on 25.10.2017.
 */

public class AuthRx {
    public Observable<IAuthForm> getForm() {
        return Observable.fromCallable(() -> Api.AuthApi().getAuthForm());
    }

    public Observable<Boolean> login(final IAuthForm authForm) {
        return Observable.fromCallable(() -> Api.AuthApi().login(authForm));
    }

    public Observable<Boolean> logout() {
        return Observable.fromCallable(() -> Api.AuthApi().logout());
    }
}
