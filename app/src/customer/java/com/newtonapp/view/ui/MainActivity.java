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
import com.newtonapp.data.network.pojo.request.TrackRequestModel;
import com.newtonapp.data.network.pojo.request.UpdateRequestModel;
import com.newtonapp.data.network.pojo.response.ComplainResponseModel;
import com.newtonapp.data.network.pojo.response.TrackResponseModel;
import com.newtonapp.data.network.pojo.response.UpdateResponseModel;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.NetworkUtil;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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
    private String statusComplain = "0";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initView();
        setListener();
        setTrackVerifyMode();
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
            onTrackVerify();
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
                    if (TextUtils.isEmpty(idCustomer)) {
                        Toast.makeText(this, getString(R.string.error_blank_customer_id), Toast.LENGTH_LONG).show();
                        return false;
                    }

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

        btnTracking.setOnClickListener(view -> trackComplain());
    }
    private void setDefault() {
        etIdCustomer.setText(Constants.idcustomer);
        etIdPrinter.setText(Constants.idprinter);
    }

    private void checkOnGoingProblemAvailbility() {
        /*Customer customer = getOngoindCustomerProblem();
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
        }*/
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

    private void onTrackVerify() {
        if (!TextUtils.isEmpty(idCustomer) && !TextUtils.isEmpty(idPrinter)) trackVerify();
        else Toast.makeText(this, getString(R.string.error_blank_fields), Toast.LENGTH_LONG).show();
    }

    private void trackVerify() {
        showMessageDialog(getString(R.string.progress_tracking));
        TrackRequestModel formBody = new TrackRequestModel();
        formBody.setUsername(idCustomer);
        formBody.setPassword(idPrinter);
        compositeDisposable.add(
                APIHelper.trackComplain(formBody)
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribeOn(Schedulers.io())
                         .subscribe(
                                 response -> {
                                     hideDialog();
                                     if (response == null) {
                                         throw new NullPointerException(getString(R.string.error_null_response));
                                     }

                                     if (response.getStatus() == 1 && !TextUtils.isEmpty(response.getData().getStatusComplain())) {
                                         Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                         onSuccessTrackVerify(response);
                                     } else {
                                         Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                                         setComplainMode();
                                     }
                                 }, error -> {
                                     hideDialog();
                                     String errorMessage = NetworkUtil.handleApiError(error);
                                     Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                                 }
                         )
        );
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
                                    setTrackVerifyMode();
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
        UpdateRequestModel formBody = new UpdateRequestModel();
        formBody.setUsername(idCustomer);
        formBody.setPassword(idPrinter);
        formBody.setNote(note);
        compositeDisposable.add(
                APIHelper.updateComplain(formBody)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                response -> {
                                    setTrackVerifyMode();
                                    hideDialog();
                                    if (response == null) {
                                        throw new NullPointerException(getString(R.string.error_null_response));
                                    }

                                    if (response.getStatus() == 1) {
                                        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                        onSuccessUpdate(response);
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

    private void trackComplain() {
        int currentStatusComplain = Integer.parseInt(statusComplain);
        Intent intent = new Intent(this, ProblemTrackingActivity.class);
        intent.putExtra(Constants.EXTRA_STATUS_COMPLAIN, currentStatusComplain);
        startActivity(intent);
        setTrackVerifyMode();
    }

    private void onSuccessTrackVerify(TrackResponseModel response) {
        // open, update is eligible
        saveToken(response.getToken());
        obtainToken(this);
        etNote.setText(response.getData().getNote());

        statusComplain = response.getData().getStatusComplain();
        if (Constants.FLAG_OPEN.equals(response.getData().getStatusComplain()))
            setUpdateMode();
        else if (Integer.parseInt(response.getData().getStatusComplain()) > Integer.parseInt(Constants.FLAG_OPEN)) {
            setTrackingMode();
        }
    }

    private void onSuccessComplain(ComplainResponseModel response) {

        saveToken(response.getToken());
        obtainToken(this);

        Customer customer = new Customer();
        customer.setIdCust(idCustomer);

        ArrayList<Problem> problems = new ArrayList<>();
        Problem problem = new Problem();
        problem.setIdProblem(response.getData().getIdProblem());
        problem.setIdProduk(loginToken.getClaim(Constants.CLAIM_SN).asString());
        problem.setNote(note);
        problem.setStatusComplain(response.getData().getStatusComplain());
        problem.setOtp(loginToken.getClaim(Constants.CLAIM_OTP).asString());
        problem.setWaktuComp(response.getData().getWaktuComp());
        problems.add(problem);

        customer.setProblems(problems);

        setOngoingCustomerProblem(customer);
        //setUpdateMode();
    }

    private void onSuccessUpdate(UpdateResponseModel response) {
        // update pref
        saveToken(response.getToken());
        obtainToken(this);
    }

    private void setTrackVerifyMode() {
        statusComplain = "0";
        etIdCustomer.setText("");
        etIdPrinter.setText("");
        etNote.setText("");
        notifyVerifyEnabled();
        notifyNoteDisabled();
        btnSendComplain.setVisibility(View.VISIBLE);
        btnSendComplain.setEnabled(false);
        btnUpdateComplain.setVisibility(View.GONE);
        btnTracking.setVisibility(View.GONE);
    }

    private void setComplainMode() {
        notifyVerifyEnabled();
        notifyNoteEnabled();
        btnSendComplain.setVisibility(View.VISIBLE);
        btnSendComplain.setEnabled(true);
        btnUpdateComplain.setVisibility(View.GONE);
        btnTracking.setVisibility(View.GONE);
    }

    private void setUpdateMode() {
        notifyVerifyEnabled();
        notifyNoteEnabled();
        btnSendComplain.setVisibility(View.GONE);
        btnUpdateComplain.setVisibility(View.VISIBLE);
        btnTracking.setVisibility(View.VISIBLE);
    }

    private void setTrackingMode() {
        notifyVerifyEnabled();
        notifyNoteDisabled();
        btnSendComplain.setVisibility(View.GONE);
        btnUpdateComplain.setVisibility(View.VISIBLE);
        btnUpdateComplain.setEnabled(false);
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
