package com.lgx.library.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ebeitech.library.R;

/**
 * Created by liugaoxin on 2017/5/24.
 * 带清除按钮的EditText
 */

public class EditTextWithClear extends AppCompatEditText implements
        View.OnFocusChangeListener, TextWatcher {
    //删除按钮
    private Drawable mClearDrawable;

    public EditTextWithClear(Context context) {
        this(context, null);
    }

    public EditTextWithClear(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditTextWithClear(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];

        //设置右侧的清除按钮
        if(mClearDrawable == null) {
            mClearDrawable = ContextCompat.getDrawable(getContext(), R.drawable.clear_btn);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());

        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    /**
     * 展示和隐藏清除按钮
     * @param visible 是否可见
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     *
     * 目前为了方便点击，将点击范围左右扩大半个图标宽度的响应距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                double clearLeftPosition = getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth() * 1.5;
                double clearRightPosition = getWidth() - getPaddingRight() + mClearDrawable.getIntrinsicWidth() * 0.5;

                boolean touchable = (event.getX() > clearLeftPosition) && (event.getX() < clearRightPosition);
                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }


    //TextChangeListener
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);

        } else {
            setClearIconVisible(false);
        }
    }
}
