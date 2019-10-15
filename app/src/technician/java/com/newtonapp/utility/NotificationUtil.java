package com.newtonapp.utility;

import android.content.Context;
import android.os.Bundle;

import com.newtonapp.BuildConfig;
import com.newtonapp.model.notification.BaseNotification;
import com.newtonapp.model.notification.DebugNotification;
import com.newtonapp.model.notification.DefaultNotificationChannel;
import com.newtonapp.model.notification.NewOutstandingNotification;

public class NotificationUtil {

    private static final int DEBUG_NOTIFICATION_ID = 0;
    private static final int NEW_OUTSTANDING_NOTIFICATION_ID = 1;

    public static BaseNotification getNotification(Context context, Bundle bundle) {
        BaseNotification notification = new BaseNotification(context, bundle, DefaultNotificationChannel.getChannelId(), DefaultNotificationChannel.getChannelPriorityLevel());
        String extrasType = bundle.getString("type");

        if (extrasType != null) {
            if (extrasType.startsWith(Constants.NOTIF_TYPE_NEW_OUTSTANDING))
                notification = new NewOutstandingNotification(context, bundle, DefaultNotificationChannel.getChannelId(), DefaultNotificationChannel.getChannelPriorityLevel(), NEW_OUTSTANDING_NOTIFICATION_ID);
        }

        if (BuildConfig.DEBUG) {
            notification = new DebugNotification(context, bundle, DefaultNotificationChannel.getChannelId(), DefaultNotificationChannel.getChannelPriorityLevel(), DEBUG_NOTIFICATION_ID);
        }

        return notification;
    }
}
