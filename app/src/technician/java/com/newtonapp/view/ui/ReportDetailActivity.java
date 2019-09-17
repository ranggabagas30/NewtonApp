package com.newtonapp.view.ui;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;

public class ReportDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private AppCompatImageView ivPrinter;
    private AppCompatTextView tvIDCustomer;
    private AppCompatTextView tvIDPrinter;
    private AppCompatTextView tvIDTechnician;
    private AppCompatTextView tvReportDate;
    private AppCompatTextView tvReportTime;
    private AppCompatTextView tvKontrakNumber;
    private AppCompatTextView tvProductType;
    private AppCompatTextView tvComplainActivity;
    private AppCompatTextView tvComplainResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        toolbar = findViewById(R.id.header_layout_toolbar);
        ivPrinter = findViewById(R.id.reportdetail_iv_pic_printer);
        tvIDCustomer = findViewById(R.id.report_tv_idcustomer);
        tvIDPrinter = findViewById(R.id.reportdetail_tv_idprinter_value);
        tvIDTechnician = findViewById(R.id.reportdetail_tv_idtechnician_value);
        tvReportDate = findViewById(R.id.reportdetail_tv_tglreport_value);
        tvReportTime = findViewById(R.id.reportdetail_tv_waktu_value);
        tvKontrakNumber = findViewById(R.id.reportdetail_tv_contract_value);
        tvProductType = findViewById(R.id.reportdetail_tv_product_value);
        tvComplainActivity = findViewById(R.id.reportdetail_tv_aktivitas_value);
        tvComplainResult = findViewById(R.id.reportdetail_tv_result_value);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_report_detail);
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportNavigateUpTo(this, ReportActivity.class);
        return true;
    }
}
