package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;

import com.newtonapp.BuildConfig;
import com.newtonapp.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

public class SplashActivity extends BaseActivity {

    private AppCompatTextView tvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvAppVersion = findViewById(R.id.splash_tv_app_version);
        tvAppVersion.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME));
        compositeDisposable.add(
                Completable.timer(2, TimeUnit.SECONDS)
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
