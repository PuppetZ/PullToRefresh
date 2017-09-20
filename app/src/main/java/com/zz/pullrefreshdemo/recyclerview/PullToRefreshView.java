package com.zz.pullrefreshdemo.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zz.pullrefreshdemo.R;

/**
 * Created by zhangjing on 2017/9/20.
 * RecyclerView下拉刷新的view
 */

public class PullToRefreshView extends LinearLayout {
    private Context mContext;
    private LinearLayout mLinearLayout;
    private ImageView mImageView;//下拉的箭头
    private TextView mTextView;//刷新的文字
    private ProgressBar mProgressBar;//刷新时的图标
    private int stateView;//状态
    private int height;//高度
    private Animation mAnimationDown;//向下动画
    private Animation mAnimationUp;//向上动画
    public final static int PULL_TO_REFRESH = 0;//下拉刷新
    public final static int RELEASE_TO_REFRESH = 1;//下拉完成，释放即刷新
    public final static int REFRESHING = 2;//正在刷新
    public final static int REFRESHED = 3;//刷新完成


    public PullToRefreshView(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        stateView = PULL_TO_REFRESH;//第一个状态
        mLinearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.rv_refresh, null);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        setLayoutParams(lp);//设置LP
        setPadding(0, 0, 0, 0);
        addView(mLinearLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);
        mImageView = (ImageView) mLinearLayout.findViewById(R.id.iv_arrow);
        mTextView = (TextView) mLinearLayout.findViewById(R.id.tv_refresh_status);
        mProgressBar = (ProgressBar) mLinearLayout.findViewById(R.id.ball_loader);

        mAnimationDown = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimationDown.setDuration(200);
        mAnimationDown.setFillAfter(true);
        mAnimationUp = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimationUp.setDuration(200);
        mAnimationUp.setFillAfter(true);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        height = getMeasuredHeight();
    }

    /**
     * 获取现在所处的状态
     *
     * @return 状态
     */
    public int getState() {
        return stateView;
    }

    /**
     * @return 获取显示的高度
     */
    public int getVisibleHeight() {
        LayoutParams layoutParams = (LayoutParams) mLinearLayout.getLayoutParams();
        return layoutParams.height;
    }

    /**
     * @param height 下拉刷新的高度
     */
    public void setVisibleHeight(int height) {
        if (height <= 0) {
            height = 0;
        }
        LayoutParams layoutParams = (LayoutParams) mLinearLayout.getLayoutParams();
        layoutParams.height = height;
        mLinearLayout.setLayoutParams(layoutParams);

    }

    public void setState(int state) {
        if (state == stateView) return;
        if (state == PULL_TO_REFRESH) {
            mImageView.setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
            mTextView.setText("下拉刷新");
            if (stateView == PULL_TO_REFRESH) {
                mImageView.startAnimation(mAnimationDown);
            } else if (stateView == REFRESHING) {
                mImageView.clearAnimation();
            }
        } else if (state == RELEASE_TO_REFRESH) {
            mImageView.setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
            mTextView.setText("释放开始刷新");
            if (stateView != RELEASE_TO_REFRESH) {
                mImageView.clearAnimation();
                mImageView.startAnimation(mAnimationUp);
            }
        } else if (state == REFRESHING) {
            mImageView.clearAnimation();
            mImageView.setVisibility(GONE);
            mProgressBar.setVisibility(VISIBLE);
            mTextView.setText("正在刷新");
        } else if (state == REFRESHED) {
            mImageView.clearAnimation();
            mImageView.setVisibility(GONE);
            mProgressBar.setVisibility(GONE);
            mTextView.setText("刷新完成");
        }
        stateView = state;

    }

    /**
     * 刷新完成 0.2s后设置为第一状态
     */
    public void refreshComplete() {
        setState(REFRESHED);
        postDelayed(new Runnable() {
            @Override
            public void run() {
               setState(PULL_TO_REFRESH);
            }
        },200);
    }

    /**
     * 根据滑动的距离判断松手是释放刷新还是下拉刷新状态
     *
     * @param offsetY 滑动距离
     */
    public void onMove(float offsetY) {
        if (getVisibleHeight() > 0 || offsetY > 0) {
            setVisibleHeight((int) (offsetY + getVisibleHeight()));
            if (stateView <= RELEASE_TO_REFRESH) {
                if (getVisibleHeight() > height) {
                    setState(RELEASE_TO_REFRESH);
                } else {
                    setState(PULL_TO_REFRESH);
                }
            }
        }
    }

    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int heightView = getVisibleHeight();
        int destHeight = 0;
        if (heightView == 0) {
            isOnRefresh = false;
        }
        if (stateView == REFRESHING){
            destHeight = height;
        }
        if (heightView >height && stateView < REFRESHING){
            setState(REFRESHING);
            destHeight = height;
            isOnRefresh=true;
        }
        return isOnRefresh;

    }

}
