package com.ebeitech.library.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/4/15 0015.
 * <br>类描述:自定义一个GridView控件
 * <br>功能详细描述: ScrollView嵌套GridView的解决办法
 */
public class GridViewWithoutScroll extends GridView {
    public GridViewWithoutScroll(Context context) {
        super(context);
    }

    public GridViewWithoutScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewWithoutScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
