package com.zz.pullrefreshdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangjing on 2017/8/18.
 */

public class PullSecondView extends View {

    private Bitmap endBitmap;


    public PullSecondView(Context context) {
        super(context);
        init();
    }


    public PullSecondView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullSecondView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        endBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pull_end_image_frame_05));
    }

    /**
     * 设置view的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureWidth(widthMeasureSpec)* endBitmap.getHeight() / endBitmap.getWidth() );
    }

    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;

        } else {
            width = endBitmap.getWidth();
            if (mode == MeasureSpec.AT_MOST) {
                width = Math.min(width, size);
            }
        }
        return width;
    }
}
