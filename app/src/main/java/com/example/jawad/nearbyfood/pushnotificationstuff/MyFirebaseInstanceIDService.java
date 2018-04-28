package com.example.jawad.nearbyfood.pushnotificationstuff;

import com.example.jawad.nearbyfood.helper.GcmManagerManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        storeRegIdInPref(refreshedToken);
    }


    private void storeRegIdInPref(String token) {
        GcmManagerManager.setToken(token);
    }


}

