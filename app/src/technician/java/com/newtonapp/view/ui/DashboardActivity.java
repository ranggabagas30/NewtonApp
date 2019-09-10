package com.newtonapp.view.ui;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.newtonapp.R;
import com.pixplicity.sharp.Sharp;

public class DashboardActivity extends AppCompatActivity {

    private GridLayout glLayoutDashboardBody;
    private LinearLayout llLayoutDashboardMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //glLayoutDashboardBody = findViewById(R.id.dashboard_layout_body);
        llLayoutDashboardMenu = findViewById(R.id.dashboard_layout_menu);

        Sharp.loadResource(getResources(), R.raw.logo_portrait).into(llLayoutDashboardMenu);
    }
}
