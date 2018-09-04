package com.ecity.cswatersupply.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkHelper {
    /*
     * judge the type of network
     * 0:no net service 
     * 1:cell network 
     * 2:WIFI 
     * 3:the net is connected
     */
    public static int network_Identification(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].isConnected()) {
                        if (0 == info[i].getType()) {
                            return 1;
                        }
                        if (1 == info[i].getType()) {
                            return 2;
                        }
                        return 3;
                    }

                }
            }
        }
        return 0;
    }

    /**
     * detection the connection of network
     * @return
     */
    public static boolean checkConnection(Context context) {
        if (null == context) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * check the current network is WIFI or not
     * 
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI");
    }
}
