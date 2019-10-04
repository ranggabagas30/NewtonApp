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

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends BaseProjectActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    protected CompositeDisposable compositeDisposable;
    protected Activity currentActivity;
    protected JWT loginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressDialog.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
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
