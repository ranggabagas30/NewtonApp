package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.RememberPasswordRequestModel;
import com.newtonapp.data.network.pojo.response.RememberPasswordResponseModel;
import com.newtonapp.utility.NetworkUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ForgetPasswordActivity extends BaseActivity {

    private Toolbar toolbar;
    private AppCompatEditText etNIK;
    private AppCompatEditText etEmail;
    private AppCompatEditText etPhone;
    private AppCompatEditText etBirthdate;
    private AppCompatButton btnConfirm;

    private String nik;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setInitView();
        setListener();
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

    private void setInitView() {
        toolbar = findViewById(R.id.header_layout_toolbar);
        etNIK = findViewById(R.id.forgetpassword_et_nik);
        etEmail = findViewById(R.id.forgetpassword_et_email);
        etPhone = findViewById(R.id.forgetpassword_et_phone);
        etBirthdate = findViewById(R.id.forgetpassword_et_birthdate);
        btnConfirm = findViewById(R.id.forgetpassword_btn_confirm);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_forget_password);
    }

    private void setListener() {
        etNIK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nik = editable.toString();
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                phone = editable.toString();
            }
        });
        btnConfirm.setOnClickListener(view -> confirm());
    }

    private void confirm() {
        if (TextUtils.isEmpty(nik) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.error_blank_fields), Toast.LENGTH_LONG).show();
            return;
        }
        requestForgetPassword();
    }

    private void requestForgetPassword() {
        showMessageDialog(getString(R.string.progress_send_request_forget_password));
        RememberPasswordRequestModel formBody = new RememberPasswordRequestModel();
        formBody.setNik(nik);
        formBody.setHp(phone);
        compositeDisposable.add(
                APIHelper.forgetPassword(formBody)
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribeOn(Schedulers.io())
                         .subscribe(
                                 response -> {
                                     hideDialog();
                                     if (response == null) {
                                         throw new NullPointerException(getString(R.string.error_null_response));
                                     }

                                     if (response.getStatus() == 1) {
                                         Toast.makeText(this, getString(R.string.success_message_request_forget_password), Toast.LENGTH_LONG).show();
                                         onSuccessRequestForgetPassword(response);
                                     } else {
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

    private void onSuccessRequestForgetPassword(RememberPasswordResponseModel response) {
        finish();
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
