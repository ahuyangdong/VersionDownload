package com.dommy.version.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.dommy.version.R;

public class NetworkUtil {

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isNetDisconnected(Context context) {
        if (!isNetworkConnected(context)) {
            Toast.makeText(context, R.string.net_not_connected, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
