package com.newtonapp.view.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;
import com.pixplicity.easyprefs.library.Prefs;

public class DebugActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatEditText etFirebaseToken;
    private AppCompatEditText etFirebaseMessagePayload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        toolbar = findViewById(R.id.header_layout_toolbar);
        etFirebaseToken = findViewById(R.id.debug_et_firebasetoken);
        etFirebaseMessagePayload = findViewById(R.id.debug_et_firebasemessage);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.screen_debug));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etFirebaseToken.setText(Prefs.getString(getString(R.string.key_firebase_token), null));
        etFirebaseMessagePayload.setText(Prefs.getString(getString(R.string.key_firebase_message_payload), null));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
