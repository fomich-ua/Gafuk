package ru.gafuk.android.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.huc.OkHttpURLConnection;
import ru.gafuk.android.App;
import ru.gafuk.android.Constant;
import ru.gafuk.android.utils.SimpleObservable;

/**
 * Created by Александр on 25.10.2017.
 */

public class Client {
    private final static String LOG_TAG = Constant.GAFUK_LOG_PREFIX + Client.class.getSimpleName();

    private static Client INSTANCE = null;
    private Map<String, Cookie> cookies = new HashMap<>();
    private final int cacheSize = 1024 * 104 * 10;

    private static SimpleObservable networkObservables = new SimpleObservable();
    private static SimpleObservable loginStateObservables = new SimpleObservable();

    private final CookieJar cookieJar = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (url.host().contains("gafuk")){
                for (Cookie cookie:cookies){
                    getINSTANCE().cookies.put(cookie.name(), cookie);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            if (!url.host().toLowerCase().contains("gafuk")) {
                return new ArrayList<>();
            }
            return new ArrayList<>(getINSTANCE().cookies.values());
        }
    };

    private final OkHttpClient client = new OkHttpClient.Builder()
            .followRedirects(false)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(new Cache(new File(App.getContext().getCacheDir().getAbsolutePath(), "okttp"), cacheSize))
            .cookieJar(cookieJar)
            .build();

    private Client(){
        String user_id = App.getInstance().getPreferences().getString("cookie_userid", null);
        Cookie parsedCookie;

        if (user_id != null) {
            parsedCookie = parseCookie(user_id);
            cookies.put(parsedCookie.name(), parsedCookie);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    private static Client getINSTANCE(){
        if (INSTANCE == null) INSTANCE = new Client();
        return INSTANCE;
    }

    public static NetworkResponse get(String url) throws Exception{

//        NetworkResponse networkResponse = new NetworkResponse(url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        return request(request);

//        try (Response okHttpResponse = getINSTANCE().client.newCall(request).execute()){
//
//            Log.d(LOG_TAG, "get: url=" + url + ", message=" + okHttpResponse.message());
//
//            if (okHttpResponse.isSuccessful()){
//                networkResponse.setCode(okHttpResponse.code());
//                networkResponse.setMessage(okHttpResponse.message());
//                networkResponse.setRedirect(okHttpResponse.request().url().toString());
//                networkResponse.setBody(okHttpResponse.body().string());
//            }
//        }

//        return networkResponse;
    }

    public static NetworkResponse request(Request request) throws Exception{
        NetworkResponse networkResponse = new NetworkResponse(request.url().toString());

        try (Response okHttpResponse = getINSTANCE().client.newCall(request).execute()){
            Log.d(LOG_TAG, "get: url=" + request.url().toString() + ", code=" + okHttpResponse.code() + ", message=" + okHttpResponse.message());

            if (okHttpResponse.isSuccessful()){
                networkResponse.setCode(okHttpResponse.code());
                networkResponse.setMessage(okHttpResponse.message());
                networkResponse.setRedirect(okHttpResponse.request().url().toString());
                networkResponse.setBody(okHttpResponse.body().string());
            }else if (okHttpResponse.isRedirect()){
                switch (okHttpResponse.header("Location")){
                    case "/":{
                        networkResponse.setCode(OkHttpURLConnection.HTTP_OK);
                        networkResponse.setMessage(okHttpResponse.message());
                        networkResponse.setRedirect(Constant.GAFUK_URL);
                        networkResponse.setBody(okHttpResponse.body().string());
                        break;
                    }case "/login": {
                        networkResponse.setCode(OkHttpURLConnection.HTTP_OK);
                        networkResponse.setMessage(okHttpResponse.message());
                        networkResponse.setRedirect(Constant.GAFUK_LOGIN_STRING);
                        networkResponse.setBody(okHttpResponse.body().string());
                        break;
                    }case "/auth/error.html":{
                        networkResponse.setCode(OkHttpURLConnection.HTTP_OK);
                        networkResponse.setMessage(okHttpResponse.message());
                        networkResponse.setRedirect(Constant.GAFUK_AUTH_ERROR);
                        break;
                    }
                }
            }
        }
        return networkResponse;
    }

    public static Map<String, Cookie> getCookies() {
        return getINSTANCE().cookies;
    }

    public static void clearCookies() {
        Log.d(LOG_TAG, "clearCookies: ");
        getINSTANCE().cookies.clear();
    }

    public static void saveAuthCookie(){
        for (Map.Entry<String, Cookie > keyValue: getINSTANCE().cookies.entrySet()){
            if (keyValue.getKey().contains("[userid]")){
                App.getInstance()
                        .getPreferences()
                        .edit()
                        .putString("cookie_userid", getINSTANCE().cookieToPref(Constant.GAFUK_URL, keyValue.getValue()))
                        .apply();
                break;
            }
        }
    }
    private Cookie parseCookie(String cookieFields) {
        /*Хранение: Url|:|Cookie*/
        String[] fields = cookieFields.split("\\|:\\|");
        return Cookie.parse(HttpUrl.parse(fields[0]), fields[1]);
    }

    private String cookieToPref(String url, Cookie cookie) {
        return url.concat("|:|").concat(cookie.toString());
    }

    public static boolean loggedWithCookie(){
        if (getINSTANCE().cookies.isEmpty()) return false;

        for (Map.Entry<String, Cookie > keyValue: getINSTANCE().cookies.entrySet()){
            if (keyValue.getKey().contains("[userid]")) return true;
        }

        return false;
    }

    public static void addLoginStateObservable(Observer observer){
        loginStateObservables.addObserver(observer);
    }

    public static void removeLoginStateObservable(Observer observer){
        loginStateObservables.deleteObserver(observer);
    }

    public static void notifyLoginStateObservables(Boolean state){
        loginStateObservables.notifyObservers(state);
    }

    public static void addNetworkObserver(Observer observer) {
        networkObservables.addObserver(observer);
    }

    public static void removeNetworkObserver(Observer observer) {
        networkObservables.deleteObserver(observer);
    }

    public static void notifyNetworkObservers(Boolean b) {
        // clear session cookies on every network changes

        networkObservables.notifyObservers(b);
    }

    public static boolean getNetworkState() {
        ConnectivityManager cm =
                (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
