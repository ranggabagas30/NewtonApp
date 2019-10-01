package com.newtonapp;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.newtonapp.model.notification.DefaultNotificationChannel;
import com.newtonapp.utility.DebugUtil;
import com.pixplicity.easyprefs.library.Prefs;

public class NewtonApplication extends Application implements ActivityLifecycleHandler.LifecycleListener {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        // Initialization FCM
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(instanceIdResult -> {

                    DebugUtil.d("FIREBASE INSTANCE ID ; " + instanceIdResult.getId());
                    DebugUtil.d("FIREBASE TOKEN : " + instanceIdResult.getToken());

                }).addOnFailureListener(Throwable::printStackTrace);

        // Create default notification channel
        DefaultNotificationChannel defaultNotificationChannel = new DefaultNotificationChannel();
        defaultNotificationChannel.createNotificationChannel(this);

        // Register activity lifecycle callbacks
        registerActivityLifecycleCallbacks(new ActivityLifecycleHandler(this));
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(new ActivityLifecycleHandler(this));
    }

    @Override
    public void onApplicationStopped() {
        DebugUtil.d("application stopped");
    }

    @Override
    public void onApplicationStarted() {
        DebugUtil.d("application started");
    }

    @Override
    public void onApplicationPaused() {
        DebugUtil.d("application paused");
    }

    @Override
    public void onApplicationResumed() {
        DebugUtil.d("application resumed");
    }
}
