package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.model.rvmodel.ReportRvModel;
import com.newtonapp.view.adapter.rvadapter.ReportRvAdapter;

import java.util.ArrayList;

public class ReportActivity extends BaseActivity {

    private Toolbar toolbar;
    private LinearLayout llFailedBody;
    private RecyclerView rvReportList;
    private ReportRvAdapter reportRvAdapter;
    private ArrayList<ReportRvModel> reportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initView();
        initRecyclerView();
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
        llFailedBody = findViewById(R.id.failed_layout_body);
        rvReportList = findViewById(R.id.report_rv_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_report_list);
    }

    private void initRecyclerView() {
        //prePopulateReportList();
        reportRvAdapter = new ReportRvAdapter(reportList);
        reportRvAdapter.setOnClickListener(report -> {
            // go to report detail
            navigateTo(this, ReportDetailActivity.class);
        });

        rvReportList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvReportList.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        rvReportList.setAdapter(reportRvAdapter);

        if (reportList.isEmpty()) {
            llFailedBody.setVisibility(View.VISIBLE);
            rvReportList.setVisibility(View.GONE);
        }
    }

    private void prePopulateReportList() {
        ReportRvModel report1 = new ReportRvModel("C123123123", "SN1231313", "21-09-2019", "SOLVED");
        ReportRvModel report2 = new ReportRvModel("C123412314", "SN123141231","01-09-2019", "HOLD");
        ReportRvModel report3 = new ReportRvModel("C3412324", "SN12124123", "09-03-2019", "SOLVED");
        reportList.add(report1);
        reportList.add(report2);
        reportList.add(report3);
    }
}
