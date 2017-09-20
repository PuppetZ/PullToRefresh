package com.zz.pullrefreshdemo.recyclerview;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by zhangjing on 2017/9/5.
 */

public class PullToRefreshRV extends RecyclerView {
    private PullToRefreshView mPullToRefreshRV;
    private float mLastY = -1;
    private boolean mScrollTop;
    private OnRefreshCompleteListener mCompleteListener;


    public interface OnRefreshCompleteListener {
        void onRefreshComplete();
    }

    public void setOnRefreshCompleteListener(OnRefreshCompleteListener listener) {
        mCompleteListener = listener;
    }

    public PullToRefreshRV(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshRV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshRV(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mPullToRefreshRV = new PullToRefreshView(context);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrollTop()) {
                    float offsetY = e.getRawY() - mLastY;
                    mPullToRefreshRV.onMove(offsetY / 3);
                    mLastY = e.getRawY();
                    if (mPullToRefreshRV.getVisibleHeight() > 0 && mPullToRefreshRV.getState() < 2) {//在正在刷新状态之前
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1;
                if (mPullToRefreshRV.releaseAction()) {
                    if (mCompleteListener != null) {
                        mCompleteListener.onRefreshComplete();
                    }
                }
        }
        return super.onTouchEvent(e);
    }

    public PullToRefreshView getPullToRefreshRV() {
        return mPullToRefreshRV == null ? null : mPullToRefreshRV;
    }

    public boolean isScrollTop() {
        if (getLayoutManager() instanceof LinearLayoutManager && ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() <= 1) {
            mScrollTop = true;
        } else {
            mScrollTop = false;
        }
        return mScrollTop;
    }

    public void refreshComplete() {
        mPullToRefreshRV.refreshComplete();
    }
}
