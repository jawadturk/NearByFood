package com.example.jawad.nearbyfood.pushnotificationstuff;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.example.jawad.nearbyfood.MainActivity;
import com.example.jawad.nearbyfood.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final String NOTIFICATION_TYPE_REQUEST = "1";
    private static final String NOTIFICATION_TYPE_WORK_ORDER = "2";


    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null)
            return;


        Map<String, String> data = remoteMessage.getData();
        Log.d(TAG, "onMessageReceived: " + data);


        // Check if message contains a data payload.
        if (data.size() > 0) {
            Log.e(TAG, "Data Payload: " + data.toString());
            try {
                JSONObject json = new JSONObject(data);
                handleMessageData(json);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }


    }

    private void handleMessageData(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {

            String messageTitle = "New Message";
            String messageContent = "New Chat Message Received";

            if (json.has("title")) {
                messageTitle = json.getString("title");
                Log.d(TAG, "messageTitle: " + messageTitle);
            }

            if (json.has("message")) {
                messageContent = json.getString("message");
                Log.d(TAG, "messageContent: " + messageContent);
            }

            displayNotification(messageContent, messageTitle);

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    protected void displayNotification(String message, String title) {

        Log.i("Start", "notification");

        // Invoking the default notification service //
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setAutoCancel(true);

        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        // Creates an explicit intent for an Activity in your app //

//        Intent resultIntent = new Intent(this, MessagesListActivity.class);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//
//        // Adds the Intent that starts the Activity to the top of the stack //
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);


        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent resultPendingIntent = PendingIntent.getActivities(
                this, 0,
                new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(resultPendingIntent);


        //  mBuilder.setOngoing(true);
        Notification note = mBuilder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationID allows you to update the notification later on. //
        mNotificationManager.notify(1, mBuilder.build());

    }

}
