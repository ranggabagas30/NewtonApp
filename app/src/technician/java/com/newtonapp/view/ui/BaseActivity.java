package com.newtonapp.view.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.f2prateek.rx.preferences2.Preference;
import com.newtonapp.NewtonApplication;
import com.newtonapp.R;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.LogoutRequestModel;
import com.newtonapp.utility.CommonUtil;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.NetworkUtil;
import com.pixplicity.easyprefs.library.Prefs;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity extends BaseProjectActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    protected CompositeDisposable compositeDisposable;
    protected JWT loginToken;
    protected Activity currentActivity;


    private boolean isAlreadyOnline = true;
    private Disposable reactiveNetworkDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = onCreateGetCurrentActivity();
        compositeDisposable = new CompositeDisposable();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        obtainLoginInformation();

        reactiveNetworkDisposable = NetworkUtil.checkInternetConnection(
                isConnectedToHost -> {
                    if (isConnectedToHost)
                        online();
                    else offline();
                }
        );

        compositeDisposable.add(reactiveNetworkDisposable);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressDialog.cancel();
        compositeDisposable.remove(reactiveNetworkDisposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    protected void online() {
        if (!isAlreadyOnline)
            Toast.makeText(this, "Back online", Toast.LENGTH_SHORT).show();
        isAlreadyOnline = true;
    }

    protected void offline() {
        Toast.makeText(this, "You're offline", Toast.LENGTH_SHORT).show();
        isAlreadyOnline = false;
    }

    protected void obtainLoginInformation() {
        try {
            String token = loadToken();
            if (!TextUtils.isEmpty(token)) {
                loginToken = CommonUtil.getJWTtokenDecrypt(token);
            } else {
                DebugUtil.d("token is empty");
                if (!(currentActivity instanceof MainActivity) &&
                    !(currentActivity instanceof ForgetPasswordActivity) &&
                    !(currentActivity instanceof SplashActivity)) {
                    Toast.makeText(this, getString(R.string.error_session_not_valid), Toast.LENGTH_LONG).show();
                    doLogout();
                }
            }
        } catch (DecodeException de) {
            DebugUtil.e("token error -> " + de.getMessage(), de);
            if (!(currentActivity instanceof MainActivity) &&
                !(currentActivity instanceof ForgetPasswordActivity) &&
                !(currentActivity instanceof SplashActivity)) {
                Toast.makeText(this, getString(R.string.error_bad_token), Toast.LENGTH_LONG).show();
                doLogout();
            }
        }
    }

    protected void doLogout() {
        showMessageDialog(getString(R.string.progress_logout));
        LogoutRequestModel formBody = new LogoutRequestModel();
        formBody.setToken(loginToken.toString());
        compositeDisposable.add(
                APIHelper.logout(formBody)
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribeOn(Schedulers.io())
                         .subscribe(
                                 response -> {
                                     if (response == null) {
                                         hideDialog();
                                         throw new NullPointerException(getString(R.string.error_null_response));
                                     }

                                     if (response.getStatus() == 1) {
                                         Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                         onSuccessLogout();
                                     } else {
                                         hideDialog();
                                         Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                                     }
                                 }, error -> {
                                     hideDialog();
                                     String errorMessage = NetworkUtil.handleApiError(error);
                                     Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                                 }
                         )
        );
    }

    private void onSuccessLogout() {
        loginToken = null;
        clearToken();
        navigateTo(this, MainActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    protected String getCurrentProblemId() {
        // TODO: get current problem id change to Problem
        return Prefs.getString(getString(R.string.key_idproblem_under_solving), null);
    }

    protected void setCurrentProblemId(String idProblem) {
        // TODO: set current problem id change to Problem
        Prefs.putString(getString(R.string.key_ongoing_problem), idProblem);
    }

    protected void saveProfile(String profile) {
        Prefs.putString(getString(R.string.key_profile), profile);
    }

    protected Preference<String> loadProfile() {
        return NewtonApplication.rxSharedPreferences.getString(getString(R.string.key_profile), "");
    }

}
