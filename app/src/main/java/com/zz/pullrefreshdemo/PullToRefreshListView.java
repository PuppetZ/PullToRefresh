package com.zz.pullrefreshdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by zhangjing on 2017/8/18.
 */

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final int PULL_FIRST = 0;//刷新开始 刷新结束的状态
    private static final int PULL_SECOND = 1;//下拉刷新时
    private static final int PULL_THIRD = 2;//下拉完成时
    private static final int PULL_FOUTH = 3;//刷新时
    private int state;//判断下拉状态
    private LinearLayout headerView;//头视图
    private int headerViewHeight;//头视图的高度
    private float startY;//Y轴初始位置
    private float offsetY;//Y轴滑动的距离
    private OnMyRefreshListener mOnMyRefreshListener;

    public  interface  OnMyRefreshListener{
        void  onRefresh();
    }

    public PullToRefreshListView(Context context) {
        super(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


}
