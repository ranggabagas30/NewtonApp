package com.newtonapp.customer;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class NewtonAppCustApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

    }
}
