package com.lgx.library.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;

/**
 * Created by liugaoxin on 2018/1/11.
 * 自定义银行卡卡号输入框，四个字符自动加空格
 * 不用设置输入类型，代码中以将类型设为phone
 */

public class BankCardEditText extends AppCompatEditText {
    private static final int SPLIT_LENGTH = 4; //每4位加一个空格
    private String mLastString = ""; //防止onTextChanged重复调，上次格式化的内容

    public BankCardEditText(Context context) {
        this(context, null);
    }

    public BankCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(InputType.TYPE_CLASS_PHONE);

        setEditFilter();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //因为重新排序之后setText的存在，会重复调onTextChanged，这里是为了避免这种情况产生一系列问题
        if (before == count || mLastString.equals(s.toString())) {
            return;
        }

        String numberWithoutBlank = s.toString().replace(" ", "");
        StringBuilder newBuilder = new StringBuilder();
        int length = numberWithoutBlank.length();

        //遍历重新拼凑字符串，中间部分每SPLIT_LENGTH位加一个空格
        for(int i = 0; i < length; i++) {
            if(i > 0 && i % SPLIT_LENGTH == 0) {
                newBuilder.append(' ');
            }

            newBuilder.append(numberWithoutBlank.charAt(i));
        }

        //如果新旧字符串一致则不用再重新设置
        if(newBuilder.toString().equals(s.toString())) {
            return;
        }

        //计算光标位置
        int newPosition = getSelectionStart();
        if(before == 0 && count > 0) { //添加字符时，当前光标前一个字符是空格则后退一格
            if(newBuilder.charAt(newPosition - 1) == ' ') {
                newPosition++;
            }
        } else if(before > 0 && count == 0) { //删除字符时，删除一个可能减少两个
            if(newPosition > newBuilder.length()) {
                newPosition--;
            }
        }

        setText(newBuilder);
        setSelection(newPosition);
        mLastString = newBuilder.toString();
    }

    /**
     * 返回不带空格的卡号
     */
    public String getCardNumber() {
        return getText().toString().replace(" ", "");
    }

    //设置过滤规则，只允许输入数字和空格
    private void setEditFilter() {
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(!source.toString().matches("^[\\d\\s]+$")) {
                    return "";
                }

                if(source.toString().equals(" ")) { //禁止单独输入空格
                    return "";
                }
                return null;
            }
        };

        setFilters(new InputFilter[]{inputFilter});
    }
}
