package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.data.database.entity.Customer;
import com.newtonapp.data.network.APIHelper;
import com.newtonapp.data.network.pojo.request.OutstandingRequestModel;
import com.newtonapp.data.network.pojo.request.TakeJobRequestModel;
import com.newtonapp.model.rvmodel.OutstandingRvModelNew;
import com.newtonapp.utility.Constants;
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
    private LinearLayout llFailedBody;
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
        llFailedBody = findViewById(R.id.failed_layout_body);
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
        Customer customerProblem = getOngoindCustomerProblem();
        if (customerProblem != null) {
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

    private void downloadOutstandingJoblist() {
        showMessageDialog(getString(R.string.progress_loading));
        OutstandingRequestModel formBody = new OutstandingRequestModel();
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
                                        onSuccessDownloadOutstandinglist(response.getData());
                                    } else {
                                        Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                                        if (response.getData() == null) {
                                            llFailedBody.setVisibility(View.VISIBLE);
                                            rvOutstandingList.setVisibility(View.GONE);
                                        }
                                    }
                                }, error -> {
                                    hideDialog();
                                    String errorMessage = NetworkUtil.handleApiError(error);
                                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                        )
        );
    }

    private void onSuccessDownloadOutstandinglist(List<Customer> data) {
        populateDataFromNetwork(data);
    }

    private void populateDataFromNetwork(List<Customer> data) {
        outstandingList.clear();
        for (Customer item : data) {
            outstandingList.add(new OutstandingRvModelNew(item));
        }
        outstandingRvAdapter.setData(outstandingList);
    }

    private void takingOutstandingJob(OutstandingRvModelNew outstanding) {
        String idProblem = outstanding.getCustomer().getProblems().get(0).getIdProblem();
        DebugUtil.d("taking job with idprob: " + idProblem);
        showMessageDialog(getString(R.string.progress_taking_outstanding_job));
        TakeJobRequestModel formBody = new TakeJobRequestModel();
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
                                        proceedSolvingProblem(outstanding.getCustomer());
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

    private void proceedSolvingProblem(Customer customer) {
        customer.getProblems().get(0).setStatusComplain(Constants.FLAG_START_PROGRESS);
        setOngoingCustomerProblem(customer);
        navigateTo(this, SolvingActivity.class);
        finish();
    }
}