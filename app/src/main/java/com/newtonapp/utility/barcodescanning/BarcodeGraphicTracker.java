package com.newtonapp.utility.barcodescanning;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import java.util.ArrayList;
import java.util.List;

public class BarcodeGraphicTracker extends Tracker<FirebaseVisionBarcode> {

    private GraphicOverlay<BarcodeGraphic> mOverlay;
    private BarcodeGraphic mGraphic;
    private BarcodeGraphicTrackerListener listener;

    BarcodeGraphicTracker(GraphicOverlay<BarcodeGraphic> overlay, BarcodeGraphic graphic, BarcodeGraphicTrackerListener listener) {
        mOverlay = overlay;
        mGraphic = graphic;
        this.listener = listener;
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    @Override
    public void onNewItem(int id, FirebaseVisionBarcode item) {
        mGraphic.setId(id);
        Log.e("XX", "barcode detected: " + item.getDisplayValue() + ", listener: " + listener);

        if (listener != null) {
            listener.onScanned(item);
        }
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */
    @Override
    public void onUpdate(Detector.Detections<FirebaseVisionBarcode> detectionResults, FirebaseVisionBarcode item) {
        mOverlay.add(mGraphic);
        mGraphic.setBarcode(item);

        if (detectionResults != null && detectionResults.getDetectedItems().size() > 1) {
            Log.e("XX", "Multiple items detected");
            Log.e("XX", "onUpdate: " + detectionResults.getDetectedItems().size());

            if (listener != null) {
                List<FirebaseVisionBarcode> barcodes = asList(detectionResults.getDetectedItems());
                listener.onScannedMultiple(barcodes);
            }
        }
    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    /**
     * Hide the graphic when the corresponding object was not detected.  This can happen for
     * intermediate frames temporarily, for example if the object was momentarily blocked from
     * view.
     */
    @Override
    public void onMissing(Detector.Detections<FirebaseVisionBarcode> detectionResults) {
        mOverlay.remove(mGraphic);
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
        mOverlay.remove(mGraphic);
    }

    public interface BarcodeGraphicTrackerListener {
        void onScanned(FirebaseVisionBarcode barcode);

        void onScannedMultiple(List<FirebaseVisionBarcode> barcodes);

        void onBitmapScanned(SparseArray<FirebaseVisionBarcode> sparseArray);

        void onScanError(String errorMessage);
    }
}
