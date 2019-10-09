package com.newtonapp.utility.barcodescanning;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.newtonapp.R;

public class ScannerOverlay extends RelativeLayout {
    private int parentLeft, parentTop, parentRight, parentBottom;
    private float left, top, right, bottom, endY;
    private int rectWidth, rectHeight;
    private int cornerRadius;
    private int frames;
    private boolean revAnimation;
    private int lineColor, lineWidth;
    private int cornerLineColor, cornerLineThickness;
    private float cornerLineLength;
    private String textInstruction;

    public ScannerOverlay(Context context) {
        super(context);
    }

    public ScannerOverlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerOverlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ScannerOverlay,
                0, 0);
        rectWidth = a.getInteger(R.styleable.ScannerOverlay_square_width, getResources().getInteger(R.integer.scanner_rect_width));
        rectHeight = a.getInteger(R.styleable.ScannerOverlay_square_height, getResources().getInteger(R.integer.scanner_rect_height));
        frames = a.getInteger(R.styleable.ScannerOverlay_scanner_line_speed, getResources().getInteger(R.integer.scanner_line_speed));
        lineColor = a.getColor(R.styleable.ScannerOverlay_scanner_line_color, ContextCompat.getColor(context, R.color.scanner_line_color));
        lineWidth = a.getInteger(R.styleable.ScannerOverlay_scanner_line_width, getResources().getInteger(R.integer.scanner_line_width));
        cornerRadius = a.getInteger(R.styleable.ScannerOverlay_square_corner_radius, getResources().getInteger(R.integer.scanner_rect_corner_radius));
        cornerLineColor = a.getColor(R.styleable.ScannerOverlay_corner_line_color, ContextCompat.getColor(context, R.color.corner_line_color));
        cornerLineLength = a.getInteger(R.styleable.ScannerOverlay_corner_line_length, -1);
        cornerLineThickness = a.getInteger(R.styleable.ScannerOverlay_corner_line_thickness, getResources().getInteger(R.integer.corner_line_thickness));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        parentLeft = left;
        parentTop = top;
        parentRight = right;
        parentBottom = bottom;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        left = (w - dpToPx(rectWidth)) / 2;
        top = (h - dpToPx(rectHeight)) / 2;
        right = left + dpToPx(rectWidth);
        bottom = top + dpToPx(rectHeight);
        endY = top;
        if (cornerLineLength == -1) cornerLineLength = (float) dpToPx(rectWidth) * .1f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // init
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);

        Paint p = new Paint();
        Paint framePaint = new Paint();
        framePaint.setColor(getResources().getColor(R.color.frame_color_1));

        Paint transparentPaint = new Paint();
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        // draw frame
        temp.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), framePaint);

        // draw transparent
        RectF rect = new RectF(left, top, dpToPx(rectWidth) + left, dpToPx(rectHeight) + top);
        temp.drawRoundRect(rect, (float) cornerRadius, (float) cornerRadius, transparentPaint);
        canvas.drawBitmap(bitmap, 0, 0, p);

        Paint cornerLinePaint = new Paint();
        cornerLinePaint.setColor(cornerLineColor);
        cornerLinePaint.setStrokeWidth(Float.valueOf(cornerLineThickness));

        // top left
        canvas.drawLine(left, top, left + cornerLineLength, top, cornerLinePaint);
        canvas.drawLine(left, top, left, top + cornerLineLength, cornerLinePaint);

        // top right
        canvas.drawLine(right, top, right - cornerLineLength, top, cornerLinePaint);
        canvas.drawLine(right, top, right, top + cornerLineLength, cornerLinePaint);

        // bottom right
        canvas.drawLine(right, bottom, right, bottom - cornerLineLength, cornerLinePaint);
        canvas.drawLine(right, bottom, right - cornerLineLength, bottom, cornerLinePaint);

        // bottom left
        canvas.drawLine(left, bottom, left, bottom - cornerLineLength, cornerLinePaint);
        canvas.drawLine(left, bottom, left + cornerLineLength, bottom, cornerLinePaint);

        // draw horizontal line
        Paint line = new Paint();
        line.setColor(lineColor);
        line.setStrokeWidth(Float.valueOf(lineWidth));

        // draw the line to product animation
        if (endY >= top + dpToPx(rectHeight) + frames) {
            revAnimation = true;
        } else if (endY == top + frames) {
            revAnimation = false;
        }

        // check if the line has reached to bottom
        if (revAnimation) {
            endY -= frames;
        } else {
            endY += frames;
        }
        canvas.drawLine(left, endY, left + dpToPx(rectWidth), endY, line);

        invalidate();
    }
}
