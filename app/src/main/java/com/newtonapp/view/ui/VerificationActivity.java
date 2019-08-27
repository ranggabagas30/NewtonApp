package com.newtonapp.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;

import com.newtonapp.R;

public class VerificationActivity extends BaseActivity {

    private AppCompatButton btnTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        btnTracking = findViewById(R.id.verification_btn_tracking);
        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTo(VerificationActivity.this, ProblemTrackingActivity.class);
            }
        });
    }
}
