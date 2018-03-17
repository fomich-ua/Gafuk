package ru.gafuk.android.api.auth.interfaces;

/**
 * Created by Александр on 25.10.2017.
 */

public interface IAuthForm {

    public String getNick();

    public void setNick(String nick);

    public String getPassword();

    public void setPassword(String password);

    public String getBody();

    public void setBody(String body);

    public String getCsrf_token();

    public void setCsrf_token(String csrf_token);

    public String getRemember();

    public void setRemember(String remember);

}
