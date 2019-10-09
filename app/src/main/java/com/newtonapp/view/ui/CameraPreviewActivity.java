package com.newtonapp.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.newtonapp.R;
import com.newtonapp.utility.DebugUtil;

import java.util.List;

public class CameraPreviewActivity extends AppCompatActivity implements ReaderFragment.ReaderListener {

    private static final String TAG = CameraPreviewActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST = 1;
    private ReaderFragment readerFragment;
    private boolean autoFocus = false;
    private boolean useFlash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        attachReaderFragment();
    }

    private void attachReaderFragment() {
        readerFragment = ReaderFragment.newInstance(autoFocus, useFlash);
        readerFragment.setReaderListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.camera_layout_container, readerFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onScanned(FirebaseVisionBarcode barcode) {
        if (readerFragment != null) {
            readerFragment.pauseScanning();
        }
    }

    @Override
    public void onScannedMultiple(List<FirebaseVisionBarcode> barcodes) {
        DebugUtil.d("found " + barcodes.size() + " qrcodes");
    }

    @Override
    public void onBitmapScanned(SparseArray<FirebaseVisionBarcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(this, "ERROR: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, getString(R.string.error_access_camera_not_allowed), Toast.LENGTH_LONG).show();
        this.setResult(Activity.RESULT_CANCELED);
        this.finish();
    }
}
