package com.newtonapp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.newtonapp.R;

public class DashboardActivity extends BaseActivity {

    private GridLayout glLayoutDashboardBody;
    private LinearLayout llLayoutDashboardMenu;
    private CardView cvOutstanding, cvSolving, cvReport;
    private AppCompatButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        glLayoutDashboardBody = findViewById(R.id.dashboard_layout_body);
        llLayoutDashboardMenu = findViewById(R.id.dashboard_layout_menu);
        cvOutstanding         = findViewById(R.id.dashboard_cv_outstanding);
        cvSolving             = findViewById(R.id.dashboard_cv_solving);
        cvReport              = findViewById(R.id.dashboard_cv_report);
        btnLogout             = findViewById(R.id.dashboard_btn_logout);

        cvOutstanding.setOnClickListener(view -> navigateTo(this, OutstandingActivity.class));
        cvSolving.setOnClickListener(view -> navigateTo(this, SolvingActivity.class));
        cvReport.setOnClickListener(view -> navigateTo(this, ReportActivity.class));
        btnLogout.setOnClickListener(view -> showConfirmationDialog());
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Log out")
                .setMessage("Anda ingin log out dari aplikasi ? ")
                .setPositiveButton(getString(android.R.string.yes), (dialogInterface, i) -> {
                    // confirm assignment
                    navigateTo(DashboardActivity.this, LoginActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                })
                .setNegativeButton(getString(android.R.string.no), (dialogInterface, i) -> {
                    // cancel
                })
                .show();
    }
}
