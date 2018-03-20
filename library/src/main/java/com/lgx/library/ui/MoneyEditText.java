package com.lgx.library.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by liugaoxin on 2018/1/10.
 * 自定义金额输入框，处理金额输入时的各种异常情况
 */

public class MoneyEditText extends AppCompatEditText implements TextWatcher {
    private static final int DECIMAL_NUMBER = 2; //小数点后面只能输入两位

    public MoneyEditText(Context context) {
        this(context, null);
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        //监听初始化
        addTextChangedListener(this);
        setEditFilter();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String mountStr = s.toString();

        //防止只输入一个.的情况
        if(mountStr.equals(".")) {
            setText("0.");
            setSelection(2);
            return;
        }

        //防止.开头
        if(mountStr.startsWith(".")) {
            String fullStr = "0" + mountStr;
            setText(fullStr);
            setSelection(1);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置过滤规则，输入框只能输入小数点后两位
     * source 本次输入的内容
     * start, end本次的起止，一般start为0
     * dest 当前所有的文本内容
     * dstart, dend 本次输入的内容在所有内容中的起止
     */
    private void setEditFilter() {
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String lastInputContent = dest.toString();
                int lastInputLength = lastInputContent.length();

                if (lastInputContent.contains(".")) {
                    int index = lastInputContent.indexOf(".");

                    //输入内容在小数点前面的不做限制，直接返回null
                    if(dstart <= index) {
                        return null;
                    }
                    //输入内容在小数点后面的只能有DECIMAL_NUMBER位，超过返回""
                    if(lastInputLength - index >= DECIMAL_NUMBER + 1) {
                        return "";
                    }
                }
                return null;
            }
        };

        setFilters(new InputFilter[]{inputFilter});
    }
}
