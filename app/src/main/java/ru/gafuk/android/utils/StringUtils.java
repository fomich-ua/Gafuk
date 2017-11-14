package ru.gafuk.android.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import ru.gafuk.android.App;
import ru.gafuk.android.R;

/**
 * Created by Александр on 06.11.2017.
 */

public class StringUtils {
    private StringUtils(){}

    public static int str2int(String str){
        Integer result = null;

        if (null == str || 0 == str.length()) {
            return result;
        }

        try {
            result = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            String negativeMode = "";
            if(str.indexOf('−') != -1)
                negativeMode = "-";
            str = str.replaceAll("-", "" );
            if (str.indexOf('.') != -1) {
                str = str.substring(0, str.indexOf('.'));
                if (str.length() == 0) {
                    return 0;
                }
            }
            String strNum = str.replaceAll("[^\\d]", "" );
            if (0 == strNum.length()) {
                return 0;
            }
            result = Integer.parseInt(negativeMode + strNum);
        }
        return result;
    }

    public static void openUrlInBrowser(String s){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        App.getContext().startActivity(intent);
    }

    public static void copyToClipBoard(String s){
        ClipboardManager clipboardManager = (ClipboardManager) App.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", s);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static void shareText(String text){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getContext().startActivity(Intent.createChooser(sendIntent, App.getContext().getString(R.string.share)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
