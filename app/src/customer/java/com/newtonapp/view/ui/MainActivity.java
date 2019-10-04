package com.newtonapp.view.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.newtonapp.data.database.entity.Problem;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.ComplainRequestModel;
import com.newtonapp.data.network.pojo.response.ComplainResponseModel;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.DateTimeUtil;
import com.newtonapp.utility.NetworkUtil;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VerificationActivity extends BaseActivity {

    private static final String TAG = VerificationActivity.class.getSimpleName();

    private Toolbar toolbar;
    private LinearLayout llVerification;
    private LinearLayout llNote;
    private AppCompatEditText etIdCustomer;
    private AppCompatEditText etIdPrinter;
    private AppCompatEditText etNote;
    private AppCompatButton btnSendComplain;
    private AppCompatButton btnUpdateComplain;
    private AppCompatButton btnTracking;

    private String idCustomer;
    private String idPrinter;
    private String note;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initView();
        setListener();
        //setDefault();
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkOnGoingProblemAvailbility();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String qrcodeValue = data != null ? data.getStringExtra(Constants.EXTRA_INTENT_RESULT_SCAN) : null;

        Log.d(TAG, "requestCode : " + requestCode);
        Log.d(TAG, "resultCode : " + resultCode);
        Log.d(TAG, "data barcode : " + qrcodeValue);

        if (requestCode == Constants.RC_SCAN_BARCODE && resultCode == RESULT_OK) {
            etIdPrinter.setText(qrcodeValue);
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.header_layout_toolbar);
        llVerification = findViewById(R.id.verification_layout_verify);
        llNote = findViewById(R.id.verification_layout_note);
        etIdCustomer = findViewById(R.id.verification_et_idcustomer);
        etIdPrinter = findViewById(R.id.verification_et_idprinter);
        etNote = findViewById(R.id.verification_et_note);
        btnSendComplain = findViewById(R.id.verification_btn_submit);
        btnUpdateComplain = findViewById(R.id.verification_btn_update);
        btnTracking = findViewById(R.id.verification_btn_tracking);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.screen_verification));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        etIdPrinter.setOnTouchListener((view, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (etIdPrinter.getRight() - etIdPrinter.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    Intent openCameraIntent = new Intent(this, CameraPreviewActivity.class);
                    startActivityForResult(openCameraIntent, Constants.RC_SCAN_BARCODE);
                    return true;
                }
            }
            return false;
        });
        etIdCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                idCustomer = editable.toString();
            }
        });
        etIdPrinter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                idPrinter = editable.toString();
            }
        });
        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                note = editable.toString();
            }
        });
        btnSendComplain.setOnClickListener(view -> {
            onComplain();
        });

        btnUpdateComplain.setOnClickListener(view -> {
            onUpdate();
        });

        btnTracking.setOnClickListener(view -> navigateTo(VerificationActivity.this, ProblemTrackingActivity.class));
    }
    private void setDefault() {
        etIdCustomer.setText(Constants.idcustomer);
        etIdPrinter.setText(Constants.idprinter);
    }

    private void checkOnGoingProblemAvailbility() {
        Customer customer = getOngoindCustomerProblem();
        if (customer != null) {
            Problem problem = customer.getProblems().get(0);
            if (problem != null) {
                etIdCustomer.setText(customer.getIdCust());
                etIdPrinter.setText(loginToken.getClaim(Constants.CLAIM_SN).asString());
                etNote.setText(problem.getNote());
                switch (problem.getStatusComplain()) {
                    case Constants.FLAG_OPEN:
                        setUpdateMode();
                        break;
                    default:
                        setTrackingMode();
                        break;
                }
            }
        } else {
            setComplainMode();
        }
    }
    private boolean isValid() {
        return !TextUtils.isEmpty(idCustomer) &&
                !TextUtils.isEmpty(idPrinter) &&
                !TextUtils.isEmpty(note);
    }

    private void onComplain() {
        if (isValid()) sendComplain();
        else Toast.makeText(this, getString(R.string.error_blank_fields), Toast.LENGTH_LONG).show();
    }

    private void onUpdate() {
        if (isValid()) updateComplain();
        else Toast.makeText(this, getString(R.string.error_blank_fields), Toast.LENGTH_LONG).show();
    }

    private void onTracking() {

    }

    @SuppressLint("CheckResult")
    private void sendComplain() {
        showMessageDialog(getString(R.string.progress_send_complain));
        ComplainRequestModel formBody = new ComplainRequestModel();
        formBody.setUsername(idCustomer);
        formBody.setPassword(idPrinter);
        formBody.setNote(note);
        compositeDisposable.add(
                APIHelper.sendComplain(formBody)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                response -> {
                                    hideDialog();
                                    if (response == null) {
                                        throw new NullPointerException(getString(R.string.error_null_response));
                                    }

                                    if (response.getStatus() == 1) {
                                        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                        onSuccessComplain(response);
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

    private void updateComplain() {
        showMessageDialog(getString(R.string.progress_update_complain));

    }

    private void onSuccessComplain(ComplainResponseModel response) {
        Customer customer = new Customer();
        customer.setIdCust(idCustomer);
        ArrayList<Problem> problems = new ArrayList<>();
        Problem problem = new Problem();
        problem.setIdProduk(Constants.idprinter);
        problem.setNote(note);
        problem.setStatusComplain(Constants.FLAG_OPEN);
        problem.setOtp(loginToken.getClaim(Constants.CLAIM_OTP).asString());
        problem.setWaktuComp(DateTimeUtil.getCurrentDate(DateTimeUtil.DATE_TIME_PATTERN_2));
        problems.add(problem);
        customer.setProblems(problems);
        setOngoingCustomerProblem(customer);
    }

    private void setComplainMode() {
        notifyVerifyEnabled();
        notifyNoteEnabled();
        btnSendComplain.setVisibility(View.VISIBLE);
        btnUpdateComplain.setVisibility(View.GONE);
        btnTracking.setVisibility(View.GONE);
    }

    private void setUpdateMode() {
        notifyVerifyDisabled();
        notifyNoteEnabled();
        btnSendComplain.setVisibility(View.GONE);
        btnUpdateComplain.setVisibility(View.VISIBLE);
        btnTracking.setVisibility(View.GONE);
    }

    private void setTrackingMode() {
        notifyVerifyDisabled();
        notifyNoteDisabled();
        btnSendComplain.setVisibility(View.GONE);
        btnUpdateComplain.setVisibility(View.GONE);
        btnTracking.setVisibility(View.VISIBLE);
    }

    private void notifyVerifyEnabled() {
        etIdCustomer.setEnabled(true);
        etIdPrinter.setEnabled(true);
    }

    private void notifyVerifyDisabled() {
        etIdCustomer.setEnabled(false);
        etIdPrinter.setEnabled(false);
    }

    private void notifyNoteEnabled() {
        etNote.setEnabled(true);
    }

    private void notifyNoteDisabled() {
        etNote.setEnabled(false);
    }
}
