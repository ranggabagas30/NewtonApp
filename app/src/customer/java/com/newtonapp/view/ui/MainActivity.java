package com.newtonapp.view.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.BuildConfig;
import com.newtonapp.R;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.ComplainRequestModel;
import com.newtonapp.data.network.pojo.request.TrackRequestModel;
import com.newtonapp.data.network.pojo.request.UpdateRequestModel;
import com.newtonapp.data.network.pojo.response.ComplainResponseModel;
import com.newtonapp.data.network.pojo.response.TrackResponseModel;
import com.newtonapp.data.network.pojo.response.UpdateResponseModel;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.NetworkUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private LinearLayout llVerification;
    private LinearLayout llNote;
    private LinearLayout llFailedBody;
    private AppCompatEditText etIdCustomer;
    private AppCompatEditText etIdPrinter;
    private AppCompatEditText etNote;
    private AppCompatButton btnSendComplain;
    private AppCompatButton btnUpdateComplain;
    private AppCompatButton btnTracking;
    private AppCompatImageButton imgBtnScan;
    private AppCompatImageView ivFailedLogo;
    private AppCompatTextView tvFailedMessage;
    private AppCompatTextView tvOTP;

    private String idCustomer;
    private String idPrinter;
    private String note;
    private String statusComplain = Constants.FLAG_OPEN;
    private String otp;
    private String OTP_CURRENT_LABEL;

    private static final String LABEL_OTP_HIDDEN = "Lihat kode OTP";
    private static final String LABEL_OTP_VISIBLE = "Kode OTP ";
    private static final int OTP_VISIBLE_COUNT_DOWN = 4; // seconds;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initView();
        setListener();
        setVerifyMode();
    }

    @Override
    protected void online() {
        super.online();
        setOnlineMode();
    }

    @Override
    protected void offline() {
        super.offline();
        setOfflineMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            getMenuInflater().inflate(R.menu.verification_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            switch (item.getItemId()) {
                case R.id.verification_menu_debug:
                    navigateTo(this, DebugActivity.class);
                    return true;
            }
        }
        return false;
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
        tvOTP = findViewById(R.id.verification_tv_otp);
        llVerification = findViewById(R.id.verification_layout_verify);
        llNote = findViewById(R.id.verification_layout_note);
        llFailedBody = findViewById(R.id.failed_layout_body);
        etIdCustomer = findViewById(R.id.verification_et_idcustomer);
        etIdPrinter = findViewById(R.id.verification_et_idprinter);
        etNote = findViewById(R.id.verification_et_note);
        btnSendComplain = findViewById(R.id.verification_btn_submit);
        btnUpdateComplain = findViewById(R.id.verification_btn_update);
        btnTracking = findViewById(R.id.verification_btn_tracking);
        imgBtnScan = findViewById(R.id.verification_imgbtn_camera);
        ivFailedLogo = findViewById(R.id.body_iv_failed_logo);
        tvFailedMessage = findViewById(R.id.body_tv_failed_text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.screen_verification));
        setBlockMode();

        OTP_CURRENT_LABEL = LABEL_OTP_HIDDEN;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {

        /*tvOTP.setOnClickListener(view -> {
            if (TextUtils.isEmpty(otp)) { // OTP kosong, harus scan lagi

            }
        });*/
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
        imgBtnScan.setOnClickListener(view -> onOpeningCameraStartScanning());
    }

    private boolean isVerificationFieldNotBlank() {
        return !TextUtils.isEmpty(idCustomer) &&
                !TextUtils.isEmpty(idPrinter);
    }

    private boolean isValid() {
        return  isVerificationFieldNotBlank()&&
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
        if (isVerificationFieldNotBlank()) trackVerify();
        else Toast.makeText(this, getString(R.string.error_blank_fields), Toast.LENGTH_LONG).show();
    }

    private void onOpeningCameraStartScanning() {
        if (TextUtils.isEmpty(idCustomer)) {
            Toast.makeText(this, getString(R.string.error_blank_customer_id), Toast.LENGTH_LONG).show();
            return;
        }
        Intent openCameraIntent = new Intent(this, CameraPreviewActivity.class);
        startActivityForResult(openCameraIntent, Constants.RC_SCAN_BARCODE);
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

                                     DebugUtil.d("verification response: " + response.toString());
                                     if (response.getStatus() == 1) {
                                         Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                                         onSuccessTrackVerify(response);
                                     } else {
                                         //Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                                         if (response.getAck().equalsIgnoreCase(Constants.ACK_CONTRACT_NOT_FOUND)) {
                                             setComplainMode();
                                         }
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
                                    setVerifyMode();
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
                                    setVerifyMode();
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
        setVerifyMode();
    }

    private void onSuccessTrackVerify(TrackResponseModel response) {
        saveToken(response.getToken());
        obtainToken(this);
        otp = loginToken.getClaim(Constants.CLAIM_OTP).asString();
        statusComplain = response.getData().getStatusComplain();
        if (!TextUtils.isEmpty(statusComplain)) {
            etNote.setText(response.getData().getNote());
            if (Constants.FLAG_OPEN.equals(statusComplain))
                setUpdateMode();
            else if (Integer.parseInt(statusComplain) > Integer.parseInt(Constants.FLAG_OPEN)) {
                setTrackingMode();
            }
        } else {
            setComplainMode();
        }
    }

    private void onSuccessComplain(ComplainResponseModel response) {
        saveToken(response.getToken());
        obtainToken(this);
    }

    private void onSuccessUpdate(UpdateResponseModel response) {
        saveToken(response.getToken());
        obtainToken(this);
    }

    private void startOTPCountDown() {
        compositeDisposable.add(
                Completable.timer(OTP_VISIBLE_COUNT_DOWN, TimeUnit.SECONDS)
                            .subscribe(
                                    () -> {
                                        // otp visible time out, hide it

                                    }
                            )
        );
    }

    private void setFailedMode() {
        llFailedBody.setVisibility(View.VISIBLE);
        llFailedBody.setOnTouchListener((view, motionEvent) -> {
            return true;
        });
    }

    private void setNormalMode() {
        llFailedBody.setVisibility(View.GONE);
        llFailedBody.setOnTouchListener((view, motionEvent) -> {
            return false;
        });
    }

    private void setOfflineMode() {
        setFailedMode();
        ivFailedLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_network_unavailable));
        tvFailedMessage.setText(getString(R.string.error_no_internet_connection));
    }

    private void setOnlineMode() {
        setNormalMode();
    }

    private void setBlockMode() {
        notifyVerifyDisabled();
        notifyNoteDisabled();
    }

    private void setVerifyMode() {
        statusComplain = Constants.FLAG_OPEN;
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
        //etIdPrinter.setEnabled(true);
    }

    private void notifyVerifyDisabled() {
        etIdCustomer.setEnabled(false);
        //etIdPrinter.setEnabled(false);
    }

    private void notifyNoteEnabled() {
        etNote.setEnabled(true);
    }

    private void notifyNoteDisabled() {
        etNote.setEnabled(false);
    }
}
