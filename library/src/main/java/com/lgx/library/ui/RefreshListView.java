package com.lgx.library.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ebeitech.library.R;


/**
 * Created by liugaoxin on 2017/7/14.
 * 上拉加载、下拉刷新的listView
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final int FOOTER_STATE_HIDE = 0; //隐藏底部view
    private static final int FOOTER_STATE_NORMAL = 1; //显示底部加载更多的视图
    private static final int FOOTER_STATE_LOADING = 2; //显示底部正在加载中视图
    private static final int FOOTER_STATE_FINISHED = 3; //显示加载已完成

    private static final int HEADER_STATE_HIDE = 0; //头部隐藏状态
    private static final int HEADER_STATE_START = 1; //头部未完全显示状态
    private static final int HEADER_STATE_READY = 2; //头部完全显示状态
    private static final int HEADER_STATE_REFRESHING = 3; //头部隐藏状态
    private static final int HEADER_STATE_SUCCESS = 4; //刷新成功状态

    public static final int TYPE_ALL = 0; //上下加载和下拉刷新都可以
    public static final int TYPE_REFRESH = 1; //只有下拉刷新
    public static final int TYPE_LOAD_MORE = 2; //只有上拉加载

    //顶部布局
    private View mHeaderView;
    private LinearLayout mHeaderRefresh;
    private LinearLayout mHeaderRefreshing;
    private LinearLayout mHeaderSuccess;
    private int mHeaderHeight;
    private int mHeaderState = HEADER_STATE_HIDE;

    //顶部动画部分
    private ImageView mArrowImage;
    private TextView mRefreshText;
    private Animation mUpAnimation;
    private Animation mDownAnimation;

    //底部布局
    private View mFooterView;
    private TextView mFooterAddText;
    private LinearLayout mFooterAddingLinear;
    private int mFooterViewHeight;
    private int mFooterState = FOOTER_STATE_NORMAL;

    //滑动事件
    private int mDownY; //下按时的位置
    private boolean mIsScrollTop = true; //是否已经滚动到头部
    private OnRefreshListener mListener; //监听回调
    private int mRefreshType = TYPE_ALL;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnScrollListener(this);
        initFooterView();
        initRefreshHeader();
        initAnimation();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mListener == null //没设置监听则不做处理
                || !mIsScrollTop //没有滚动到头部则不处理下拉刷新事件
                || mRefreshType == TYPE_LOAD_MORE //只有上拉加载模式
                || mHeaderState == HEADER_STATE_REFRESHING) { //正在下拉刷新时不执行操作
            return super.onTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //目前onItemClick放到子控件，但是会屏蔽掉ACTION_DOWN事件，所以这里重新赋值
                mDownY = (mDownY == 0 ? (int) ev.getY() : mDownY);
                int diff = (int) ((ev.getY() - mDownY) / 2); //移动的距离，除以2为了降低移速
                int paddingTop = -mHeaderHeight + diff; //当前应有的padding

                //设置headerView的padding
                if(paddingTop > 0) { //已经完全显示
                    setHeaderViewState(HEADER_STATE_READY, paddingTop);
                } else { //未完全显示
                    setHeaderViewState(HEADER_STATE_START, paddingTop);
                }

                //因为设置了最大的滑动距离，所以这里在滑动到最大后重新计算按下位置
                if(mHeaderView.getPaddingTop() < paddingTop) {
                    mDownY = (int) (ev.getY() - ((mHeaderView.getPaddingTop() + mHeaderHeight) * 2));
                }

                //headerView没有完全隐藏，则屏蔽listView本身的滑动
                if(paddingTop > -mHeaderHeight) {
                    ev.setLocation(ev.getX(), mDownY);
                    //该方法可以拦截onTouch和onItemClickListener的冲突
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mDownY = 0;
                //是触发刷新效果还是隐藏刷新布局
                if(mHeaderState == HEADER_STATE_READY) {
                    setHeaderViewState(HEADER_STATE_REFRESHING, 0);
                    mListener.onRefresh();
                } else {
                    setHeaderViewState(HEADER_STATE_HIDE, 0);
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //隐藏了底部布局，或者监听为空，或者只有下拉刷新模式，则不做上拉加载的处理
        if(mFooterState != FOOTER_STATE_NORMAL || mListener == null || mRefreshType == TYPE_REFRESH) {
            return;
        }

        boolean isInBottom = (getLastVisiblePosition() == this.getAdapter().getCount() - 1);
        if(isInBottom && scrollState == SCROLL_STATE_IDLE) {
            setFooterViewState(FOOTER_STATE_LOADING);
            mListener.onLoadMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //滚动到最上面时才去处理下拉刷新
        mIsScrollTop = (firstVisibleItem == 0);
    }

    /**
     * 设置监听事件
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 设置是否只有上拉加载或下拉刷新，如果都个都不想要，不设置监听即可
     */
    public void setRefreshType(int type) {
        mRefreshType = type;
    }

    /**
     * 隐藏顶部布局
     */
    public void hideHearView(boolean showSuccess) {
        if(showSuccess) {
            setHeaderViewState(HEADER_STATE_SUCCESS, 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setHeaderViewState(HEADER_STATE_HIDE, 0);
                }
            }, 1000);

        } else {
            setHeaderViewState(HEADER_STATE_HIDE, 0);
        }
    }

    /**
     * 隐藏底部布局
     */
    public void hideFooterView() {
        setFooterViewState(FOOTER_STATE_HIDE);
    }

    /**
     * 底部显示加载更多
     */
    public void showFooterNormal() {
        setFooterViewState(FOOTER_STATE_NORMAL);
    }

    /**
     * 底部显示暂无更多数据
     */
    public void showFooterNoData() {
        setFooterViewState(FOOTER_STATE_FINISHED);
    }

    //设置上拉加载的视图显示，所有状态更新都要走该方法
    private void setFooterViewState(int state) {
        mFooterState = state;

        if(state == FOOTER_STATE_HIDE) {
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

        } else if(state == FOOTER_STATE_LOADING) {
            mFooterView.setPadding(0, 0, 0, 0);
            mFooterAddingLinear.setVisibility(VISIBLE);
            mFooterAddText.setVisibility(INVISIBLE);

        } else if(state == FOOTER_STATE_NORMAL) {
            mFooterView.setPadding(0, 0, 0, 0);
            mFooterAddingLinear.setVisibility(INVISIBLE);
            mFooterAddText.setVisibility(VISIBLE);
            mFooterAddText.setText("查看更多");

        } else if(state == FOOTER_STATE_FINISHED) {
            mFooterView.setPadding(0, 0, 0, 0);
            mFooterAddingLinear.setVisibility(INVISIBLE);
            mFooterAddText.setVisibility(VISIBLE);
            mFooterAddText.setText("暂无更多数据");
        }
    }

    //设置下拉刷新视图的显示，所有状态更新都要走该方法
    private void setHeaderViewState(int state, int padding) {
        if(padding > 0 && mHeaderState == HEADER_STATE_START) {
            mArrowImage.startAnimation(mUpAnimation);
            mRefreshText.setText("释放刷新");

        } else if(padding <= 0 && mHeaderState == HEADER_STATE_READY){
            mArrowImage.startAnimation(mDownAnimation);
            mRefreshText.setText("下拉刷新");
        }

        mHeaderState = state;

        if(state == HEADER_STATE_HIDE) {
            mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);
            mHeaderRefresh.setVisibility(VISIBLE);
            mHeaderRefreshing.setVisibility(INVISIBLE);
            mHeaderSuccess.setVisibility(INVISIBLE);

        } else if(state == HEADER_STATE_START && padding >= -mHeaderHeight) { //已经完全隐藏了则不再往上隐藏
            mHeaderView.setPadding(0, padding, 0, 0);

        } else if(state == HEADER_STATE_READY && padding < mHeaderHeight / 2) { //限制不能下拉太多
            mHeaderView.setPadding(0, padding, 0, 0);

        } else if(state == HEADER_STATE_REFRESHING) {
            mHeaderView.setPadding(0, 0, 0, 0);
            mHeaderRefresh.setVisibility(INVISIBLE);
            mHeaderRefreshing.setVisibility(VISIBLE);

            mArrowImage.startAnimation(mDownAnimation);
            mRefreshText.setText("下拉刷新");

        } else if(state == HEADER_STATE_SUCCESS) {
            mHeaderRefreshing.setVisibility(INVISIBLE);
            mHeaderSuccess.setVisibility(VISIBLE);
        }
    }

    //底部加载更多的初始化
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.view_main_list_footer, null);
        mFooterAddText = (TextView) mFooterView.findViewById(R.id.view_main_list_footer_add);
        mFooterAddingLinear = (LinearLayout) mFooterView.findViewById(R.id.view_main_list_footer_adding);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();

        addFooterView(mFooterView);
    }

    //头部下拉刷新的初始化
    private void initRefreshHeader() {
        mHeaderView = View.inflate(getContext(), R.layout.view_main_list_header, null);
        mHeaderRefresh = (LinearLayout) mHeaderView.findViewById(R.id.main_list_header_refresh);
        mHeaderRefreshing = (LinearLayout) mHeaderView.findViewById(R.id.main_list_header_refreshing);
        mHeaderSuccess = (LinearLayout) mHeaderView.findViewById(R.id.main_list_header_success);
        mArrowImage = (ImageView) mHeaderView.findViewById(R.id.main_list_header_arrow);
        mRefreshText = (TextView) mHeaderView.findViewById(R.id.main_list_header_text);

        mHeaderView.measure(0, 0);
        mHeaderHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);

        addHeaderView(mHeaderView);
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        mUpAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mUpAnimation.setDuration(500);
        mUpAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

        mDownAnimation = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mDownAnimation.setDuration(500);
        mDownAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    public interface OnRefreshListener {
        void onRefresh();
        void onLoadMore();
    }
}
