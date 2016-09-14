package com.money;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import io.fabric.sdk.android.Fabric;

/**
 * Created by skuba on 28.06.2016.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        FirebaseCrash.report(new Exception("BaseActivity"));
//        FirebaseAnalytics.getInstance(this);
    }
}