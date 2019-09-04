package com.newtonapp.view.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.newtonapp.customer.R;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.ComplainRequestModel;
import com.newtonapp.utility.Constants;
import com.pixplicity.easyprefs.library.Prefs;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AppCompatTextView tvTitle;
    private AppCompatEditText etIdCustomer;
    private AppCompatEditText etIdBarcode;
    private AppCompatEditText etNote;
    private AppCompatButton btnConfirm;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int status = Prefs.getInt("status", 0);
        if (status == 1) navigateTo(this, VerificationActivity.class);

        initView();
        setDefault();

        etIdBarcode.setOnTouchListener((view, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (etIdBarcode.getRight() - etIdBarcode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    Intent openCameraIntent = new Intent(MainActivity.this, CameraPreviewActivity.class);
                    startActivityForResult(openCameraIntent, Constants.RC_SCAN_BARCODE);
                    return true;
                }
            }
            return false;
        });

        //btnConfirm.setOnClickListener(view -> navigateTo(MainActivity.this, VerificationActivity.class));
        btnConfirm.setOnClickListener(view -> sendComplain());

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
        tvTitle         = findViewById(R.id.header_tv_title);
        etIdCustomer    = findViewById(R.id.main_et_idcustomer);
        etIdBarcode     = findViewById(R.id.main_et_idbarcode);
        etNote          = findViewById(R.id.main_et_note);
        btnConfirm      = findViewById(R.id.main_btn_confirm);
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
        String message  = etNote.getText().toString();

        ComplainRequestModel complainBody = new ComplainRequestModel(
                act,
                username,
                password,
                category,
                message
        );

        APIHelper.sendComplain(complainBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        result -> {
                            Log.d(TAG, "complain response: " + result.toString());
                            Log.d(TAG, "ack : " + result.getAck());
                            Log.d(TAG, "token: " + result.getToken());
                            Log.d(TAG, "message: " + result.getMessage());
                            Log.d(TAG, "status: " + result.getStatus());

                            switch (result.getStatus()) {
                                case 0 :
                                    Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_LONG).show(); break;
                                case 1 :
                                    Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                                    Prefs.putString("idcustomer", etIdCustomer.getText().toString());
                                    Prefs.putString("idprinter", etIdBarcode.getText().toString());
                                    Prefs.putInt("status", result.getStatus());
                                    navigateTo(MainActivity.this, VerificationActivity.class); break;
                                default:
                                    Log.e(TAG, "sendComplain: unknown status"); break;
                            }
                        },
                        error -> Log.e(TAG, "sendComplain: ", error),
                        () -> {
                            Log.d(TAG, "sendComplain: complete");
                            //Toast.makeText(MainActivity.this, "Send complain complete", Toast.LENGTH_SHORT).show();
                        }
                );
    }
}
