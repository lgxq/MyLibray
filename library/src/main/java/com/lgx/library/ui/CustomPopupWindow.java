package com.lgx.library.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ebeitech.library.R;
import com.lgx.library.util.UtilFunction;

/**
 * Created by liugaoxin on 2017/7/17.
 * 通用的popupWindow效果
 */

public class CustomPopupWindow extends PopupWindow {
    private Context mContext;
    private String[] mParams;
    private OnItemClickListener mItemListener;

    private final int[] mLocation = new int[2]; //坐标的位置（x、y）
    private Rect mRect = new Rect(); //实例化一个矩形

    public CustomPopupWindow(Context context, String[] params) {
        //设置布局的参数
        this(context, params, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private CustomPopupWindow(Context context, String[] params, int width, int height) {
        mContext = context;
        mParams = params;

        //设置可以获得焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true);
        //设置弹窗外可点击
        setOutsideTouchable(true);
        setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.popoup_back));

        //设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

        //设置弹窗的布局界面
        setContentView(getLinerView());

        //消失时恢复透明度，并设置退出动画
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        setAnimationStyle(R.style.PopupAnimation);
    }

    /**
     * 显示弹窗列表界面
     */
    @SuppressLint("RtlHardcoded")
    public void show(View view) {
        backgroundAlpha(0.7f);

        //获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);
        //设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());

        //离最右边留出一些间距
        int leftPadding = UtilFunction.dp2px(mContext, 8);
        //因为terminalImage设置了paddingBottom 12dp ,所以这里去掉一些
        int bottomPadding = UtilFunction.dp2px(mContext, 4);
        showAtLocation(getContentView(), Gravity.RIGHT|Gravity.TOP, leftPadding, mRect.bottom - bottomPadding);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }

    private View getLinerView() {
        ListView listView = new ListView(mContext);
        listView.setDividerHeight(1);
        listView.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_popup_window_text, mParams));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mItemListener != null) {
                    mItemListener.onItemClick(mParams[position], position);
                }
                dismiss();
            }
        });

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(listView);

        //因为listView设置wrap_content没有效果，所以这里加一个高度为0的textView，是为了让linearLayout有wrap_content的效果
        TextView textview = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_popup_window_text, linearLayout, false);
        textview.setText(getMaxLengthStr());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        linearLayout.addView(textview, params);

        return linearLayout;
    }

    //设置屏幕透明度
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity)mContext).getWindow().setAttributes(lp);
    }

    //返回数组中最长的字符串
    private String getMaxLengthStr() {
        String maxStr = "";
        for(String string : mParams) {
            if(maxStr.length() < string.length())
                maxStr = string;
        }

        return maxStr;
    }

    //自定义点击事件
    public interface OnItemClickListener {
        void onItemClick(String message, int position);
    }
}
