package ru.gafuk.android.api.auth.interfaces;

import ru.gafuk.android.api.auth.models.AuthForm;

/**
 * Created by Александр on 25.10.2017.
 */

public interface IAuthApi {
    AuthForm getAuthForm() throws Exception;
    boolean login(final IAuthForm authForm) throws Exception;
    boolean logout() throws Exception;
}
