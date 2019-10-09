package com.newtonapp.utility.barcodescanning;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.newtonapp.R;

/** Graphic instance for rendering Barcode position and content information in an overlay view. */
public class BarcodeGraphic extends GraphicOverlay.Graphic {

    private int id;

    private static final int TEXT_COLOR_RES = R.color.qrcode_paint_color;
    private static final float TEXT_SIZE = 54.0f;
    private static final float STROKE_WIDTH = 4.0f;

    private Paint rectPaint;
    private Paint barcodePaint;
    private volatile FirebaseVisionBarcode barcode;

    public BarcodeGraphic(GraphicOverlay overlay, FirebaseVisionBarcode barcode) {
        super(overlay);

        this.barcode = barcode;

        final int TEXT_COLOR = getApplicationContext().getResources().getColor(TEXT_COLOR_RES);
        rectPaint = new Paint();
        rectPaint.setColor(TEXT_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        barcodePaint = new Paint();
        barcodePaint.setColor(TEXT_COLOR);
        barcodePaint.setTextSize(TEXT_SIZE);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FirebaseVisionBarcode getBarcode() {
        return barcode;
    }

    public void setBarcode(FirebaseVisionBarcode barcode) {
        this.barcode = barcode;
    }

    @Override
    public void draw(Canvas canvas) {

        if (barcode == null) {
            throw new IllegalStateException("Attempting to draw a null barcode.");
        }

        // Draws the bounding box around the BarcodeBlock.
        RectF rect = new RectF(barcode.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        canvas.drawRect(rect, rectPaint);

        // Renders the barcode at the bottom of the box.
        if (barcode.getRawValue() != null)
            canvas.drawText(barcode.getRawValue(), rect.left, rect.bottom, barcodePaint);

    }
}
