package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.newtonapp.data.database.entity.Problem;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.ApprovalRequestModel;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.NetworkUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ApprovalActivity extends BaseActivity {

    private Toolbar toolbar;
    private AppCompatTextView tvCustomerId;
    private AppCompatTextView tvPrinterId;
    private AppCompatTextView tvTechId;
    private AppCompatTextView tvReportDate;
    private AppCompatTextView tvReportTime;
    private AppCompatTextView tvContractNumber;
    private AppCompatTextView tvProduct;
    private AppCompatTextView tvSolvingOption;
    private AppCompatTextView tvSolvingNote;
    private AppCompatEditText etCustomerOtp;
    private AppCompatButton btnApproval;
    private MaterialRatingBar rbSolvingRate;

    private String customerOTP;
    private String problemRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
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

    private void initView() {
        toolbar = findViewById(R.id.header_layout_toolbar);
        tvCustomerId = findViewById(R.id.approval_tv_customer_value);
        tvPrinterId = findViewById(R.id.approval_tv_idprinter_value);
        tvTechId = findViewById(R.id.approval_tv_idtechnician_value);
        tvReportDate = findViewById(R.id.approval_tv_tglreport_value);
        tvReportTime = findViewById(R.id.approval_tv_waktu_value);
        tvContractNumber = findViewById(R.id.approval_tv_contract_value);
        tvProduct = findViewById(R.id.approval_tv_product_value);
        tvSolvingOption = findViewById(R.id.approval_tv_aktivitas_value);
        tvSolvingNote = findViewById(R.id.approval_tv_result_value);
        rbSolvingRate = findViewById(R.id.approval_rb_rate_value);
        etCustomerOtp = findViewById(R.id.approval_et_customerotp);
        btnApproval = findViewById(R.id.approval_btn_approve);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_approval);
    }

    private void setListener() {
        btnApproval.setOnClickListener(view -> onApprove());
        rbSolvingRate.setOnRatingChangeListener((ratingBar, rating) -> problemRate = String.valueOf((int) rating));
        etCustomerOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                customerOTP = editable.toString();
            }
        });
    }

    private void checkOnGoingProblemAvailbility() {
        Customer customer = getOngoindCustomerProblem();
        if (customer != null) {
            Problem problem = customer.getProblems().get(0);
            if (problem != null) {
                String idCustomer = customer.getIdCust();
                String idPrinter = problem.getIdProduk();
                String idTech = loginToken.getClaim(Constants.CLAIM_USERNAME).asString();
                String reportDate = problem.getWaktuComp();
                String productName = problem.getIdProblem();
                String solvingOption = problem.getSolving().getSolvingOption();
                String solvingNote = problem.getSolving().getSolvingNote();

                tvCustomerId.setText(idCustomer);
                tvPrinterId.setText(idPrinter);
                tvTechId.setText(idTech);
                tvReportDate.setText(reportDate);
                tvProduct.setText(productName);
                tvSolvingOption.setText(solvingOption);
                tvSolvingNote.setText(solvingNote);
            }
        }
    }

    private void onApprove() {
        if (TextUtils.isEmpty(customerOTP)) {
            Toast.makeText(this, getString(R.string.error_empty_otp), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(problemRate)) {
            Toast.makeText(this, getString(R.string.error_empty_rate), Toast.LENGTH_LONG).show();
            return;
        }
        approve();
    }

    private void approve() {
        Customer customer = getOngoindCustomerProblem();
        if (customer != null) {
            Problem problem = customer.getProblems().get(0);
            if (problem != null) {
                showMessageDialog(getString(R.string.progress_approve));
                ApprovalRequestModel formBody = new ApprovalRequestModel();
                formBody.setProb(problem.getIdProblem());
                formBody.setToken(loginToken.toString());
                compositeDisposable.add(
                        APIHelper.approve(formBody)
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
                                                onSuccessApproval(customer);
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
    }

    private void onSuccessApproval(Customer customer) {
        clearOngoingCustomerProblem();
        Toast.makeText(this, getString(R.string.success_message_approved), Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isValid() {
        return !TextUtils.isEmpty(problemRate) &&
                !TextUtils.isEmpty(customerOTP);
    }
}
