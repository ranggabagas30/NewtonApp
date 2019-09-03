package com.newtonapp.customer.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
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

    public void showMessageDialog(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
            if (!progressDialog.isShowing())
                progressDialog.show();
        }
    }

    public void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
