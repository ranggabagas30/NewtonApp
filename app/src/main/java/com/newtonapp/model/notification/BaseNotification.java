package com.newtonapp.model.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.newtonapp.R;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.view.ui.MainActivity;

public class BaseNotification {

    private NotificationCompat.Builder builder;
    private Notification notification;
    private final String CHANNEL_ID;
    private final int PRIORITY;
    private final int NOTIFICATION_ID;

    protected Context context;
    protected Bundle bundle;

    public BaseNotification(Context context, Bundle bundle, String CHANNEL_ID, int PRIORITY) {
        this(context, bundle, CHANNEL_ID, PRIORITY, 0);
    }

    public BaseNotification(Context context, Bundle bundle, String CHANNEL_ID, int PRIORITY, int NOTIFICATION_ID) {
        this.context = context;
        this.bundle = bundle;
        this.CHANNEL_ID = CHANNEL_ID;
        this.PRIORITY = PRIORITY;
        this.NOTIFICATION_ID = NOTIFICATION_ID;
    }

    protected NotificationCompat.Builder getNotifBuilder(final String CHANNEL_ID, final int PRIORITY) {

        if (builder != null)
            return builder;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(PRIORITY)
                .setContentTitle(getTitle())
                .setContentText(getMessage())
                .setContentIntent(getPendingIntent())
                .setSmallIcon(R.drawable.ic_logo2)
                .setTicker(getMessage())
                .setAutoCancel(true)
                .setDefaults(getNotifPattern());

        this.builder = builder;
        return builder;
    }

    public void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, getNotification());
    }

    protected void setNotification(Notification notification) {
        this.notification = notification;
    }

    protected Notification getNotification() {
        if (notification != null)
            return notification;
        DebugUtil.d("notification (title, message) : (" + getTitle() + ", " + getMessage() + ")");
        notification = getNotifBuilder(CHANNEL_ID, PRIORITY).build();
        return notification;
    }

    private int getNotifPattern() {
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        return defaults;
    }

    protected String getTitle() {
        return bundle.getString("title");
    }

    protected String getMessage() {
        return bundle.getString("message");
    }

    protected PendingIntent getPendingIntent() {
        Intent resultIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context.getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
