package ru.gafuk.android.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;

/**
 * Created by Александр on 06.10.2017.
 */

public class CustomWebViewClient extends WebViewClient {
    private final static String LOG_TAG = CustomWebViewClient.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        Matcher matcher = cachePattern.matcher(url);
//        if (matcher.find()) {
//            try {
//                Log.d(LOG_TAG, "intercepted " + url);
//                WebResourceResponse resourceResponse = null;
//                String nick = matcher.group(1);
//                nick = URLDecoder.decode(nick, "UTF-8");
//                ForumUser forumUser = ForumUsersCache.loadUserByNick(nick);
//                Log.d(LOG_TAG, "Loaded user " + forumUser.getId() + " : " + forumUser.getNick() + " : " + forumUser.getAvatar());
//                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(forumUser.getAvatar());
//                String base64Bitmap = convert(bitmap);
//                base64Bitmap = "data:image/png;base64," + base64Bitmap;
//                resourceResponse = new WebResourceResponse("text/text", null, new ByteArrayInputStream(base64Bitmap.getBytes()));
//                return resourceResponse;
//            } catch (Exception e) {
//                e.printStackTrace();
//                super.shouldInterceptRequest(view, url);
//            }
//        }
        return super.shouldInterceptRequest(view, url);
    }

    public Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
    }
}
