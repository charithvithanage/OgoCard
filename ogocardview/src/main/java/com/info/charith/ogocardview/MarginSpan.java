package com.info.charith.ogocardview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

public class MarginSpan extends ReplacementSpan {

    private int mPadding;

    public MarginSpan(int padding) {
        mPadding = padding;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        float[] widths = new float[end - start];
        paint.getTextWidths(text, start, end, widths);
        int sum = mPadding;
        for (int i = 0; i < widths.length; i++) {
            sum += widths[i];
        }
        return sum;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        canvas.drawText(text, start, end, x, y, paint);
    }

}