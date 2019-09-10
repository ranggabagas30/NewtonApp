package com.newtonapp.view.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.newtonapp.R;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.ComplainRequestModel;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.DateTimeUtil;
import com.pixplicity.easyprefs.library.Prefs;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VerificationActivity extends BaseActivity {

    private static final String TAG = VerificationActivity.class.getSimpleName();
    private AppCompatTextView tvTitle;
    private AppCompatTextView tvProblemTrackingId;
    private AppCompatTextView tvProblemTrackingNote;
    private AppCompatTextView tvProblemTrackingTime;
    private AppCompatEditText etIdCustomer;
    private AppCompatEditText etIdBarcode;
    private AppCompatEditText etNote;
    private AppCompatButton btnConfirm;
    private AppCompatButton btnUpdate;
    private AppCompatButton btnTracking;
    private RelativeLayout layoutProblemTracking;
    private LinearLayout layoutUpdateTracking;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initView();
        //setDefault();

        etIdCustomer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        if (Prefs.getInt("status", 0) == 1) {
            layoutProblemTracking.setVisibility(View.VISIBLE);
            layoutUpdateTracking.setVisibility(View.VISIBLE);
        }

        etIdBarcode.setOnTouchListener((view, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (etIdBarcode.getRight() - etIdBarcode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    Intent openCameraIntent = new Intent(this, CameraPreviewActivity.class);
                    startActivityForResult(openCameraIntent, Constants.RC_SCAN_BARCODE);
                    return true;
                }
            }
            return false;
        });

        btnConfirm.setOnClickListener(view -> {
            sendComplain();
        });

        btnUpdate.setOnClickListener(view -> {
            updateComplain();
        });

        btnTracking.setOnClickListener(view -> navigateTo(VerificationActivity.this, ProblemTrackingActivity.class));
        tvTitle.setText("Verification");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String barcodeValue = data != null ? data.getStringExtra(Constants.EXTRA_INTENT_RESULT_SCAN) : null;

        Log.d(TAG, "requestCode : " + requestCode);
        Log.d(TAG, "resultCode : " + resultCode);
        Log.d(TAG, "data barcode : " + barcodeValue);

        if (requestCode == Constants.RC_SCAN_BARCODE && resultCode == RESULT_OK) {
            etIdBarcode.setText(barcodeValue);
        }
    }

    private void initView() {
        tvTitle                 = findViewById(R.id.header_tv_title);
        tvProblemTrackingId     = findViewById(R.id.verification_tv_value_problemid);
        tvProblemTrackingNote   = findViewById(R.id.verification_tv_value_problemnote);
        tvProblemTrackingTime   = findViewById(R.id.verification_tv_value_problemtime);
        etIdCustomer            = findViewById(R.id.verification_et_idcustomer);
        etIdBarcode             = findViewById(R.id.verification_et_idbarcode);
        etNote                  = findViewById(R.id.verification_et_note);
        btnConfirm              = findViewById(R.id.verification_btn_confirm);
        btnUpdate               = findViewById(R.id.verification_btn_update);
        btnTracking             = findViewById(R.id.verification_btn_tracking);
        layoutProblemTracking   = findViewById(R.id.verification_layout_problem_tracking);
        layoutUpdateTracking    = findViewById(R.id.verification_layout_update_tracking);
    }

    private void setDefault() {
        tvTitle.setText("Complain");
        etIdCustomer.setText("NWT00085");
        etIdBarcode.setText("6Qv6Ui9ZZHXqHQjWOFw0fMa0I04dBwnJRRE+TyqWB/c=");
        etNote.setText("printernya Gak bisa hidup");
    }

    @SuppressLint("CheckResult")
    private void sendComplain() {

        String act      = "cmp";
        String username = etIdCustomer.getText().toString();
        String password = etIdBarcode.getText().toString();
        String category = "customer";
        String note     = etNote.getText().toString();

        ComplainRequestModel complainBody = new ComplainRequestModel(
                act,
                username,
                password,
                category,
                note
        );

        showMessageDialog("Sending complain and verifying");
        APIHelper.sendComplain(complainBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        result -> {
                            Log.d(TAG, "== complain response ==");
                            Log.d(TAG, "ack : " + result.getAck());
                            Log.d(TAG, "token: " + result.getToken());
                            Log.d(TAG, "message: " + result.getMessage());
                            Log.d(TAG, "status: " + result.getStatus());

                            switch (result.getStatus()) {
                                case 0 : // failed
                                    Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show(); break;
                                case 1 : // success
                                    Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
                                    Prefs.putString("idcustomer", etIdCustomer.getText().toString());
                                    Prefs.putString("idprinter", etIdBarcode.getText().toString());
                                    Prefs.putString("note", etNote.getText().toString());
                                    Prefs.putInt("status", result.getStatus());
                                    updateProblemTracking("#unknown", note, DateTimeUtil.getCurrentDate());
                                    layoutProblemTracking.setVisibility(View.VISIBLE);
                                    layoutUpdateTracking.setVisibility(View.VISIBLE);
                                default:
                                    Log.e(TAG, "sendComplain: unknown status"); break;
                            }
                        },
                        error -> {
                            hideDialog();
                            Log.e(TAG, "sendComplain: ", error);
                            Toast.makeText(this, "Failed to make complain", Toast.LENGTH_LONG).show();
                        },
                        () -> {
                            hideDialog();
                            Log.d(TAG, "sendComplain: complete");
                            //Toast.makeText(LoginActivity.this, "Send complain complete", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    private void updateComplain() {
        etIdCustomer.setText(Prefs.getString("idcustomer", ""));
        etIdBarcode.setText(Prefs.getString("idprinter", ""));
        etNote.setText(Prefs.getString("note", ""));
    }

    private void updateProblemTracking(String id, String note, String time) {
        tvProblemTrackingId.setText(id);
        tvProblemTrackingNote.setText(note);
        tvProblemTrackingTime.setText(time);
    }
}
