package com.newtonapp.view.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.newtonapp.R;
import com.newtonapp.utility.Constants;
import com.newtonapp.utility.DebugUtil;
import com.newtonapp.utility.PermissionUtil;
import com.newtonapp.utility.barcodescanning.BarcodeScanningProcessor;
import com.newtonapp.utility.barcodescanning.CameraSource;
import com.newtonapp.utility.barcodescanning.CameraSourcePreview;
import com.newtonapp.utility.barcodescanning.GraphicOverlay;
import com.newtonapp.utility.barcodescanning.ScannerOverlay;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReaderFragment extends Fragment implements View.OnTouchListener, EasyPermissions.PermissionCallbacks {

    private CameraSourcePreview cameraSourcePreview;
    private GraphicOverlay graphicOverlay;
    private ScannerOverlay scannerOverlay;

    private CameraSource cameraSource;
    private ReaderListener readerListener;
    private boolean autoFocus;
    private boolean useFlash;
    private boolean useScanOverlay;
    private boolean isPaused = false;

    public ReaderFragment() {
        // Required empty public constructor
    }

    public static ReaderFragment newInstance(boolean autoFocus, boolean useFlash) {
        return newInstance(autoFocus, useFlash, true);
    }

    public static ReaderFragment newInstance(boolean autoFocus, boolean useFlash, boolean useScanOverlay) {
        Bundle args = new Bundle();
        args.putBoolean(Constants.EXTRA_AUTO_FOCUS, autoFocus);
        args.putBoolean(Constants.EXTRA_USE_FLASH, useFlash);
        args.putBoolean(Constants.EXTRA_USE_SCAN_OVERLAY, useScanOverlay);

        ReaderFragment fragment = new ReaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        autoFocus = getArguments().getBoolean(Constants.EXTRA_AUTO_FOCUS, false);
        useFlash  = getArguments().getBoolean(Constants.EXTRA_USE_FLASH, false);
        useScanOverlay = getArguments().getBoolean(Constants.EXTRA_USE_SCAN_OVERLAY, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraSourcePreview = view.findViewById(R.id.reader_layout_preview);
        graphicOverlay = view.findViewById(R.id.reader_v_graphic_overlay);
        scannerOverlay = view.findViewById(R.id.reader_layout_scan_overlay);
        scannerOverlay.setVisibility(useScanOverlay? View.VISIBLE : View.GONE);
    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BarcodeReaderFragment);
        autoFocus = a.getBoolean(R.styleable.BarcodeReaderFragment_auto_focus, true);
        useFlash = a.getBoolean(R.styleable.BarcodeReaderFragment_use_flash, false);
        a.recycle();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cameraSourcePreview.stop();
        if (PermissionUtil.hasPermission(getActivity(), PermissionUtil.CAMERA)) {
            // start preview
            proceedAfterPermissionGranted();
        } else requestCameraPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSourcePreview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) cameraSource.release();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.RC_CAMERA) {
            if (PermissionUtil.hasPermission(getActivity(), PermissionUtil.CAMERA)) {
                // navigate opening camera
                proceedAfterPermissionGranted();
            } else requestCameraPermission();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @AfterPermissionGranted(Constants.RC_CAMERA)
    private void requestCameraPermission() {
        PermissionUtil.requestPermission(getActivity(), getString(R.string.warning_message_rationale_camera), Constants.RC_CAMERA, PermissionUtil.CAMERA);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        proceedAfterPermissionGranted();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (readerListener != null) readerListener.onCameraPermissionDenied();
    }

    public void setReaderListener(ReaderListener readerListener) {
        this.readerListener = readerListener;
    }

    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(getActivity(), graphicOverlay);
        }

        DebugUtil.d("Using Barcode Detector Processor");
        cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor(getActivity()));
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.*/

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (cameraSourcePreview == null) {
                    DebugUtil.d("resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    DebugUtil.d("resume: graphOverlay is null");
                }
                cameraSourcePreview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                DebugUtil.e("Unable to start camera source: " + e.getMessage(), e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    public void proceedAfterPermissionGranted() {
        createCameraSource();
    }

    public void pauseScanning() {
        isPaused = true;
    }

    public void resumeScanning() {
        isPaused = false;
    }

    public interface ReaderListener {
        void onScanned(FirebaseVisionBarcode barcode);

        void onScannedMultiple(List<FirebaseVisionBarcode> barcodes);

        void onBitmapScanned(SparseArray<FirebaseVisionBarcode> sparseArray);

        void onScanError(String errorMessage);

        void onCameraPermissionDenied();
    }
}
