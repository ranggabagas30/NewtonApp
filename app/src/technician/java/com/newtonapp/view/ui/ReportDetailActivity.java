package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.newtonapp.R;
import com.newtonapp.model.rvmodel.ReportRvModel;
import com.newtonapp.utility.Constants;

public class ReportDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private AppCompatImageView ivReport;
    private AppCompatTextView tvCustomerId;
    private AppCompatTextView tvPrinterId;
    private AppCompatTextView tvTechnicianId;
    private AppCompatTextView tvReportDate;
    private AppCompatTextView tvReportTime;
    private AppCompatTextView tvContractNumber;
    private AppCompatTextView tvProductType;
    private AppCompatTextView tvSolvingOption;
    private AppCompatTextView tvSolvingNote;

    private ReportRvModel reportDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        initView();
        loadReportDetail();
        updateView();
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportNavigateUpTo(this, ReportActivity.class);
        return true;
    }


    private void initView() {
        toolbar = findViewById(R.id.header_layout_toolbar);
        ivReport = findViewById(R.id.reportdetail_iv_pic_report);
        tvCustomerId = findViewById(R.id.reportdetail_tv_customerid_value);
        tvPrinterId = findViewById(R.id.reportdetail_tv_printerid_value);
        tvTechnicianId = findViewById(R.id.reportdetail_tv_technicianid_value);
        tvReportDate = findViewById(R.id.reportdetail_tv_reportdate_value);
        tvReportTime = findViewById(R.id.reportdetail_tv_reporttime_value);
        tvContractNumber = findViewById(R.id.reportdetail_tv_contractnumber_value);
        tvProductType = findViewById(R.id.reportdetail_tv_product_value);
        tvSolvingOption = findViewById(R.id.reportdetail_tv_solvingoption_value);
        tvSolvingNote = findViewById(R.id.reportdetail_tv_solvingnote_value);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_report_detail);
    }

    private void loadReportDetail() {
        String reportData = getIntent().getStringExtra(Constants.EXTRA_REPORT_DETAIL);
        if (TextUtils.isEmpty(reportData)) {
            Toast.makeText(this, getString(R.string.error_empty_report_detail), Toast.LENGTH_LONG).show();
            return;
        }
        reportDetail = new Gson().fromJson(reportData, ReportRvModel.class);
    }

    private void updateView() {
        if (reportDetail != null) {
            tvCustomerId.setText(reportDetail.getIdcustomer());
            tvPrinterId.setText(reportDetail.getIdprinter());
            tvTechnicianId.setText(loginToken.getClaim(Constants.CLAIM_USERNAME).asString());
            tvReportDate.setText(reportDetail.getIssueddate());
            tvSolvingOption.setText(reportDetail.getSolvingOption());
            tvSolvingNote.setText(reportDetail.getSolvingReason());
        }
    }
}
