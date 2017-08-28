package com.zz.pullrefreshdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangjing on 2017/8/18.
 *
 */

public class PullFirstView extends View {
    private Bitmap startBitmap, endBitmap, scaledBitmap;
    private int measureWidth, measureHeight;
    private float mCurrentProgress;

    public PullFirstView(Context context) {
        super(context);
        init();
    }


    public PullFirstView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullFirstView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //下拉刷新第一个状态 结束时的图片
        startBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pull_image));
        //第二个状态的图片
        endBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.refreshing_image_frame_05));
    }

    /**
     * 绘制view的宽度大小
     * 因为view中的第二状态图片的大小是确定的，所以根据第二状态的图片来确定view的大小
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {//view给出精确的数值时
            width = size;
        } else {
            width = endBitmap.getWidth();
            if (mode == MeasureSpec.AT_MOST) {//view 设置为wrap_content时
                width = Math.min(width, size);
            }
        }
        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), endBitmap.getHeight() / endBitmap.getWidth() * measureWidth(widthMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        measureWidth = getMeasuredWidth();
        measureHeight = getMeasuredHeight();
        //通过获取view的大小  对第一开始的图片进行缩放
        scaledBitmap = Bitmap.createScaledBitmap(startBitmap, measureWidth, endBitmap.getHeight() / endBitmap.getWidth() * measureWidth, true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置view的缩放 （宽缩放比例，高缩放比例，基准点的X坐标，基准点的Y坐标）
        canvas.scale(mCurrentProgress, mCurrentProgress, measureWidth / 2, measureHeight / 2);
        //绘制view
        canvas.drawBitmap(scaledBitmap, 0, measureHeight / 4, null);
    }

    /**
     * 设置view的缩放比例
     *
     * @param currentProgress
     * @return
     */
    public void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
    }
}
