package ru.gafuk.android.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.gafuk.android.client.Client;

/**
 * Created by Александр on 17.01.2018.
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    protected Boolean connected;
    protected boolean mRegistered;

    public NetworkStateReceiver() {
        connected = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED){
            connected = true;
        }else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)){
            connected = false;
        }

        Client.notifyNetworkObservers(connected);

    }
}
