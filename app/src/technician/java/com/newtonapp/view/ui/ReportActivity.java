package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.ReportsRequestModel;
import com.newtonapp.data.network.pojo.response.ReportsResponseModel;
import com.newtonapp.model.rvmodel.ReportRvModel;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.NetworkUtil;
import com.newtonapp.view.adapter.rvadapter.ReportRvAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReportActivity extends BaseActivity {

    private Toolbar toolbar;
    private LinearLayout llFailedBody;
    private RecyclerView rvReportList;
    private ReportRvAdapter rvReportAdapter;
    private ArrayList<ReportRvModel> reports;

    private AppCompatImageView ivFailedLogo;
    private AppCompatTextView tvFailedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initView();
        initRecyclerView();
        downloadReports();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void offline() {
        super.offline();
        if (reports.isEmpty()) showNoItemView();
    }

    @Override
    protected void online() {
        super.online();
        if (reports.isEmpty()) {
            downloadReports();
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.header_layout_toolbar);
        llFailedBody = findViewById(R.id.failed_layout_body);
        rvReportList = findViewById(R.id.report_rv_list);
        ivFailedLogo = findViewById(R.id.body_iv_failed_logo);
        tvFailedMessage = findViewById(R.id.body_tv_failed_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_report_list);
    }

    private void initRecyclerView() {
        reports = new ArrayList<>();
        rvReportAdapter = new ReportRvAdapter(reports, R.layout.item_report);
        rvReportAdapter.setOnItemClickListener(report -> {
                String reportData = new Gson().toJson(report);
                navigateTo(this, ReportDetailActivity.class, Constants.EXTRA_REPORT_DETAIL, reportData);
        });

        rvReportList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvReportList.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        rvReportList.setAdapter(rvReportAdapter);
    }

    private void downloadReports() {
        showMessageDialog(getString(R.string.progress_getting_reports));
        ReportsRequestModel formBody = new ReportsRequestModel();
        formBody.setToken(loginToken.toString());
        compositeDisposable.add(
                APIHelper.getReportList(formBody)
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribeOn(Schedulers.io())
                         .subscribe(
                                 response -> {
                                     hideDialog();
                                     if (response == null) {
                                         throw new NullPointerException(getString(R.string.error_null_response));
                                     }

                                     if (response.getStatus() == 1) {
                                         onSuccessDownloadReports(response);
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

    private void onSuccessDownloadReports(ReportsResponseModel response) {
        if (response.getData() == null) {
            showNoItemView();
            return;
        }
        populateDataReports(response.getData());
    }

    private void populateDataReports(List<Customer> customers) {
        showNormalView();
        reports.clear();
        for (Customer customer : customers) {
            reports.add(new ReportRvModel(customer));
        }
        rvReportAdapter.setData(reports);
    }

    private void setNormalMode() {
        llFailedBody.setVisibility(View.GONE);
    }

    private void setFailedMode() {
        llFailedBody.setVisibility(View.VISIBLE);
    }

    private void showNormalView() {
        setNormalMode();
    }

    private void showNoItemView() {
        setFailedMode();
        ivFailedLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty));
        tvFailedMessage.setText(getString(R.string.success_message_empty_data_item));
    }

    private void showNoInternetConnectionView() {
        setFailedMode();
        ivFailedLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_network_unavailable));
        tvFailedMessage.setText(getString(R.string.error_no_internet_connection));
    }
}
