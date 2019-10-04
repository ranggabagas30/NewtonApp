package com.newtonapp.model.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.newtonapp.R;
import com.newtonapp.view.ui.OutstandingActivity;

public class NewOutstandingNotification extends BaseNotification {


    public NewOutstandingNotification(Context context, Bundle bundle, String CHANNEL_ID, int PRIORITY, int NOTIFICATION_ID) {
        super(context, bundle, CHANNEL_ID, PRIORITY, NOTIFICATION_ID);
    }

    @Override
    protected PendingIntent getPendingIntent() {
        Intent resultIntent = new Intent(context, OutstandingActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context.getApplicationContext(),0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected String getMessage() {
        return bundle.getString("message");
    }

    @Override
    protected String getTitle() {
        return context.getString(R.string.app_name);
    }

    private String getOrderCode(){
        return bundle.getString("order_code");
    }
}
