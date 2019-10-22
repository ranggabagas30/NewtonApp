package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;

import com.newtonapp.R;

public class OnboardingScreenActivity extends BaseActivity {

    private AppCompatButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);
        btnNext = findViewById(R.id.onboarding_btn_next);
        btnNext.setOnClickListener(view -> navigateTo(this, DashboardActivity.class));
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

}
