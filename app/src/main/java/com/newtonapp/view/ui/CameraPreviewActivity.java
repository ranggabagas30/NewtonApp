package com.newtonapp.view.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.newtonapp.R;
import com.newtonapp.utility.barcodescanning.BarcodeScanningProcessor;
import com.newtonapp.utility.barcodescanning.CameraSource;
import com.newtonapp.utility.barcodescanning.CameraSourcePreview;
import com.newtonapp.utility.barcodescanning.GraphicOverlay;
import com.newtonapp.utility.barcodescanning.ScannerOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraPreviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CameraSourcePreview cameraSourcePreview;
    private GraphicOverlay graphicOverlay;
    private ScannerOverlay scannerOverlay;

    private static final String TAG = CameraPreviewActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST = 1;

    private CameraSource cameraSource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate camera preview....");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        toolbar = findViewById(R.id.header_layout_toolbar);
        cameraSourcePreview = findViewById(R.id.camera_layout_preview);
        graphicOverlay = findViewById(R.id.camera_layout_overlay);
        scannerOverlay = findViewById(R.id.camera_layout_frame_overlay);
        scannerOverlay.setVisibility(View.VISIBLE);

        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.label_scan_instruction));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (cameraSourcePreview == null) {
            Log.d(TAG, "Preview is null");
        }

        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        cameraSourcePreview.stop();

        if (allPermissionsGranted()) {
            Log.d(TAG, "all permissions granted, craete camera source");
            createCameraSource();
            //startCameraSource();
        } else {
            Log.d(TAG, "get runtime permissions first");
            getRuntimePermissions();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /*Stops the camera.*/
    @Override
    protected void onPause() {
        super.onPause();
        cameraSourcePreview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        Log.i(TAG, "Using Barcode Detector Processor");
        cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor(CameraPreviewActivity.this));
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.*/

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (cameraSourcePreview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                cameraSourcePreview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }
}
