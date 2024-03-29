package com.newtonapp.view.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.newtonapp.BuildConfig;
import com.newtonapp.R;
import com.newtonapp.data.network.pojo.response.VerificationResponseModel;
import com.newtonapp.utility.Constants;

public class DashboardActivity extends BaseActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private GridLayout glLayoutDashboardBody;
    private LinearLayout llLayoutDashboardMenu;
    private CardView cvOutstanding, cvSolving, cvReport;
    private CircularImageView ivProfile;
    private AppCompatTextView tvName, tvIdtechnisian;
    private AppCompatButton btnLogout;

    private String techName;
    private String techUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setInitView();
        setListener();
        loadData();
    }

    @Override
    public Activity onCreateGetCurrentActivity() {
        return this;
    }

    private void setInitView() {
        glLayoutDashboardBody = findViewById(R.id.dashboard_layout_body);
        llLayoutDashboardMenu = findViewById(R.id.dashboard_layout_menu);
        cvOutstanding         = findViewById(R.id.dashboard_cv_outstanding);
        cvSolving             = findViewById(R.id.dashboard_cv_solving);
        cvReport              = findViewById(R.id.dashboard_cv_report);
        ivProfile             = findViewById(R.id.dashboard_iv_profile);
        tvName                = findViewById(R.id.dashboard_tv_name);
        tvIdtechnisian        = findViewById(R.id.dashboard_tv_id);
        btnLogout             = findViewById(R.id.dashboard_btn_logout);
    }

    private void setListener() {

        if (BuildConfig.DEBUG) {
        //if (true) {
            ivProfile.setOnClickListener(view -> navigateTo(this, DebugActivity.class));
        }

        // main menu listener
        cvOutstanding.setOnClickListener(view -> navigateTo(this, OutstandingActivity.class));
        cvSolving.setOnClickListener(view -> navigateTo(this, SolvingActivity.class));
        cvReport.setOnClickListener(view -> navigateTo(this, ReportActivity.class));

        // logout listener
        btnLogout.setOnClickListener(view -> showConfirmationDialog());
    }

    private void loadData() {
        compositeDisposable.add(
                loadProfile().asObservable().subscribe(
                        profileJson -> {
                            VerificationResponseModel.Profile profile = new Gson().fromJson(profileJson, VerificationResponseModel.Profile.class);
                            String profileUrl = profile.getPic();
                            String profileName = profile.getName();
                            tvName.setText(profileName);
                            tvIdtechnisian.setText(loginToken.getClaim(Constants.CLAIM_IDTECHNICIAN).asString());
                            Glide.with(this)
                                    .load(profileUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.blank_profile)
                                    .into(ivProfile);
                        }
                )
        );
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Log out")
                .setMessage("Anda ingin log out dari aplikasi ? ")
                .setPositiveButton(getString(android.R.string.yes), (dialogInterface, i) -> {
                    // confirm assignment
                    doLogout();
                })
                .setNegativeButton(getString(android.R.string.cancel), ((dialogInterface, i) -> {

                }))
                .show();
    }
}
