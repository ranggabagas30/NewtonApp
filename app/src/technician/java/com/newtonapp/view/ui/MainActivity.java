package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.newtonapp.BuildConfig;
import com.newtonapp.R;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.ErrorLoggingRequestModel;
import com.newtonapp.data.network.pojo.request.FirebaseTokenSendingRequestModel;
import com.newtonapp.data.network.pojo.request.VerificationRequestModel;
import com.newtonapp.data.network.pojo.response.VerificationResponseModel;
import com.newtonapp.utility.CommonUtil;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.DateTimeUtil;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.NetworkUtil;
import com.newtonapp.utility.PermissionUtil;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

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

    private String deviceId;
    private String username;
    private String password;
    private String token;
    private String firebaseToken;
    private String errorMessage;
    private String errorDateTime;
    private String errorDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtil.d(TAG.concat(" created"));
        setContentView(R.layout.activity_login);
        initView();
        setListener();
        setDummy();
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
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                username = editable.toString();
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = editable.toString();
            }
        });
        tvForgetPassword.setOnClickListener(view -> doForgetPassword());
        btnLogin.setOnClickListener(view -> doLogin());
    }

    @AfterPermissionGranted(Constants.RC_ALL_PERMISSIONS)
    private void requestAllPermissions() {
        PermissionUtil.requestAllPermissions(this, getString(R.string.warning_message_rationale_all_permissions), Constants.RC_ALL_PERMISSIONS);
    }


    private boolean isLoggedIn() {
        return loginToken != null;
    }

    private void doLogin() {
        if (!PermissionUtil.hasAllPermissions(this)) {
            requestAllPermissions();
        } else {
            if (isLoginValidated(username, password)) {
                showMessageDialog(getString(R.string.progress_login));
                VerificationRequestModel loginBody = new VerificationRequestModel();
                loginBody.setUsername(username);
                loginBody.setPassword(password);
                compositeDisposable.add(
                        APIHelper.requestLogin(loginBody)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                        response -> {
                                            hideDialog();
                                            if (response == null) {
                                                Toast.makeText(this, getString(R.string.error_null_response), Toast.LENGTH_LONG).show();
                                                return;
                                            }

                                            if (response.getStatus() == 1) {
                                                onSuccessLogin(response);

                                            } else {
                                                onFailedLogin(response);
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

    private void onSuccessLogin(VerificationResponseModel response) {

        if (BuildConfig.DEBUG) Toast.makeText(this, getString(R.string.success_message_login), Toast.LENGTH_SHORT).show();

        token = response.getToken();
        saveToken(token);

        boolean isFirstTime = getFirstTimeUserFlag();
        if (isFirstTime) {
            setFirstTimeUserFlag(false);
            sendFirebaseToken();
            navigateTo(this, OnboardingScreenActivity.class);
        } else navigateTo(this, DashboardActivity.class);
        finish();
    }

    private void onFailedLogin(VerificationResponseModel response) {
        Toast.makeText(this, "Login failed\n" + response.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void sendFirebaseToken() {
        try {
            deviceId = CommonUtil.getIMEI(this);
        } catch (SecurityException se) {
            errorMessage = getString(R.string.error_failed_sending_firebase_token);
            errorDescription = getString(R.string.error_read_phone_state_not_allowed);
            errorDateTime = DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_TIME_PATTERN_1);;
            sendErrorLog(username, deviceId, token, errorMessage, errorDateTime, errorDescription);
            DebugUtil.e(errorMessage, se);
            return;
        }

        firebaseToken = Prefs.getString(getString(R.string.key_firebase_token), null);
        if (TextUtils.isEmpty(firebaseToken)) {
            errorMessage = getString(R.string.error_failed_sending_firebase_token);
            errorDescription = getString(R.string.error_null_firebase_token);
            errorDateTime = DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_TIME_PATTERN_1);;
            sendErrorLog(username, deviceId, token, errorMessage, errorDateTime, errorDescription);
            return;
        }

        FirebaseTokenSendingRequestModel formBody = new FirebaseTokenSendingRequestModel();
        formBody.setUsername(username);
        formBody.setWebtoken(token);
        formBody.setImei(deviceId);
        formBody.setFirebasetoken(firebaseToken);
        compositeDisposable.add(
                APIHelper.sendFirebaseToken(formBody)
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribeOn(Schedulers.io())
                         .subscribe(
                                 response -> {
                                     if (response == null)
                                         throw new NullPointerException(getString(R.string.error_null_response));

                                     if (response.getStatus() == 1) {
                                         // success
                                         DebugUtil.d(getString(R.string.success_message_firebase_token_sent));
                                     } else {
                                         errorMessage = getString(R.string.error_failed_sending_firebase_token);
                                         errorDescription = "ERROR: " + response.getMessage();;
                                         errorDateTime = DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_TIME_PATTERN_1);;
                                         sendErrorLog(username, deviceId, token, errorMessage, errorDateTime, errorDescription);
                                     }
                                 }, error -> {
                                     errorMessage = getString(R.string.error_failed_sending_firebase_token);
                                     errorDescription = "ERROR: " + NetworkUtil.handleApiError(error);
                                     errorDateTime = DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_TIME_PATTERN_1);;
                                     sendErrorLog(username, deviceId, token, errorMessage, errorDateTime, errorDescription);
                                 }
                         )
        );
    }

    private void sendErrorLog(String username, String imei, String webtoken, String errorMessage, String errorDateTime, String errorDescription) {
        ErrorLoggingRequestModel formBody = new ErrorLoggingRequestModel();
        formBody.setUsername(username);
        formBody.setImei(imei);
        formBody.setWebtoken(webtoken);
        formBody.setErrorMessage(errorMessage);
        formBody.setErrorDatetime(errorDateTime);
        formBody.setErrorDescription(errorDescription);
        compositeDisposable.add(
                APIHelper.sendErrorLog(formBody)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> DebugUtil.d("error log sent"),
                                throwable -> {
                                    DebugUtil.e("error log not sent: " + throwable.getMessage(), throwable);
                                })
        );
    }

    private void doForgetPassword() {
        navigateTo(this, ForgetPasswordActivity.class);
    }

    private boolean isLoginValidated(String username, String password) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            isValid = true;
        }
        return isValid;
    }
}
