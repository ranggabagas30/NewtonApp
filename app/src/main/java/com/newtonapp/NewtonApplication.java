package com.newtonapp;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.firebase.FirebaseApp;
import com.newtonapp.utility.DebugUtil;
import com.pixplicity.easyprefs.library.Prefs;

public class NewtonApplication extends Application implements ActivityLifecycleHandler.LifecycleListener {

    private static volatile NewtonApplication INSTANCE;

    public NewtonApplication() {}

    public static synchronized NewtonApplication getInstance() {

        if (INSTANCE == null) {
            synchronized (NewtonApplication.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NewtonApplication();
                }
            }
        }
        return INSTANCE;
    }

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
