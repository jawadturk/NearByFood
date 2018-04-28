package com.example.jawad.nearbyfood.helper;


import com.example.jawad.nearbyfood.application.ApplicationClass;

public class GcmManagerManager {
    private static final String GCM_TOKEN = "gcmToken";

    private static final String GCM_SENT_TO_SERVER = "sent";

    private static final String tokenDefaultValue = "";
    private static final boolean tokenSentToServer = false;

    public static String getToken() {

        return PrefUtils.getString(ApplicationClass.getInstance(), GCM_TOKEN, tokenDefaultValue);
    }

    public static void setToken(String cookie) {
        PrefUtils.setString(ApplicationClass.getInstance(), GCM_TOKEN, cookie);
    }

    public static Boolean isGcmSentToServer() {

        return PrefUtils.getBoolean(ApplicationClass.getInstance(), GCM_SENT_TO_SERVER, tokenSentToServer);
    }

    public static void setGcmSent(boolean sent) {
        PrefUtils.setBoolean(ApplicationClass.getInstance(), GCM_SENT_TO_SERVER, sent);
    }

}
