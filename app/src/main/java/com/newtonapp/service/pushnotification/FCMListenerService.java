package com.newtonapp.service.pushnotification;

import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.newtonapp.BuildConfig;
import com.newtonapp.R;
import com.newtonapp.model.notification.MessagePayload;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.NotificationUtil;
import com.pixplicity.easyprefs.library.Prefs;

public class FCMListenerService extends FirebaseMessagingService {
    StringBuilder fcmCompiled;

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
        String from = "From : " + remoteMessage.getFrom();
        String to   = "To: " + remoteMessage.getTo();
        String sentTime = "sent time: " + remoteMessage.getSentTime();
        String ttl = "ttl : " + remoteMessage.getTtl();

        fcmCompiled = new StringBuilder();
        fcmCompiled.append(from).append("\n");
        fcmCompiled.append(to).append("\n");
        fcmCompiled.append(sentTime).append("\n");
        fcmCompiled.append(ttl).append("\n");

        DebugUtil.d(from);
        DebugUtil.d(to);
        DebugUtil.d(sentTime);
        DebugUtil.d(ttl);

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

            fcmCompiled.append("Message Id : " + messageId).append("\n");
            fcmCompiled.append("Message collapse key : " + messageCollapseKey).append("\n");
            fcmCompiled.append("Message type : " + messageType).append("\n");
            fcmCompiled.append("Message channel id :  " + messageChannelId).append("\n");
            fcmCompiled.append("Message title : " + messageTitle).append("\n");
            fcmCompiled.append("Message Body : " + messageBody).append("\n");
            fcmCompiled.append("Message data : " + messageData).append("\n");

            MessagePayload messagePayload = new Gson().fromJson(messageBody, MessagePayload.class);

            try {
                handleNotification(messagePayload);
            } catch (NullPointerException e) {
                DebugUtil.e("NOTIFICATION ERROR: " + e.getMessage(), e);
            }
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

    private void handleNotification(MessagePayload messagePayload) throws NullPointerException {

        if (TextUtils.isEmpty(messagePayload.getAction()))
            throw new NullPointerException("notification action is empty");

        if (TextUtils.isEmpty(messagePayload.getMessage()))
            throw new NullPointerException("notification message is empty");

        if (BuildConfig.DEBUG) {
            Prefs.putString(getString(R.string.key_firebase_message_payload), fcmCompiled.toString());
        }

        NotificationUtil.notify(this, messagePayload);
    }

    private void handleDataMessage(RemoteMessage remoteMessage) {
        String action = remoteMessage.getData().get("action");
        String actionDestination = remoteMessage.getData().get("action_destination");
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        fcmCompiled.append("Message payload: {").append("\n");
        fcmCompiled.append("\taction: ").append(action).append(", \n");
        fcmCompiled.append("\taction_destination: ").append(actionDestination).append(", \n");
        fcmCompiled.append("\ttitle: ").append(title).append(", \n");
        fcmCompiled.append("\tmessage: ").append(message).append("\n");
        fcmCompiled.append("}");

        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setAction(action);
        messagePayload.setActionDestination(actionDestination);
        messagePayload.setTitle(title);
        messagePayload.setMessage(message);

        try {
            handleNotification(messagePayload);
        } catch (NullPointerException e) {
            DebugUtil.e("NOTIFICATION ERROR: " + e.getMessage(), e);
        }
    }
}
