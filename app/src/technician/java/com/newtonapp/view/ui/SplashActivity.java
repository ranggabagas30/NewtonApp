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
                Completable.timer(3, TimeUnit.SECONDS)
                        .subscribe(() -> {
                            if (isLoggedIn()) {
                                navigateTo(this, DashboardActivity.class);
                                finish();
                            } else {
                                navigateTo(this, MainActivity.class);
                                finish();
                            }
                        })
        );
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    private boolean isLoggedIn() {
        return loginToken != null;
    }
}
