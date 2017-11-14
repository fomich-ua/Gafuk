package ru.gafuk.android.client;

/**
 * Created by Александр on 25.10.2017.
 */

public class NetworkResponse {
    private int code = 0;
    private String message = "";
    private String url = "";
    private String redirect = url;
    private String body = "";

    public NetworkResponse(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
