package com.newtonapp.view.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.newtonapp.R;
import com.newtonapp.model.OutstandingRvModel;
import com.newtonapp.view.adapter.OutstandingRvAdapter;

import java.util.ArrayList;

public class OutstandingActivity extends AppCompatActivity {

    private RecyclerView outstandingRv;
    private OutstandingRvAdapter outstandingRvAdapter;
    private ArrayList<OutstandingRvModel> outstandingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding);
        popoulateData();

        outstandingRvAdapter = new OutstandingRvAdapter(outstandingList);
        outstandingRvAdapter.setOnClickListener(outstanding -> {
            showConfirmationDialog(outstanding);
        });

        outstandingRv = findViewById(R.id.outstanding_rv_tasklist);
        outstandingRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        outstandingRv.setItemAnimator(new DefaultItemAnimator());
        outstandingRv.setAdapter(outstandingRvAdapter);
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