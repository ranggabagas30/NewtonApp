package com.newtonapp.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.newtonapp.R;
import com.newtonapp.model.notification.AssignNotification;
import com.newtonapp.utility.Constants;
import com.pixplicity.easyprefs.library.Prefs;

public class DebugActivity extends AppCompatActivity {

    private static final String TAG = DebugActivity.class.getSimpleName();
    private Toolbar toolbar;
    private AppCompatEditText etFirebaseToken;
    private AppCompatEditText etFirebaseMessagePayload;
    private AppCompatEditText etNotificationTitle;
    private AppCompatEditText etNotificationMessage;
    private AppCompatTextView tvCameraResult;
    private AppCompatButton btnOpenCamera;
    private AppCompatButton btnNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        toolbar = findViewById(R.id.header_layout_toolbar);
        etFirebaseToken = findViewById(R.id.debug_et_firebasetoken);
        etFirebaseMessagePayload = findViewById(R.id.debug_et_firebasemessage);
        etNotificationTitle = findViewById(R.id.debug_et_notification_title);
        etNotificationMessage = findViewById(R.id.debug_et_notification_message);
        tvCameraResult = findViewById(R.id.debug_tv_camera_result);
        btnOpenCamera = findViewById(R.id.debug_btn_camera_open);
        btnNotify = findViewById(R.id.debug_btn_notification_notify);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.screen_debug));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etFirebaseToken.setText(Prefs.getString(getString(R.string.key_firebase_token), null));
        etFirebaseMessagePayload.setText(Prefs.getString(getString(R.string.key_firebase_message_payload), null));
        btnOpenCamera.setOnClickListener(view -> {
            Intent openCameraIntent = new Intent(this, CameraPreviewActivity.class);
            startActivityForResult(openCameraIntent, Constants.RC_SCAN_BARCODE);
        });
        btnNotify.setOnClickListener(view -> {
            String title = etNotificationTitle.getText().toString();
            String message = etNotificationMessage.getText().toString();
            new AssignNotification(this, title, message).show(AssignNotification.ASSIGN_NOTIFICATION_ID);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String qrcodeValue = data != null ? data.getStringExtra(Constants.EXTRA_INTENT_RESULT_SCAN) : null;

        Log.d(TAG, "requestCode : " + requestCode);
        Log.d(TAG, "resultCode : " + resultCode);
        Log.d(TAG, "data barcode : " + qrcodeValue);
        if (requestCode == Constants.RC_SCAN_BARCODE && resultCode == RESULT_OK) {
            tvCameraResult.setText(qrcodeValue);
        }
    }
}
