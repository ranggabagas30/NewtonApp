package com.newtonapp.customer.view.ui;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.newtonapp.customer.R;
import com.newtonapp.customer.model.ProblemTrackingModel;
import com.newtonapp.customer.view.adapter.ProblemTrackingAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration;
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView;
import xyz.sangcomz.stickytimelineview.model.SectionInfo;

public class ProblemTrackingActivity extends BaseActivity {

    private AppCompatTextView tvTitle;
    private AppCompatButton btnHome;
    private TimeLineRecyclerView rvTimeLineProblemTracking;
    private ProblemTrackingAdapter problemTrackingAdapter;
    private RecyclerSectionItemDecoration.SectionCallback sectionCallback;

    private ArrayList<ProblemTrackingModel> problemTrackings;
    private List<String> problemTrackingPoints;
    private TypedArray problemTrackingIconActive;
    private TypedArray problemTrackingIconInactive;
    private int currentActiveProblemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_tracking);
        initProblemPoints();
        tvTitle = findViewById(R.id.header_tv_title);
        tvTitle.setText("Problem Tracking");
        problemTrackingAdapter = new ProblemTrackingAdapter(problemTrackingPoints);
        rvTimeLineProblemTracking = findViewById(R.id.problemtracking_timeline);
        rvTimeLineProblemTracking.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvTimeLineProblemTracking.addItemDecoration(sectionCallback);
        rvTimeLineProblemTracking.setAdapter(problemTrackingAdapter);
        btnHome = findViewById(R.id.tracking_btn_home);
        btnHome.setOnClickListener(view -> {
            int flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK;
            navigateTo(ProblemTrackingActivity.this, VerificationActivity.class, flags);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        problemTrackingIconActive.recycle();
        problemTrackingIconInactive.recycle();
    }

    private void initProblemPoints() {
        problemTrackingPoints = Arrays.asList(getResources().getStringArray(R.array.problems));
        problemTrackingIconActive = getResources().obtainTypedArray(R.array.ic_active_problems);
        problemTrackingIconInactive = getResources().obtainTypedArray(R.array.ic_inactive_problems);

        problemTrackings = new ArrayList<>();
        for (int i = 0; i < problemTrackingPoints.size(); i++) {
            ProblemTrackingModel problemTracking = new ProblemTrackingModel();
            problemTracking.setId(i);
            problemTracking.setName(problemTrackingPoints.get(i));
            problemTracking.setIconActive(problemTrackingIconActive.getResourceId(i, R.drawable.ic_verification_complain_active_24dp));
            problemTracking.setIconInactive(problemTrackingIconInactive.getResourceId(i, R.drawable.ic_verification_complain_inactive_24dp));
            problemTrackings.add(problemTracking);
        }
        sectionCallback = getSectionCallback(problemTrackings);
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final ArrayList<ProblemTrackingModel> problemTrackings) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                return problemTrackings.get(position).getId() != problemTrackings.get(position-1).getId();
            }

            @Override
            public SectionInfo getSectionHeader(int position) {
                String problemTitle = problemTrackings.get(position).getName();
                int iconResourceProblem = position == currentActiveProblemId ? problemTrackings.get(position).getIconActive() : problemTrackings.get(position).getIconInactive();
                Drawable iconDrawableProblem = getResources().getDrawable(iconResourceProblem);
                return new SectionInfo(problemTitle, "", iconDrawableProblem);
            }
        };
    }
}
