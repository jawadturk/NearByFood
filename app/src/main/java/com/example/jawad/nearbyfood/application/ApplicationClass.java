package com.example.jawad.nearbyfood.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class ApplicationClass extends Application {

    private static ApplicationClass mInstance;

    public static synchronized ApplicationClass getInstance() {
        return mInstance;
    }

    public static boolean hasNetwork() {
        return mInstance.checkIfHasNetwork();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;



    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
