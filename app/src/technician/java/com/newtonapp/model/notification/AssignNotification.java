package com.newtonapp.model.notification;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.newtonapp.R;
import com.newtonapp.view.ui.SolvingActivity;

public class AssignNotification extends BaseNotification {

    public static final int ASSIGN_NOTIFICATION_ID = 1;
    public AssignNotification(Context context, @NonNull String title, @NonNull String message) {
        super(
                context,
                R.drawable.ic_notification,
                title,
                message,
                HighNotificationChannel.CHANNEL_ID,
                HighNotificationChannel.CHANNEL_PRIORITY_LEVEL,
                NotificationCompat.CATEGORY_EMAIL
        );

        Intent intent = new Intent(context, SolvingActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        setPendingIntent(pendingIntent);
        setAutoCancel(true);
        setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
    }
}
