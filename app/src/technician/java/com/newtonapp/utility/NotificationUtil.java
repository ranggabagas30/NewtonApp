package com.newtonapp.utility;

import android.content.Context;

import com.google.firebase.messaging.RemoteMessage;
import com.newtonapp.model.notification.AssignNotification;

public class NotificationUtil {

    public static void notify(Context context, RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        new AssignNotification(context, title, message).show(AssignNotification.ASSIGN_NOTIFICATION_ID);
    }
}
