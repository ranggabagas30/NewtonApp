package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.OutstandingJoblistRequestModel;
import com.newtonapp.data.network.pojo.request.TakingOutstandingRequestModel;
import com.newtonapp.model.rvmodel.OutstandingRvModelNew;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.NetworkUtil;
import com.newtonapp.view.adapter.rvadapter.OutstandingRvAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OutstandingActivity extends BaseActivity {

    private static final String TAG = OutstandingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView rvOutstandingList;
    private OutstandingRvAdapter outstandingRvAdapter;
    private ArrayList<OutstandingRvModelNew> outstandingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);
        initView();
        prepareRecyclerView();
        checkOnGoingProblemAvailbility();
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadOutstandingJoblist();
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportNavigateUpTo(this, DashboardActivity.class);
        return true;
    }

    private void initView() {
        toolbar = findViewById(R.id.header_layout_toolbar);
        rvOutstandingList = findViewById(R.id.outstanding_rv_tasklist);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_outstanding_list);
    }

    private void prepareRecyclerView() {
        outstandingList = new ArrayList<>();
        outstandingRvAdapter = new OutstandingRvAdapter(outstandingList, R.layout.item_outstanding_task);
        outstandingRvAdapter.setOnItemClickListener(this::showConfirmationDialog);

        rvOutstandingList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvOutstandingList.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        rvOutstandingList.setItemAnimator(new DefaultItemAnimator());
        rvOutstandingList.setAdapter(outstandingRvAdapter);
    }

    private void checkOnGoingProblemAvailbility() {
        String currentIdProblem = getOngoindCustomerProblem();
        if (!TextUtils.isEmpty(currentIdProblem)) {
            outstandingRvAdapter.setOnItemClickListener(data -> showUnableTakingOutstandingJob());
            rvOutstandingList.setAdapter(outstandingRvAdapter);
        }
    }

    private void showConfirmationDialog(OutstandingRvModelNew outstanding) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi pengambilan tugas")
                .setMessage("Lanjutkan pengambilan tugas untuk customer " + outstanding.getCustomer().getIdCust() + " ?")
                .setPositiveButton(getString(android.R.string.yes), (dialogInterface, i) -> {
                    // confirm assignment
                    takingOutstandingJob(outstanding);
                })
                .setNegativeButton(getString(android.R.string.cancel), (dialogInterface, i) -> {

                })
                .show();
    }

    private void showUnableTakingOutstandingJob() {
        Toast.makeText(this, getString(R.string.error_unable_taking_job), Toast.LENGTH_LONG).show();
    }

    private void populateDataFromNetwork(List<Customer> data) {
        outstandingList.clear();
        for (Customer item : data) {
            outstandingList.add(new OutstandingRvModelNew(item));
        }
        outstandingRvAdapter.setData(outstandingList);
    }

    private void downloadOutstandingJoblist() {
        showMessageDialog(getString(R.string.progress_loading));
        OutstandingJoblistRequestModel formBody = new OutstandingJoblistRequestModel();
        formBody.setToken(loginToken.toString());
        compositeDisposable.add(
                APIHelper.getOutstandingJoblist(formBody)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                response -> {
                                    hideDialog();
                                    if (response == null) {
                                        throw new NullPointerException(getString(R.string.error_null_response));
                                    }

                                    if (response.getStatus() == 1) {
                                        populateDataFromNetwork(response.getData());
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

    private void takingOutstandingJob(OutstandingRvModelNew outstanding) {
        String idProblem = outstanding.getCustomer().getProblems().get(0).getIdProblem();
        DebugUtil.d("taking job with idprob: " + idProblem);
        showMessageDialog(getString(R.string.progress_taking_outstanding_job));
        TakingOutstandingRequestModel formBody = new TakingOutstandingRequestModel();
        formBody.setProb(idProblem);
        formBody.setToken(loginToken.toString());
        compositeDisposable.add(
                APIHelper.takingOutstandingJob(formBody)
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
                                        proceedSolvingProblem(outstanding);
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

    private void proceedSolvingProblem(OutstandingRvModelNew outstanding) {
        String customerJson = new Gson().toJson(outstanding);
        setOngoingCustomerProblem(customerJson);
        navigateTo(this, SolvingActivity.class);
        finish();
    }
}