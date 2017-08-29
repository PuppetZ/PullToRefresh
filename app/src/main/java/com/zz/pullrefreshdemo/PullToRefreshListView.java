package com.zz.pullrefreshdemo;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by zhangjing on 2017/8/18.
 */

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final int PULL_FIRST = 0;//刷新开始 刷新结束的状态
    private static final int PULL_SECOND = 1;//下拉刷新时
    private static final int PULL_THIRD = 2;//下拉完成时
    private static final int PULL_FOUTH = 3;//刷新时
    private int state;//判断下拉状态
    private boolean isEnd;//刷新完毕？
    private boolean isRefreable;//是否是可刷新状态
    private int mFirstVisibleItem;
    private boolean isRecondY;//记录Y轴
    private LinearLayout headerView;//头视图
    private int headerViewHeight;//头视图的高度
    private float startY;//Y轴初始位置
    private float offsetY;//Y轴滑动的距离
    private OnMyRefreshListener mOnMyRefreshListener;
    private PullFirstView pull_refresh_first_view;//下拉状态的view
    private PullSecondView pull_refresh_second_view;
    private PullThirdView pull_refresh_third_view;//下拉完成时和刷新时的view
    private AnimationDrawable secondAnim, thirdAnim;//下拉完成时和刷新时的动画
    private TextView pull_refresh_text_view;


    public interface OnMyRefreshListener {
        void onRefresh();
    }

    public PullToRefreshListView(Context context) {
        super(context);
        initView(context);
    }


    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
    }

    public void setOnMyRefreshListener(OnMyRefreshListener onMyRefreshListener) {
        mOnMyRefreshListener = onMyRefreshListener;
        isRefreable = true;
    }

    private void initView(Context context) {
        setOnScrollListener(this);
        setOverScrollMode(OVER_SCROLL_NEVER);//Overscroll（边界回弹）效果 当滑动到边界的时候，如果再滑动，就会有一个边界就会有一个发光效果。
        headerView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pull_refresh, null);
        pull_refresh_first_view = (PullFirstView) headerView.findViewById(R.id.pull_refresh_first_view);
        pull_refresh_second_view = (PullSecondView) headerView.findViewById(R.id.pull_refresh_second_view);
        pull_refresh_third_view = (PullThirdView) headerView.findViewById(R.id.pull_refresh_third_view);
        pull_refresh_second_view.setBackgroundResource(R.drawable.pull_to_end_anim);
        pull_refresh_third_view.setBackgroundResource(R.drawable.refresh_to_end_anim);
        secondAnim = (AnimationDrawable) pull_refresh_second_view.getBackground();//下拉完成动画
        thirdAnim = (AnimationDrawable) pull_refresh_third_view.getBackground();//刷新时动画
        pull_refresh_text_view = (TextView) headerView.findViewById(R.id.pull_refresh_text_view);

        measureView(headerView);//绘制下拉刷新view，如果不绘制，lv的初态时，下拉刷新view就出现了，因为高度默认是xml的。就是为了获取getMeasuredHeight的
        addHeaderView(headerView);
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);

        state = PULL_FIRST;//默认第一个状态
        isEnd = true;
        isRefreable = false;

    }

    /**
     * 这个LayoutParams类是用于child view（子视图） 向 parent view（父视图）传达自己的意愿的一个东西（孩子想变成什么样向其父亲说明，告诉父控件 是怎么放置自己的
     *
     * @param headerView
     */
    private void measureView(View headerView) {
        ViewGroup.LayoutParams params = headerView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int headerViewWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int lpHeight = params.height;
        int headerViewHeightSpec;
        if (lpHeight > 0) {//如果下拉刷新的view 高度>0，高度是精确的
            headerViewHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {//view的高度是<0，高度是想多大多大的，父view不会限制
            headerViewHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        headerView.measure(headerViewWidthSpec, headerViewHeightSpec);
    }

    /**
     * 根据状态改变headerView的动画和文字显示
     *
     * @param state
     */
    private void headerViewState(int state) {
        switch (state) {
            case PULL_FIRST://第一个状态 未刷新或刷新完成状态
                headerView.setPadding(0, -headerViewHeight, 0, 0);
                pull_refresh_first_view.setVisibility(VISIBLE);
                pull_refresh_second_view.setVisibility(GONE);
                secondAnim.stop();
                pull_refresh_third_view.setVisibility(GONE);
                thirdAnim.stop();
                break;
            case PULL_SECOND://第二个状态 下拉状态
                pull_refresh_first_view.setVisibility(VISIBLE);
                pull_refresh_second_view.setVisibility(GONE);
                secondAnim.stop();
                pull_refresh_third_view.setVisibility(GONE);
                thirdAnim.stop();
                pull_refresh_text_view.setText("下拉刷新");
                break;
            case PULL_THIRD://第三个状态 下拉完成状态（松手即可刷新）
                pull_refresh_first_view.setVisibility(GONE);
                pull_refresh_second_view.setVisibility(VISIBLE);
                secondAnim.start();
                pull_refresh_third_view.setVisibility(GONE);
                thirdAnim.stop();
                pull_refresh_text_view.setText("放开刷新");
                break;
            case PULL_FOUTH://第四个状态 正在刷新状态
                pull_refresh_first_view.setVisibility(GONE);
                pull_refresh_second_view.setVisibility(GONE);
                secondAnim.stop();
                pull_refresh_third_view.setVisibility(VISIBLE);
                thirdAnim.start();
                pull_refresh_text_view.setText("正在刷新");
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnd) {//如果刷新完毕 可再次刷新
            if (isRefreable) {//并且是可刷新状态
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN://手指摁下
                        if (mFirstVisibleItem == 0 && !isRecondY) {//lv处于顶部，并且没有记录Y轴
                            isRecondY = true;
                            startY = ev.getY();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE://滑动
                        //手指刚嗯下Y轴距离
                        float tempY = ev.getY();
                        if (mFirstVisibleItem == 0 && !isRecondY) {
                            isRecondY = true;
                            startY = tempY;
                        }
                        //如果当前状态不是第三状态 正在刷新 ，记录Y坐标
                        if (state != PULL_THIRD && isRecondY) {
                            offsetY = tempY - startY;//Y轴偏移
                            float currentHeight = (-headerViewHeight + offsetY / 3);//当前Y轴滑动距离 3就是随便一个系数
                            float currentProgress = 1 + currentHeight / headerViewHeight;//用当前滑动的高度和头部headerView的总高度进行比 计算出当前滑动的百分比 0到1
                            if (currentProgress > 1) {//如果百分比>1，默认为1
                                currentProgress = 1;
                            }
                            //如果是第四状态 正在刷新
                            if (state == PULL_FOUTH && isRecondY) {
                                setSelection(0);//默认lv选择了第一个item
                                if (-headerViewHeight + offsetY / 3 < 0) {//下拉的位移小于headview，继续是下拉状态
                                    state = PULL_SECOND;
                                    headerViewState(state);
                                } else if (offsetY < 0) {//下拉位移<0，是第一个状态
                                    state = PULL_FIRST;
                                    headerViewState(state);
                                }
                            }
                            //如果是第二状态 下拉状态
                            if (state == PULL_SECOND && isRecondY) {
                                setSelection(0);
                                if (-headerViewHeight + offsetY / 3 >= 0) {
                                    state = PULL_THIRD;
                                    headerViewState(state);
                                } else if (offsetY <= 0) {
                                    state = PULL_FIRST;
                                    headerViewState(state);
                                }
                            }
                            //如果是第一状态 刷新完成或未刷新的状态
                            if (state == PULL_FIRST && isRecondY) {
                                if (offsetY >= 0) {
                                    state = PULL_SECOND;
                                    headerViewState(state);
                                }
                            }
                            //如果是第二状态  下拉状态  或者第三状态  放开刷新状态
                            if (state == PULL_SECOND || state == PULL_FOUTH) {
                                headerView.setPadding(0, (int) (-headerViewHeight + offsetY / 3), 0, 0);
                                pull_refresh_first_view.setCurrentProgress(currentProgress);
                                pull_refresh_first_view.postInvalidate();//firstView重绘（在主线程可以重绘）
                            }
                            /*if (state ==PULL_FOUTH){

                            }*/
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //如果是第二状态  下拉刷新状态
                        if (state == PULL_SECOND) {
                            this.smoothScrollBy((int) (-headerViewHeight + offsetY / 3) + headerViewHeight, 500);//隐藏headView
                            headerViewState(state);
                        }
                        //如果是第三状态  放开刷新状态
                        if (state == PULL_THIRD) {
                            this.smoothScrollBy((int) (-headerViewHeight + offsetY / 3) + headerViewHeight, 500);//隐藏headView
                            state = PULL_FOUTH;
                            mOnMyRefreshListener.onRefresh();
                            headerViewState(state);
                        }
                        isRecondY = false;
                        break;
                }
            }
        }
        return super.onTouchEvent(ev);
    }
}
