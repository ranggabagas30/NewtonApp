package com.newtonapp.model.notification;

import android.content.Context;

public abstract class BaseNotificationChannel {
    public abstract void createNotificationChannel(Context context);
    public abstract void deleteNotificationChannel(Context context);
}
