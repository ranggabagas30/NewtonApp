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

import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.newtonapp.data.database.entity.Problem;
import com.newtonapp.data.database.entity.Solving;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.HoldRequestModel;
import com.newtonapp.data.network.pojo.request.KunjunganRequestModel;
import com.newtonapp.data.network.pojo.request.SolvedRequestModel;
import com.newtonapp.utility.CommonUtil;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.NetworkUtil;

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
    private String solvingNote;
    private String solvingOption;
    private String[] arrSolvingOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solving);
        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        String qrcodeValue = data != null ? data.getStringExtra(Constants.EXTRA_INTENT_RESULT_SCAN) : null;

        Log.d(TAG, "requestCode : " + requestCode);
        Log.d(TAG, "resultCode : " + resultCode);
        Log.d(TAG, "data barcode : " + qrcodeValue);
        if (requestCode == Constants.RC_SCAN_BARCODE && resultCode == RESULT_OK) {
            etIdBarcode.setText(qrcodeValue);
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

        arrSolvingOptions = getResources().getStringArray(R.array.solving_options);
        ArrayAdapter<String> spSolvingOptionsAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, arrSolvingOptions);
        spSolvingOption.setAdapter(spSolvingOptionsAdapter);
    }

    private void setListener() {
        btnVerify.setOnClickListener(view -> onVerify());
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
                solvingNote = editable.toString();
            }
        });
    }

    private void checkOnGoingProblemAvailbility() {
        Customer customer = getOngoindCustomerProblem();
        if (customer != null) {
            Problem problem = customer.getProblems().get(0);
            if (problem != null) {
                etIdCustomer.setText(customer.getIdCust());
                etIdBarcode.setText(problem.getIdProduk());
                Solving solving = problem.getSolving();
                if (solving != null) {
                    int posSolvingOption = CommonUtil.getIndex(solving.getSolvingOption(), arrSolvingOptions);
                    spSolvingOption.setSelection(posSolvingOption);
                    etNote.setText(problem.getSolving().getSolvingNote());
                }
                switch (problem.getStatusComplain()) {
                    case Constants.FLAG_START_PROGRESS:
                        setKunjunganMode();
                        break;
                    case Constants.FLAG_KUNJUNGAN:
                        setSolvingMode();
                        break;
                    case Constants.FLAG_HOLD:
                        setSolvingMode();
                        break;
                    case Constants.FLAG_SOLVED:
                        navigateTo(this, ApprovalActivity.class);
                        finish();
                        break;
                    default:
                        setKunjunganMode();
                        break;
                }
            }
        } else {
             Toast.makeText(this, getString(R.string.error_problem_not_found), Toast.LENGTH_LONG).show();
             setBlockMode();
        }
    }

    private void loadData(Customer customer) {

    }

    private void onVerify() {
        if (!TextUtils.isEmpty(idCustomer) && !TextUtils.isEmpty(idPrinter)) {
            verify();
        } else Toast.makeText(this, getString(R.string.error_blank_fields), Toast.LENGTH_LONG).show();
    }
    private void onSolved() {
        // TODO: add navigation method with order detail data bundle
        if (isValid()) {
            solved(idCustomer, idPrinter, solvingOption, solvingNote);
        }
        else Toast.makeText(this, getString(R.string.error_blank_fields), Toast.LENGTH_LONG).show();
    }

    private void onHold() {
        // TODO: add navigation method with printer's historical usage data bundle
        if (isValid()) {
            hold(idCustomer, idPrinter, solvingOption, solvingNote);
        }
        else Toast.makeText(this, getString(R.string.error_blank_fields), Toast.LENGTH_LONG).show();
    }

    private void verify() {
        Customer customer = getOngoindCustomerProblem();
        if (customer != null) {
            showMessageDialog(getString(R.string.progress_verifying_kunjungan));
            KunjunganRequestModel formBody = new KunjunganRequestModel();
            formBody.setToken(loginToken.toString());
            formBody.setProb(customer.getProblems().get(0).getIdProblem());
            compositeDisposable.add(
                    APIHelper.kunjunganSolvingin(formBody)
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
                                            proceedKunjungan(customer);
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
    }

    private void solved(String idCustomer, String idPrinter, String solvingOption, String solvingNote) {
        Customer customer = getOngoindCustomerProblem();
        if (customer != null) {
            showMessageDialog(getString(R.string.progress_solved));
            SolvedRequestModel formBody = new SolvedRequestModel();
            formBody.setProb(customer.getProblems().get(0).getIdProblem());
            formBody.setIdCust(idCustomer);
            formBody.setSn(idPrinter);
            formBody.setSolveOpt(solvingOption);
            formBody.setNote(solvingNote);
            formBody.setToken(loginToken.toString());
            compositeDisposable.add(
                    APIHelper.solved(formBody)
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
                                            Solving solving = new Solving();
                                            solving.setSolvingOption(solvingOption);
                                            solving.setSolvingNote(solvingNote);
                                            proceedSolved(customer, solving);
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
    }

    private void hold(String idCustomer, String idPrinter, String solvingOption, String solvingNote) {
        Customer customer = getOngoindCustomerProblem();
        if (customer != null) {
            showMessageDialog(getString(R.string.progress_hold));
            HoldRequestModel formBody = new HoldRequestModel();
            formBody.setProb(customer.getProblems().get(0).getIdProblem());
            formBody.setIdCust(idCustomer);
            formBody.setSn(idPrinter);
            formBody.setSolveOpt(solvingOption);
            formBody.setNote(solvingNote);
            formBody.setToken(loginToken.toString());
            compositeDisposable.add(
                    APIHelper.hold(formBody)
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
                                             Solving solving = new Solving();
                                             solving.setSolvingOption(solvingOption);
                                             solving.setSolvingNote(solvingNote);
                                             proceedHold(customer, solving);
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
    }

    private void proceedKunjungan(Customer customer) {
        customer.getProblems().get(0).setStatusComplain(Constants.FLAG_KUNJUNGAN);
        setOngoingCustomerProblem(customer);
        setSolvingMode();
    }

    private void proceedSolved(Customer customer, Solving solving) {
        customer.getProblems().get(0).setSolving(solving);
        customer.getProblems().get(0).setStatusComplain(Constants.FLAG_SOLVED);
        setOngoingCustomerProblem(customer);
        navigateTo(this, ApprovalActivity.class);
        finish();
    }

    private void proceedHold(Customer customer, Solving solving) {
        customer.getProblems().get(0).setSolving(solving);
        customer.getProblems().get(0).setStatusComplain(Constants.FLAG_HOLD);
        setOngoingCustomerProblem(customer);
        finish(); // back to main menu
    }

    private boolean isValid() {
        return !TextUtils.isEmpty(idCustomer) &&
                !TextUtils.isEmpty(idPrinter) &&
                !TextUtils.isEmpty(solvingOption) &&
                !TextUtils.isEmpty(solvingNote);
    }

    private void setBlockMode() {
        llSolvingBodySolving.setVisibility(View.GONE);
        notifyDisabledInputKunjungan();
    }

    private void setKunjunganMode() {
        llSolvingBodySolving.setVisibility(View.GONE);
        notifyEnabledInputKunjungan();
    }

    private void setSolvingMode() {
        llSolvingBodySolving.setVisibility(View.VISIBLE);
        notifyDisabledInputKunjungan();
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
