package com.shengpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by liugaoxin on 2017/6/19.
 * 自定义通用dialog
 */

public class CustomCommonDialog extends Dialog implements View.OnClickListener {
    private String mTitle;
    private String mContentStr;
    private DialogInterface.OnClickListener mCancelListener, mPositiveListener;
    private String mCancelStr, mPositiveStr;
    private boolean mOnlyPositive = false; //是否只有取消按钮

    public CustomCommonDialog(@NonNull Context context) {
        this(context, R.style.LibraryDialogTheme);
    }

    public CustomCommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public CustomCommonDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public CustomCommonDialog setMessage(String message) {
        mContentStr = message;
        return this;
    }

    /*
     * 是否只保留确定按钮
     */
    public CustomCommonDialog setOnlyPositive(boolean onlyPositive) {
        mOnlyPositive = onlyPositive;
        return this;
    }

    public CustomCommonDialog setNegativeButton(DialogInterface.OnClickListener negativeListener) {
        mCancelListener = negativeListener;
        return this;
    }

    public CustomCommonDialog setNegativeButton(
            String buttonName, DialogInterface.OnClickListener negativeListener) {
        mCancelStr = buttonName;
        mCancelListener = negativeListener;
        return this;
    }

    public CustomCommonDialog setPositiveButton(DialogInterface.OnClickListener positiveListener) {
        mPositiveListener = positiveListener;
        return this;
    }

    public CustomCommonDialog setPositiveButton(
            String buttonName, DialogInterface.OnClickListener positiveListener) {
        mPositiveStr = buttonName;
        mPositiveListener = positiveListener;
        return this;
    }

    public void show() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.library_dialog_common, null);

        //标题
        TextView titleText = (TextView) view.findViewById(R.id.library_common_dialog_title);
        if(mTitle == null) {
            titleText.setVisibility(View.GONE);

        } else {
            titleText.setText(mTitle);
        }

        //内容
        TextView contentText = (TextView) view.findViewById(R.id.library_common_dialog_content);
        if(mContentStr != null) {
            contentText.setText(mContentStr);

        } else {
            contentText.setText("");
        }

        //按钮
        TextView cancelButton = (TextView) view.findViewById(R.id.library_common_dialog_cancel);
        cancelButton.setOnClickListener(this);
        if(mCancelStr != null) {
            cancelButton.setText(mCancelStr);
        }

        TextView positiveButton = (TextView) view.findViewById(R.id.library_common_dialog_sure);
        positiveButton.setOnClickListener(this);
        if(mPositiveStr != null) {
            positiveButton.setText(mPositiveStr);
        }

        //是否只保留确定按钮
        if(mOnlyPositive) {
            cancelButton.setVisibility(View.INVISIBLE);
        }

        //测量View的高度
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        //最多占屏幕高度的四分之三
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int maxHeight = dm.heightPixels / 4 * 3;
        int resultHeight = (view.getMeasuredHeight() > maxHeight ? maxHeight : ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, resultHeight);

        setContentView(view, params);
        super.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.library_common_dialog_cancel) {
            if(mCancelListener != null)
                mCancelListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);

            dismiss();

        } else if (view.getId() == R.id.library_common_dialog_sure) {
            if(mPositiveListener != null)
                mPositiveListener.onClick(this, DialogInterface.BUTTON_POSITIVE);

            dismiss();
        }
    }
}
