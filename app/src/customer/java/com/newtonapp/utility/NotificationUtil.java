package com.newtonapp.utility;

import android.content.Context;
import android.os.Bundle;

import com.newtonapp.model.notification.BaseNotification;
import com.newtonapp.model.notification.DefaultNotificationChannel;

public class NotificationUtil {

    public static BaseNotification getNotification(Context context, Bundle bundle) {
        BaseNotification baseNotification = new BaseNotification(context, bundle, DefaultNotificationChannel.CHANNEL_ID, DefaultNotificationChannel.getChannelPriorityLevel());
        String extrasType = bundle.getString("type");
        return baseNotification;
    }
}
