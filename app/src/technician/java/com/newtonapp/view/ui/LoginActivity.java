package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.auth0.android.jwt.DecodeException;
import com.newtonapp.BuildConfig;
import com.newtonapp.R;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.TeknisiVerifyRequestModel;
import com.newtonapp.data.network.pojo.response.TeknisiVerifyResponseModel;
import com.newtonapp.utility.CommonUtil;
import com.newtonapp.utility.Constants;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private AppCompatImageView ivLogo;
    private AppCompatEditText etUsername;
    private AppCompatEditText etPassword;
    private AppCompatButton btnLogin;
    private AppCompatTextView tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setDummy();
        setListener();
        if (isLoggedIn()) {
            navigateTo(this, DashboardActivity.class);
            finish();
        }
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        ivLogo = findViewById(R.id.main_iv_logo);
        etUsername = findViewById(R.id.main_et_username);
        etPassword = findViewById(R.id.main_et_password);
        btnLogin = findViewById(R.id.main_btn_login);
        tvForgetPassword = findViewById(R.id.main_tv_forgetpassword_link);
    }

    private void setDummy() {
        etUsername.setText(Constants.username);
        etPassword.setText(Constants.password);
    }

    private void setListener() {
        tvForgetPassword.setOnClickListener(view -> doForgetPassword());
        btnLogin.setOnClickListener(view -> doLogin());
    }

    private boolean isLoggedIn() {
        String token = Prefs.getString(getString(R.string.key_token), null);
        if (!TextUtils.isEmpty(token)) {
            try {
                CommonUtil.getJWTtokenDecrypt(token);
                Log.d(TAG, "isLoggedIn: token exist, isLoggedIn -> true");
                return true;
            } catch (DecodeException de) {
                Log.e(TAG, "isLoggedIn: token error -> " + de.getMessage(), de);
                Toast.makeText(this, getString(R.string.error_bad_token), Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    private void doLogin() {
        String username = Objects.requireNonNull(etUsername.getText()).toString();
        String password = Objects.requireNonNull(etPassword.getText()).toString();

        boolean isValid = CommonUtil.isLoginValidated(username, password);
        if (isValid) {

            showMessageDialog(getString(R.string.progress_login));
            TeknisiVerifyRequestModel loginBody = new TeknisiVerifyRequestModel();
            loginBody.setUsername(username);
            loginBody.setPassword(password);
            Disposable subscRequestLogin = APIHelper.requestLogin(loginBody)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            teknisiVerifyResponseModel -> {
                                hideDialog();
                                if (teknisiVerifyResponseModel == null) {
                                    Toast.makeText(this, getString(R.string.error_null_response), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (teknisiVerifyResponseModel.getStatus() == 1) {
                                    proccedLoginSuccess(teknisiVerifyResponseModel);
                                } else {
                                    proceedLoginFailed(teknisiVerifyResponseModel);
                                }
                            }, error -> {
                                hideDialog();
                                Log.e(TAG, "doLogin: error -> " + error.getMessage(), error);
                                Toast.makeText(this, "Login failed:\n" + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    );
            compositeDisposable.add(subscRequestLogin);

        } else {
            Toast.makeText(this, "Please input correct data", Toast.LENGTH_LONG).show();
        }
    }

    private void proccedLoginSuccess(TeknisiVerifyResponseModel teknisiVerifyResponseModel) {

        if (BuildConfig.DEBUG)
            Toast.makeText(this, getString(R.string.success_message_login), Toast.LENGTH_SHORT).show();

        Prefs.putString(getString(R.string.key_token), teknisiVerifyResponseModel.getToken());
        boolean isFirstTime = Prefs.getBoolean(getString(R.string.key_first_time_user), true);
        if (isFirstTime) {
            Prefs.putBoolean(getString(R.string.key_first_time_user), false);
            navigateTo(this, OnboardingScreenActivity.class);
        } else navigateTo(this, DashboardActivity.class);
        finish();
    }

    private void proceedLoginFailed(TeknisiVerifyResponseModel teknisiVerifyResponseModel) {
        Toast.makeText(this, "Login failed\n" + teknisiVerifyResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
    }
    private void doForgetPassword() {
        navigateTo(this, ForgetPasswordActivity.class);
    }
}
