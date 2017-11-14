package ru.gafuk.android.api.auth.models;

import ru.gafuk.android.api.auth.interfaces.IAuthForm;

/**
 * Created by Александр on 25.10.2017.
 */

public class AuthForm implements IAuthForm {
    private String nick = "";
    private String password = "";
    private String body = "";
    private String csrf_token = "";
    private String remember = "";

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCsrf_token() {
        return csrf_token;
    }

    public void setCsrf_token(String csrf_token) {
        this.csrf_token = csrf_token;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }

    @Override
    public String toString() {
        return "AuthForm{nick=" + getNick() + ", csrf=" + getCsrf_token() + "}";
    }
}
