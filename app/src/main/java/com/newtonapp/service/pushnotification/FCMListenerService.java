package com.newtonapp.service.pushnotification;

import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.newtonapp.R;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.NotificationUtil;
import com.pixplicity.easyprefs.library.Prefs;

public class FCMListenerService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        DebugUtil.d("==== FIREBASE MESSAGE RECEIVED ====\n");

        // [START_EXCLUDE]
        // There are two types of messages : data messages and notification messages. ImbasPetirData messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. ImbasPetirData
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        DebugUtil.d("From : " + remoteMessage.getFrom());
        DebugUtil.d("To: " + remoteMessage.getTo());
        DebugUtil.d("sent time: " + remoteMessage.getSentTime());
        DebugUtil.d("ttl : " + remoteMessage.getTtl());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            // data can be processed in long-running mode (using Firebase Job Dispatcher)
            // or just handle it directly which is short-running mode (under 10 secs)
            handleDataMessage(remoteMessage);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            String messageId = remoteMessage.getMessageId();
            String messageCollapseKey = remoteMessage.getCollapseKey();
            String messageType = remoteMessage.getMessageType();
            String messageChannelId = remoteMessage.getNotification().getChannelId();
            String messageTitle = remoteMessage.getNotification().getTitle();
            String messageBody = remoteMessage.getNotification().getBody();
            String messageData = remoteMessage.getData().toString();

            DebugUtil.d("Message Id : " + messageId);
            DebugUtil.d("Message collapse key : " + messageCollapseKey);
            DebugUtil.d("Message type : " + messageType);
            DebugUtil.d("Message channel id :  " + messageChannelId);
            DebugUtil.d("Message title : " + messageTitle);
            DebugUtil.d("Message Body : " + messageBody);
            DebugUtil.d("Message data : " + messageData);

            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.NOTIF_TYPE_NEW_OUTSTANDING);
            bundle.putString("message", messageBody);
            handleNotification(bundle);
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        /**
         * once get new registration token
         * 1. save to shared pref
         * 2. send token to server
         *
         * */
        DebugUtil.d("NEW TOKEN : " + token);
        saveFirebaseToken(token);
    }

    private void saveFirebaseToken(String token) {
        Prefs.putString(getString(R.string.key_firebase_token), token);
    }

    private void handleNotification(Bundle bundleMessage) {
        NotificationUtil.getNotification(this, bundleMessage).sendNotification();
    }

    private void handleDataMessage(RemoteMessage remoteMessage) {

        String type = remoteMessage.getData().get("type");
        String message = remoteMessage.getData().get("message");
        DebugUtil.d("Message data payload : " + remoteMessage.getData());

        Bundle extras = new Bundle();
        extras.putString("type", type);
        extras.putString("message", message);

        handleNotification(extras);
    }
}
