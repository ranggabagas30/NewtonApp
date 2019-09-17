package com.newtonapp.view.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.newtonapp.R;
import com.newtonapp.utility.CommonUtil;
import com.pixplicity.easyprefs.library.Prefs;

public class LoginActivity extends BaseActivity {

    private AppCompatImageView ivLogo;
    private AppCompatEditText etUsername;
    private AppCompatEditText etPassword;
    private AppCompatButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        btnLogin.setOnClickListener(view -> doLogin());
    }

    private void initView() {
        ivLogo = findViewById(R.id.main_iv_logo);
        etUsername = findViewById(R.id.main_et_username);
        etPassword = findViewById(R.id.main_et_password);
        btnLogin = findViewById(R.id.main_btn_login);
    }

    private void doLogin() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (CommonUtil.isLoginValidated(username, password)) {
            boolean isFirstTime = Prefs.getBoolean(getString(R.string.key_first_time_user), true);
            if (isFirstTime) {
                Prefs.putBoolean(getString(R.string.key_first_time_user), false);
                navigateTo(this, OnboardingScreenActivity.class);
            } else navigateTo(this, DashboardActivity.class);
            finish();
        } else {
            Toast.makeText(this, "Please input correct data", Toast.LENGTH_LONG).show();
        }
    }
}
