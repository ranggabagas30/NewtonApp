package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;

public class ForgetPasswordActivity extends BaseActivity {

    private Toolbar toolbar;
    private AppCompatEditText etNIK;
    private AppCompatEditText etEmail;
    private AppCompatEditText etPhone;
    private AppCompatEditText etBirthdate;
    private AppCompatButton btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        toolbar = findViewById(R.id.header_layout_toolbar);
        etNIK = findViewById(R.id.forgetpassword_et_nik);
        etEmail = findViewById(R.id.forgetpassword_et_email);
        etPhone = findViewById(R.id.forgetpassword_et_phone);
        etBirthdate = findViewById(R.id.forgetpassword_et_birthdate);
        btnConfirm = findViewById(R.id.forgetpassword_btn_confirm);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_forget_password);

        btnConfirm.setOnClickListener(view -> confirm());
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void confirm() {

    }

    private boolean isValid() {

        boolean isValid = false;
        String nik          = etNIK.getText().toString();
        String email        = etEmail.getText().toString();
        String phone        = etPhone.getText().toString();
        String birthdate    = etBirthdate.getText().toString();

        if (!TextUtils.isEmpty(nik) &&
         !TextUtils.isEmpty(email) &&
         !TextUtils.isEmpty(phone) &&
         !TextUtils.isEmpty(birthdate)) isValid = true;
        else isValid = false;

        return isValid;
    }
}
