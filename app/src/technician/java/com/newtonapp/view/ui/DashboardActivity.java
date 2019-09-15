package com.newtonapp.view.ui;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

import com.newtonapp.R;

public class DashboardActivity extends BaseActivity {

    private GridLayout glLayoutDashboardBody;
    private LinearLayout llLayoutDashboardMenu;
    private CardView cvOutstanding, cvSolving, cvReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        glLayoutDashboardBody = findViewById(R.id.dashboard_layout_body);
        llLayoutDashboardMenu = findViewById(R.id.dashboard_layout_menu);
        cvOutstanding         = findViewById(R.id.dashboard_cv_outstanding);
        cvSolving             = findViewById(R.id.dashboard_cv_solving);
        cvReport              = findViewById(R.id.dashboard_cv_report);

        cvOutstanding.setOnClickListener(view -> navigateTo(this, OutstandingActivity.class));
        cvSolving.setOnClickListener(view -> navigateTo(this, SolvingActivity.class));
    }
}
