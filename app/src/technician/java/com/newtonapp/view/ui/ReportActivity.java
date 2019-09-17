package com.newtonapp.view.ui;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.model.ReportRvModel;
import com.newtonapp.view.adapter.ReportRvAdapter;

import java.util.ArrayList;

public class ReportActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView rvReportList;
    private ReportRvAdapter reportRvAdapter;
    private ArrayList<ReportRvModel> reportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        toolbar = findViewById(R.id.header_layout_toolbar);
        rvReportList = findViewById(R.id.report_rv_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_report_list);

        prePopulateReportList();
        reportRvAdapter = new ReportRvAdapter(reportList);
        reportRvAdapter.setOnClickListener(report -> {
            // go to report detail
            navigateTo(this, ReportDetailActivity.class);
        });

        rvReportList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvReportList.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        rvReportList.setAdapter(reportRvAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportNavigateUpTo(this, DashboardActivity.class);
        return true;
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
