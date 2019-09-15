package com.newtonapp.view.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;

public class SolvingActivity extends BaseActivity {

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
        toolbar = findViewById(R.id.header_layout_toolbar);
        etIdCustomer = findViewById(R.id.solving_et_idcustomer);
        etIdBarcode = findViewById(R.id.solving_et_idbarcode);
        etNote = findViewById(R.id.solving_et_note);
        btnSolved = findViewById(R.id.solving_btn_solved);
        btnHold = findViewById(R.id.solving_btn_hold);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onSupportNavigateUp() {
        supportNavigateUpTo(this, DashboardActivity.class);
        return true;
    }

    private void solved() {
        if (isValid()) navigateTo(this, ApprovalActivity.class);
        else Toast.makeText(this, "Please fill the empty fields", Toast.LENGTH_SHORT).show();
    }

    private void hold() {

    }

    private boolean isValid() {
        if (!TextUtils.isEmpty(idCustomer) &&
            !TextUtils.isEmpty(idPrinter) &&
            !TextUtils.isEmpty(note)) return true;
        else return false;
    }
}
