package com.newtonapp.view.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.newtonapp.utility.CommonUtil;
import com.pixplicity.easyprefs.library.Prefs;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private Activity currentActivity;
    private ProgressDialog progressDialog;
    protected CompositeDisposable compositeDisposable;
    protected JWT loginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = onCreateGetCurrentActivity();
        if (!(currentActivity instanceof LoginActivity) &&
            !(currentActivity instanceof ForgetPasswordActivity))
             obtainLoginInformation(this);
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

    public abstract Activity onCreateGetCurrentActivity();

    protected void navigateTo(Activity activity, Class targetActivity) {
        Intent intent = new Intent(activity, targetActivity);
        startActivity(intent);
    }

    protected void navigateTo(Activity activity, Class targetActivity, String bundleKey, Bundle data) {
        Intent intent = new Intent(activity, targetActivity);
        intent.putExtra(bundleKey, data);
        startActivity(intent);
    }

    protected void navigateTo(Activity activity, Class targetActivity, int mode) {
        Intent intent = new Intent(activity, targetActivity);
        intent.setFlags(mode);
        startActivity(intent);
    }

    protected void supportNavigateUpTo(Activity childActivity, Class parentActivityClass) {
        Intent upIntent = new Intent(childActivity, parentActivityClass);
        supportNavigateUpTo(upIntent);
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

    protected void obtainLoginInformation(Activity activity) {
        try {
            String token = Prefs.getString(activity.getString(R.string.key_token), null);
            if (!TextUtils.isEmpty(token)) {
                loginToken = CommonUtil.getJWTtokenDecrypt(token);
            } else {
                Toast.makeText(this, getString(R.string.error_session_not_valid), Toast.LENGTH_LONG).show();
                doLogout();
            }
        } catch (DecodeException de) {
            Log.e(TAG, "token error -> " + de.getMessage(), de);
            Toast.makeText(this, getString(R.string.error_bad_token), Toast.LENGTH_LONG).show();
            doLogout();
        }
    }

    protected void doLogout() {
        Prefs.remove(getString(R.string.key_token));
        navigateTo(this, LoginActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    protected String getCurrentProblemId() {
        // TODO: get current problem id change to Problem
        return Prefs.getString(getString(R.string.key_idproblem_under_solving), null);
    }

    protected void setCurrentProblemId(String idProblem) {
        // TODO: set current problem id change to Problem
        Prefs.putString(getString(R.string.key_ongoing_problem), idProblem);
    }

    protected Customer getOngoindCustomerProblem() {
        String customerJson = Prefs.getString(getString(R.string.key_ongoing_problem), null);
        if (TextUtils.isEmpty(customerJson)) {
            // TODO: get ongoing problem from server
            return null;
        }
        return new Gson().fromJson(customerJson, Customer.class);
    }

    protected void setOngoingCustomerProblem(Customer customer) {
        Prefs.putString(getString(R.string.key_ongoing_problem), new Gson().toJson(customer));
    }

    protected void clearOngoingCustomerProblem() {
        Prefs.remove(getString(R.string.key_ongoing_problem));
    }
}
