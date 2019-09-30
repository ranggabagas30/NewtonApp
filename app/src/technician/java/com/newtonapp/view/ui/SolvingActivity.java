package com.newtonapp.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.KunjunganSolvingInRequestModel;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.NetworkUtil;
import com.pixplicity.easyprefs.library.Prefs;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SolvingActivity extends BaseActivity {

    private static final String TAG = SolvingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private LinearLayout llSolvingBodyKunjungan, llSolvingBodySolving;
    private AppCompatEditText etIdCustomer;
    private AppCompatEditText etIdBarcode;
    private AppCompatEditText etNote;
    private AppCompatSpinner spSolvingOption;
    private AppCompatButton btnVerify;
    private AppCompatButton btnSolved;
    private AppCompatButton btnHold;

    private boolean isKunjunganEnabled = false;
    private String idCustomer;
    private String idPrinter;
    private String idProblem;
    private String note;
    private String solvingOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solving);
        initView();
        setListener();
        checkOnGoingProblemAvailbility();
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
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
            idProblem = Prefs.getString(getString(R.string.key_idproblem_under_solving), null);
            if (TextUtils.isEmpty(idProblem)) Toast.makeText(this, getString(R.string.error_problem_not_found), Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.header_layout_toolbar);
        llSolvingBodyKunjungan = findViewById(R.id.solving_layout_body_kunjungan);
        llSolvingBodySolving = findViewById(R.id.solving_layout_body_solving);
        spSolvingOption = findViewById(R.id.solving_sp_options);
        etIdCustomer = findViewById(R.id.solving_et_idcustomer);
        etIdBarcode = findViewById(R.id.solving_et_idbarcode);
        etNote = findViewById(R.id.solving_et_note);
        btnVerify = findViewById(R.id.solving_btn_verify);
        btnSolved = findViewById(R.id.solving_btn_solved);
        btnHold = findViewById(R.id.solving_btn_hold);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_solving);

        ArrayAdapter<String> spSolvingOptionsAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, getResources().getStringArray(R.array.solving_options));
        spSolvingOption.setAdapter(spSolvingOptionsAdapter);
    }

    private void setListener() {
        btnSolved.setOnClickListener(view -> onSolved());
        btnHold.setOnClickListener(view -> onHold());
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

        spSolvingOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                solvingOption = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    private void checkOnGoingProblemAvailbility() {
        String customerJson = getOngoindCustomerProblem();
        if (!TextUtils.isEmpty(customerJson)) {
            Customer customer = new Gson().fromJson(customerJson, Customer.class);
            etIdCustomer.setText(customer.getIdCust());
            etIdBarcode.setText(customer.getProblems().get(0).getIdProduk());

            llSolvingBodySolving.setVisibility(View.VISIBLE);
            notifyDisabledInputKunjungan();
        } else {
            llSolvingBodySolving.setVisibility(View.GONE);
            notifyEnabledInputKunjungan();
        }
    }

    private void onSolved() {
        // TODO: add navigation method with order detail data bundle
        if (isValid()) {
            solved(idProblem);
        }
        else Toast.makeText(this, "Please fill the empty fields", Toast.LENGTH_SHORT).show();
    }

    private void onHold() {
        // TODO: add navigation method with printer's historical usage data bundle
        if (isValid()) navigateTo(this, ProductHistoryActivity.class);
        else Toast.makeText(this, "Please fill the empty fields", Toast.LENGTH_SHORT).show();
    }

    private void solved(String idProblem) {
        showMessageDialog(getString(R.string.progress_solved));
        KunjunganSolvingInRequestModel formBody = new KunjunganSolvingInRequestModel();
        formBody.setProb(idProblem);
        formBody.setToken(loginToken.toString());
        compositeDisposable.add(
                APIHelper.solvingIn(formBody)
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
                                        proceedSolved();
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

    private void proceedSolved() {
        finish();
    }

    private boolean isValid() {
        if (!TextUtils.isEmpty(idCustomer) &&
            !TextUtils.isEmpty(idPrinter) &&
            !TextUtils.isEmpty(solvingOption) &&
            !TextUtils.isEmpty(note)) return true;
        else {
            return false;
        }
    }

    private void notifyEnabledInputKunjungan() {
        etIdCustomer.setEnabled(true);
        etIdBarcode.setEnabled(true);
        btnVerify.setEnabled(true);
    }

    private void notifyDisabledInputKunjungan() {
        etIdCustomer.setEnabled(false);
        etIdBarcode.setEnabled(false);
        btnVerify.setEnabled(false);
    }
}
