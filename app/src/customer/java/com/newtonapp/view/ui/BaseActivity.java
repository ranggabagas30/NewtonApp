package com.newtonapp.view.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.newtonapp.R;
import com.newtonapp.utility.CommonUtil;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.NetworkUtil;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends BaseProjectActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    protected CompositeDisposable compositeDisposable;
    protected Activity currentActivity;
    protected JWT loginToken;

    private boolean isAlreadyOnline = true;
    private Disposable reactiveNetworkDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

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

    protected void obtainToken(Activity activity) {
        try {
            String token = loadToken();
            if (!TextUtils.isEmpty(token)) {
                loginToken = CommonUtil.getJWTtokenDecrypt(token);
            } else {
                DebugUtil.d("null token");
            }
        } catch (DecodeException de) {
            Log.e(TAG, "token error -> " + de.getMessage(), de);
            Toast.makeText(this, getString(R.string.error_bad_token), Toast.LENGTH_LONG).show();
        }
    }


}
