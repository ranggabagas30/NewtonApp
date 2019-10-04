package com.newtonapp.utility;

import android.content.Context;
import android.os.Bundle;

import com.newtonapp.model.notification.BaseNotification;
import com.newtonapp.model.notification.DefaultNotificationChannel;
import com.newtonapp.model.notification.NewOutstandingNotification;

public class NotificationUtil {

    private static final int NEW_OUTSTANDING_NOTIFICATION_ID = 1;

    public static BaseNotification getNotification(Context context, Bundle bundle) {
        BaseNotification baseNotification = new BaseNotification(context, bundle, DefaultNotificationChannel.getChannelId(), DefaultNotificationChannel.getChannelPriorityLevel());
        String extrasType = bundle.getString("type");

        if (extrasType != null) {
            if (extrasType.startsWith(Constants.NOTIF_TYPE_NEW_OUTSTANDING))
                baseNotification = new NewOutstandingNotification(context, bundle, DefaultNotificationChannel.getChannelId(), DefaultNotificationChannel.getChannelPriorityLevel(), NEW_OUTSTANDING_NOTIFICATION_ID);
        }

        return baseNotification;
    }
}
