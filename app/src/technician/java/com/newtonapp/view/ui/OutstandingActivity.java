package com.newtonapp.view.ui;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.model.rvmodel.OutstandingRvModel;
import com.newtonapp.view.adapter.rvadapter.OutstandingRvAdapter;

import java.util.ArrayList;

public class OutstandingActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView rvOutstandingList;
    private OutstandingRvAdapter outstandingRvAdapter;
    private ArrayList<OutstandingRvModel> outstandingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);
        popoulateData();

        toolbar = findViewById(R.id.header_layout_toolbar);
        rvOutstandingList = findViewById(R.id.outstanding_rv_tasklist);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_outstanding_list);

        outstandingRvAdapter = new OutstandingRvAdapter(outstandingList, R.layout.item_outstanding_task);
        outstandingRvAdapter.setOnItemClickListener(this::showConfirmationDialog);
        rvOutstandingList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvOutstandingList.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        rvOutstandingList.setItemAnimator(new DefaultItemAnimator());
        rvOutstandingList.setAdapter(outstandingRvAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportNavigateUpTo(this, DashboardActivity.class);
        return true;
    }

    private void popoulateData() {
        OutstandingRvModel outstanding1 = new OutstandingRvModel("customer1", "KOMP BKI BLOK I NO 12, RT 05/RW 04, KRAMATWATU, SERANG, BANTEN");
        OutstandingRvModel outstanding2 = new OutstandingRvModel("customer2", "SERANG CITY, BLOK G NO 17, RT 03/RW 02, SERANG, BANTEN");
        OutstandingRvModel outstanding3 = new OutstandingRvModel("customer3", "JL. RAYA KEBAYORAN LAMA NO 8A, KEBAYORAN LAMA. GROGOL SELATAN, JAKARTA SELATAN");
        outstandingList = new ArrayList<>();
        outstandingList.add(outstanding1);
        outstandingList.add(outstanding2);
        outstandingList.add(outstanding3);
    }

    private void showConfirmationDialog(OutstandingRvModel outstanding) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Pengambilan Tugas")
                .setMessage("Apakah Anda yakin akan melaksanakan tugas ini ?")
                .setPositiveButton(getString(android.R.string.yes), (dialogInterface, i) -> {
                    // confirm assignment
                })
                .setNegativeButton(getString(android.R.string.no), (dialogInterface, i) -> {
                    // cancel
                })
                .show();
    }
}