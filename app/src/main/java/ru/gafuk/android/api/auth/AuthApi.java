package ru.gafuk.android.api.auth;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ru.gafuk.android.Constant;
import ru.gafuk.android.api.auth.interfaces.IAuthApi;
import ru.gafuk.android.api.auth.interfaces.IAuthForm;
import ru.gafuk.android.api.auth.models.AuthForm;
import ru.gafuk.android.client.Client;
import ru.gafuk.android.client.NetworkResponse;

/**
 * Created by Александр on 25.10.2017.
 */

public class AuthApi implements IAuthApi{
    private static final String LOG_TAG = Constant.GAFUK_LOG_PREFIX + AuthApi.class.getSimpleName();

    private final Pattern csrfPattern = Pattern.compile("name=\"csrf_token\" value=\"([^\"]*?)\"");

    @Override
    public AuthForm getAuthForm() throws Exception {

        NetworkResponse response = Client.get(Constant.GAFUK_LOGIN_STRING);

        if (response.getBody() == null || response.getBody().isEmpty())
            throw new Exception("Page empty!");

//        if (checkLogin(null, response.getBody()))
//            throw new Exception("You already logged");

        AuthForm form = new AuthForm();

        form.setBody(response.getBody());

        Matcher matcher = csrfPattern.matcher(response.getBody());
        if (matcher.find()){
            form.setCsrf_token(matcher.group(1));
        }else {
            throw new Exception("CSRF token not found");
        }
        Log.d(LOG_TAG, "getAuthForm: succesfull");
        return form;

    }

    @Override
    public boolean login(IAuthForm authForm) throws Exception {

        NetworkResponse response;

        // попытка входа с помощью cookie
        response = Client.get(Constant.GAFUK_LOGIN_STRING);
        if (response.getRedirect().equalsIgnoreCase(Constant.GAFUK_URL)){
            Log.d(LOG_TAG, "login: cookie login");
            return true;
        }

        // попытка входа с помощью логина
        if (authForm.getCsrf_token().isEmpty()){
            authForm.setBody(response.getBody());

            Matcher matcher = csrfPattern.matcher(authForm.getBody());
            if (matcher.find()){
                authForm.setCsrf_token(matcher.group(1));
            }else {
                throw new Exception("CSRF token not found");
            }

//            Client.clearCookies();
//            authForm.setCsrf_token(getAuthForm().getCsrf_token());
        };

        RequestBody formBody = new FormBody.Builder()
                .add("csrf_token", authForm.getCsrf_token())
                .add("login", authForm.getNick())
                .add("pass", authForm.getPassword())
                .add("remember", authForm.getRemember())
                .add("login_btn", "Войти")
                .build();

        Request request = new Request.Builder()
                .url(Constant.GAFUK_LOGIN_STRING)
                .post(formBody)
                .build();

        response = Client.request(request);

        authForm.setCsrf_token("");

        String redirectUrl = response.getRedirect();

        switch (redirectUrl){
            case Constant.GAFUK_URL:
                if (authForm.getRemember().equals("1")) Client.saveAuthCookie();
                return true;
            case Constant.GAFUK_AUTH_ERROR:
                Client.clearCookies();
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean logout() throws Exception {
        return false;
    }
}
