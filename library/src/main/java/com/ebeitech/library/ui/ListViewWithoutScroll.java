package com.ebeitech.library.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/4/22 0022.
 * <br>类描述:自定义一个ListView控件
 * <br>功能详细描述: ScrollView嵌套ListView的解决办法
 */
public class ListViewWithoutScroll extends ListView {
    public ListViewWithoutScroll(Context context) {
        super(context);
    }

    public ListViewWithoutScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewWithoutScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
