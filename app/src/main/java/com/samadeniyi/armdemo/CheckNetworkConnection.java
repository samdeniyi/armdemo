package com.samadeniyi.armdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckNetworkConnection {
    static public void isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        boolean hasInternet =  connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        if (hasInternet) {
//            Toast.makeText(context, "Network available", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "No network", Toast.LENGTH_LONG).show();
        }
    }
}
