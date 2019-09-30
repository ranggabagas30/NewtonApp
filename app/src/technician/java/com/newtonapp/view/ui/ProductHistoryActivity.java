package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.R;
import com.newtonapp.model.rvmodel.OutstandingRvModelNew;
import com.newtonapp.view.adapter.rvadapter.OutstandingRvAdapter;

import java.util.ArrayList;

public class ProductHistoryActivity extends BaseActivity {

    private static final String TAG = ProductHistoryActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView rvProductHistory;
    private OutstandingRvAdapter outstandingRvAdapter;
    private ArrayList<OutstandingRvModelNew> outstandingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history);
        //popoulateData();

        toolbar = findViewById(R.id.header_layout_toolbar);
        rvProductHistory = findViewById(R.id.prodhistory_rv_history);
        rvProductHistory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.screen_historical_usage);

        outstandingRvAdapter = new OutstandingRvAdapter(outstandingList, R.layout.item_outstanding_task);
        rvProductHistory.setAdapter(outstandingRvAdapter);
        rvProductHistory.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        rvProductHistory.setItemAnimator(new DefaultItemAnimator());
        rvProductHistory.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /*private void popoulateData() {
        OutstandingRvModel outstanding1 = new OutstandingRvModel("customer1", "KOMP BKI BLOK I NO 12, RT 05/RW 04, KRAMATWATU, SERANG, BANTEN");
        OutstandingRvModel outstanding2 = new OutstandingRvModel("customer2", "SERANG CITY, BLOK G NO 17, RT 03/RW 02, SERANG, BANTEN");
        OutstandingRvModel outstanding3 = new OutstandingRvModel("customer3", "JL. RAYA KEBAYORAN LAMA NO 8A, KEBAYORAN LAMA. GROGOL SELATAN, JAKARTA SELATAN");
        outstandingList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int n = new Random().nextInt(3);
            switch (n) {
                case 0 : outstandingList.add(outstanding1); break;
                case 1 : outstandingList.add(outstanding2); break;
                case 2 : outstandingList.add(outstanding3); break;
            }
        }

        Log.d(TAG, "popoulateData: list size -> " + outstandingList.size());
    }*/
}
