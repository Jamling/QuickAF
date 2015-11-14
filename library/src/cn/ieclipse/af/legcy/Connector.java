/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.legcy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connector {
    private ConnectivityManager mConnectService;
    
    public Connector(Context context) {
        mConnectService = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    
    public boolean isNetworkConnected() {
        if (mConnectService != null) {
            NetworkInfo mNetworkInfo = mConnectService.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    
    public boolean isWifiConnected() {
        if (mConnectService != null) {
            NetworkInfo mWiFiNetworkInfo = mConnectService
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    
    public boolean isMobileConnected() {
        if (mConnectService != null) {
            NetworkInfo mMobileNetworkInfo = mConnectService
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    
    public int getConnectedType() {
        if (mConnectService != null) {
            NetworkInfo mNetworkInfo = mConnectService.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
