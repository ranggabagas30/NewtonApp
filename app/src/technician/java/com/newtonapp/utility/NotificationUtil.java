package com.newtonapp.utility;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.newtonapp.R;
import com.newtonapp.model.notification.AssignNotification;
import com.newtonapp.model.notification.BaseNotification;
import com.newtonapp.model.notification.DefaultNotificationChannel;
import com.newtonapp.model.notification.MessagePayload;

public class NotificationUtil {

    public static void notify(Context context, MessagePayload messagePayload) {
        String action = messagePayload.getAction();
        String actionDestination = messagePayload.getActionDestination();
        String image = messagePayload.getImage();
        String title = messagePayload.getTitle();
        String message = messagePayload.getMessage();

        if (action.equalsIgnoreCase(context.getString(R.string.notification_assignment)))
            new AssignNotification(context, title, message).show(AssignNotification.ASSIGN_NOTIFICATION_ID);
        else new BaseNotification(context, R.drawable.ic_notification, title, message, DefaultNotificationChannel.CHANNEL_ID, DefaultNotificationChannel.CHANNEL_PRIORITY_LEVEL, NotificationCompat.CATEGORY_MESSAGE).show();
    }
}
