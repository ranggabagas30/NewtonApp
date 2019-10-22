package com.newtonapp.model.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class HighNotificationChannel extends BaseNotificationChannel {

    public static String CHANNEL_ID = "HIGH_CHANNEL_ID";
    private CharSequence CHANNEL_NAME = "HIGH NOTIFICATION";
    private String CHANNEL_DESC = "High level priority notification";
    private int CHANNEL_IMPORTANCE_LEVEL = NotificationManager.IMPORTANCE_HIGH; // for Android OS 8.0 and above
    public static int CHANNEL_PRIORITY_LEVEL = NotificationCompat.PRIORITY_HIGH; // for Android OS 7.0 and lower
    private NotificationChannel channel;

    @Override
    public void createNotificationChannel(Context context) {
        if (channel == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE_LEVEL);
            channel.setDescription(CHANNEL_DESC);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                            .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            channel.setVibrationPattern(new long[]{100, 100, 100, 100});

            // register notification channel. You cannot change after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void deleteNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.deleteNotificationChannel(CHANNEL_ID);
        }
    }
}
