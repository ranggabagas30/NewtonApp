package com.newtonapp.utility;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.newtonapp.BuildConfig;

import io.fabric.sdk.android.Fabric;

public class CrashlyticsWrapperUtil {

    private static final String GIT_SHA_KEY = "GIT_SHA";
    private static final String BUILD_TIME = "BUILD_TIME";

    public static void setup(Context context) {
        Fabric.with(context, new Crashlytics());
        Crashlytics.setString(GIT_SHA_KEY, BuildConfig.GIT_SHA);
        Crashlytics.setString(BUILD_TIME, BuildConfig.BUILD_TIME);
    }

    public static void setIdentifier(String identifier) {
        Crashlytics.setUserIdentifier(identifier);
        DebugUtil.d(Crashlytics.getInstance().getIdentifier());
    }
}
