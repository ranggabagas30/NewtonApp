package com.newtonapp;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.firebase.FirebaseApp;
import com.pixplicity.easyprefs.library.Prefs;

public class NewtonAppCustApplication extends Application {

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
}
