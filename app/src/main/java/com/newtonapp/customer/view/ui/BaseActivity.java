package com.newtonapp.customer.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void navigateTo(Activity activity, Class targetActivity) {
        Intent intent = new Intent(activity, targetActivity);
        startActivity(intent);
    }

    protected void navigateTo(Activity activity, Class targetActivity, int mode) {
        Intent intent = new Intent(activity, targetActivity);
        intent.setFlags(mode);
        startActivity(intent);
    }
}
