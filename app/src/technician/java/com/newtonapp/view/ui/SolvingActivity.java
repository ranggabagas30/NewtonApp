package com.newtonapp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;
import com.newtonapp.utility.Constants;

public class SolvingActivity extends BaseActivity {

    private static final String TAG = SolvingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private AppCompatEditText etIdCustomer;
    private AppCompatEditText etIdBarcode;
    private AppCompatEditText etNote;
    private AppCompatButton btnSolved;
    private AppCompatButton btnHold;

    private String idCustomer;
    private String idPrinter;
    private String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solving);
        initView();
        setListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportNavigateUpTo(this, DashboardActivity.class);
        return true;
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
        toolbar = findViewById(R.id.header_layout_toolbar);
        etIdCustomer = findViewById(R.id.solving_et_idcustomer);
        etIdBarcode = findViewById(R.id.solving_et_idbarcode);
        etNote = findViewById(R.id.solving_et_note);
        btnSolved = findViewById(R.id.solving_btn_solved);
        btnHold = findViewById(R.id.solving_btn_hold);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_solving);
    }

    private void setListener() {
        btnSolved.setOnClickListener(view -> solved());
        btnHold.setOnClickListener(view -> hold());
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
        etIdBarcode.addTextChangedListener(new TextWatcher() {
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
    }
    
    private void solved() {
        // TODO: add navigation method with order detail data bundle
        if (isValid()) navigateTo(this, ApprovalActivity.class);
        else Toast.makeText(this, "Please fill the empty fields", Toast.LENGTH_SHORT).show();
    }

    private void hold() {
        // TODO: add navigation method with printer's historical usage data bundle
        if (isValid()) navigateTo(this, ProductHistoryActivity.class);
        else Toast.makeText(this, "Please fill the empty fields", Toast.LENGTH_SHORT).show();
    }

    private boolean isValid() {
        if (!TextUtils.isEmpty(idCustomer) &&
            !TextUtils.isEmpty(idPrinter) &&
            !TextUtils.isEmpty(note)) return true;
        else return false;
    }
}
