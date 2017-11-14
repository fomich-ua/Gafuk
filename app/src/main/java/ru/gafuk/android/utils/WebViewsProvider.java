package ru.gafuk.android.utils;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import ru.gafuk.android.views.ExtendedWebView;

/**
 * Created by Александр on 07.11.2017.
 */

public class WebViewsProvider {
    private Queue<ExtendedWebView> availableWebViews = new LinkedList<>();
    private Timer webViewCleaner = new Timer();

    public WebViewsProvider() {
        webViewCleaner.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("WebViewsProvider", "try remove webview " + this);
                if (availableWebViews.size() > 0) {
                    Log.d("WebViewsProvider", "remove webview " + availableWebViews.element().getTag());
                    availableWebViews.remove();
                }
            }
        }, 0, 60000);
    }

    public ExtendedWebView pull(Context context) {
        ExtendedWebView webView;
        if (availableWebViews.size() > 0) {
            webView = availableWebViews.poll();
        } else {
            webView = new ExtendedWebView(context);
            webView.setTag("WebView_tag ".concat(Long.toString(System.currentTimeMillis())));
        }
        Log.d("WebViewsProvider", "Pull " + webView);
        return webView;
    }

    public void push(ExtendedWebView webView) {
        if (webView == null)
            return;
        ViewGroup parent = ((ViewGroup) webView.getParent());
        if (parent != null) {
            parent.removeView(webView);
        }
        if (availableWebViews.size() < 10) {
            availableWebViews.add(webView);
        }
        Log.d("WebViewsProvider", "Push " + webView);
    }

    public void destroy() {
        webViewCleaner.cancel();
        webViewCleaner.purge();
        availableWebViews.clear();
    }

}
