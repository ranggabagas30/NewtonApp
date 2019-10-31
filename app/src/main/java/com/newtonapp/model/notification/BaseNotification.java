package com.newtonapp.model.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.newtonapp.view.ui.MainActivity;

public class BaseNotification {

    private NotificationManagerCompat notificationManagerCompat;
    private NotificationCompat.Builder notificationBuilder;
    private Notification notification;

    public static final int DEFAULT_NOTIFICATION_ID = 0;

    public BaseNotification(Context context, int resIconDrawable, String title, String message, String channelId, int priorityLevel, String category) {
        notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(resIconDrawable)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
                .setAutoCancel(true)
                .setPriority(priorityLevel)
                .setCategory(category);
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        notificationBuilder.setContentIntent(pendingIntent);
    }

    public void setAutoCancel(boolean isAutoCancel) {
        notificationBuilder.setAutoCancel(isAutoCancel);
    }

    public void show() {
        show(DEFAULT_NOTIFICATION_ID);
    }

    public void show(final int ID) {
        notification = notificationBuilder.build();
        notificationManagerCompat.notify(ID, notification);
    }
}
