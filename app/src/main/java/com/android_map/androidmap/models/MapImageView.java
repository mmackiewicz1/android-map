package com.android_map.androidmap.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MapImageView extends ImageView {
    private Paint currentPaint;
    public boolean drawRect = false;
    public int left;
    public int top;
    public int right;
    public int bottom;

    public MapImageView(Context context) {
        super(context);

        init();
    }

    public MapImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MapImageView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    private void init() {
        currentPaint = new Paint();
        currentPaint.setDither(true);
        currentPaint.setColor(Color.RED);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);
        currentPaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawRect) {
            canvas.drawRect(left, top, right, bottom, currentPaint);
        }
    }
}
