package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.auth0.android.jwt.DecodeException;
import com.newtonapp.BuildConfig;
import com.newtonapp.R;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.VerificationRequestModel;
import com.newtonapp.data.network.pojo.response.VerificationResponseModel;
import com.newtonapp.utility.CommonUtil;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.NetworkUtil;
import com.newtonapp.utility.PermissionUtil;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.RC_ALL_PERMISSIONS) {
            doLogin();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        doLogin();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        DebugUtil.d("onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
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

    @AfterPermissionGranted(Constants.RC_ALL_PERMISSIONS)
    private void requestAllPermissions() {
        PermissionUtil.requestAllPermissions(this, getString(R.string.warning_message_rationale_all_permissions), Constants.RC_ALL_PERMISSIONS);
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
        if (!PermissionUtil.hasAllPermissions(this)) {
            requestAllPermissions();
        } else {
            String username = Objects.requireNonNull(etUsername.getText()).toString();
            String password = Objects.requireNonNull(etPassword.getText()).toString();

            boolean isValid = CommonUtil.isLoginValidated(username, password);
            if (isValid) {

                showMessageDialog(getString(R.string.progress_login));
                VerificationRequestModel loginBody = new VerificationRequestModel();
                loginBody.setUsername(username);
                loginBody.setPassword(password);
                compositeDisposable.add(
                        APIHelper.requestLogin(loginBody)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                        verificationResponseModel -> {
                                            hideDialog();
                                            if (verificationResponseModel == null) {
                                                Toast.makeText(this, getString(R.string.error_null_response), Toast.LENGTH_LONG).show();
                                                return;
                                            }

                                            if (verificationResponseModel.getStatus() == 1) {
                                                proccedLoginSuccess(verificationResponseModel);
                                            } else {
                                                proceedLoginFailed(verificationResponseModel);
                                            }
                                        }, error -> {
                                            hideDialog();
                                            String errorMessage = NetworkUtil.handleApiError(error);
                                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                                        }
                                )
                );

            } else {
                Toast.makeText(this, getString(R.string.error_incorrect_data), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void proccedLoginSuccess(VerificationResponseModel verificationResponseModel) {

        if (BuildConfig.DEBUG)
            Toast.makeText(this, getString(R.string.success_message_login), Toast.LENGTH_SHORT).show();

        Prefs.putString(getString(R.string.key_token), verificationResponseModel.getToken());
        boolean isFirstTime = Prefs.getBoolean(getString(R.string.key_first_time_user), true);
        if (isFirstTime) {
            Prefs.putBoolean(getString(R.string.key_first_time_user), false);
            navigateTo(this, OnboardingScreenActivity.class);
        } else navigateTo(this, DashboardActivity.class);
        finish();
    }

    private void proceedLoginFailed(VerificationResponseModel verificationResponseModel) {
        Toast.makeText(this, "Login failed\n" + verificationResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void doForgetPassword() {
        navigateTo(this, ForgetPasswordActivity.class);
    }

}
