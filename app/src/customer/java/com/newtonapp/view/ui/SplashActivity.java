package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;

import com.newtonapp.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        compositeDisposable.add(
                Completable.timer(1, TimeUnit.SECONDS)
                        .subscribe(() -> {
                            navigateTo(this, MainActivity.class);
                            finish();
                        })
        );
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }
}
