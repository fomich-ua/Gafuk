package ru.gafuk.android.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.gafuk.android.R;
import ru.gafuk.android.fragments.BaseFragment;

/**
 * Created by Александр on 18.09.2017.
 */
public class ErrorHandler {


    public static void handle(BaseFragment fragment, Throwable throwable, View.OnClickListener listener) {
        throwable.printStackTrace();
        String text = "";
        boolean isNetworkEx = false;

        Class c = throwable.getClass();

        if (c == SocketTimeoutException.class) {
            text = "TimeOut";
            isNetworkEx = true;
        } else if (c == UnknownHostException.class) {
            text = "Server not available";
            isNetworkEx = true;
        } else if (c == IllegalArgumentException.class) {
            Matcher matcher = Pattern.compile("unexpected (.*?):").matcher(throwable.getMessage());
            if (matcher.find()) {
                text = text.concat("Неверный аргумент ").concat(matcher.group(1));
            }
//        } else if (c == OkHttpResponseException.class) {
//            OkHttpResponseException exception = (OkHttpResponseException) throwable;
//            text = exception.getName() + " : " + exception.getCode();
//            isNetworkEx = true;
//        } else if (c == OnlyShowException.class) {
//            listener = null;
//            text = throwable.getMessage();
        } else {
            text = throwable.getMessage();
        }


        //if (isNetworkEx) {
        Snackbar snackbar = Snackbar.make(fragment.getCoordinatorLayout(), text, /*listener != null ? Snackbar.LENGTH_INDEFINITE :*/ Snackbar.LENGTH_LONG);
        if (listener != null /*&& isNetworkEx*/)
            snackbar.setAction(R.string.retry, listener);
        snackbar.setActionTextColor(Color.WHITE);
        try {
            TextView textView = (TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
        }catch (Exception ignore){}

        snackbar.show();
        /*} else {

            final String finalText = text;
            new Thread(() -> {
                Toast.makeText(App.getContext(), "Not Network Ex\n" + finalText, Toast.LENGTH_SHORT).show();
            }).run();
        }*/

    }
}
