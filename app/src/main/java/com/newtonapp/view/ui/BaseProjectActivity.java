package com.newtonapp.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.pixplicity.easyprefs.library.Prefs;

public abstract class BaseProjectActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public abstract Activity onCreateGetCurrentActivity();

    public void navigateTo(Activity activity, Class targetActivity) {
        Intent intent = new Intent(activity, targetActivity);
        startActivity(intent);
    }

    public void navigateTo(Activity activity, Class targetActivity, String bundleKey, Bundle data) {
        Intent intent = new Intent(activity, targetActivity);
        intent.putExtra(bundleKey, data);
        startActivity(intent);
    }

    public void navigateTo(Activity activity, Class targetActivity, int mode) {
        Intent intent = new Intent(activity, targetActivity);
        intent.setFlags(mode);
        startActivity(intent);
    }

    public void supportNavigateUpTo(Activity childActivity, Class parentActivityClass) {
        Intent upIntent = new Intent(childActivity, parentActivityClass);
        supportNavigateUpTo(upIntent);
    }

    public Customer getOngoindCustomerProblem() {
        String customerJson = Prefs.getString(getString(R.string.key_ongoing_problem), null);
        if (TextUtils.isEmpty(customerJson)) {
            // TODO: get ongoing problem from server
            return null;
        }
        return new Gson().fromJson(customerJson, Customer.class);
    }

    public void setOngoingCustomerProblem(Customer customer) {
        Prefs.putString(getString(R.string.key_ongoing_problem), new Gson().toJson(customer));
    }

    public void clearOngoingCustomerProblem() {
        Prefs.remove(getString(R.string.key_ongoing_problem));
    }

    public String loadToken() {
        return Prefs.getString(getString(R.string.key_token), null);
    }

    public void saveToken(String token) {
        Prefs.putString(getString(R.string.key_token), token);
    }

    public void clearToken() {
        Prefs.remove(getString(R.string.key_token));
    }
}
