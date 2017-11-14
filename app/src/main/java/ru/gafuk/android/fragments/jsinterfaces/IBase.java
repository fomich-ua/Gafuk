package ru.gafuk.android.fragments.jsinterfaces;

import android.webkit.JavascriptInterface;

/**
 * Created by Александр on 26.09.17.
 */

public interface IBase {
    String JS_BASE_INTERFACE = "IBase";

    @JavascriptInterface
    void playClickEffect();

    //Событие DOMContentLoaded
    @JavascriptInterface
    void domContentLoaded();

    //Событие load в js
    @JavascriptInterface
    void onPageLoaded();
}
