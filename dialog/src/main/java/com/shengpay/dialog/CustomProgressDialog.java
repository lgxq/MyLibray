package com.shengpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by liugaoxin on 2017/6/19.
 * 自定义进度条弹框
 */

public class CustomProgressDialog extends Dialog {
    private TextView mWarnText;
    private ImageView mRotateImage;
    private String mWarnMessage;
    private RotateAnimation mAnimation;

    public CustomProgressDialog(@NonNull Context context) {
        this(context, "加载中...");
    }

    public CustomProgressDialog(@NonNull Context context, String warnMessage) {
        this(context, R.style.LibraryProgressTheme);
        mWarnMessage = warnMessage;
    }

    private CustomProgressDialog(@NonNull Context context, @StyleRes int themeId) {
        super(context, themeId);
        setContentView(R.layout.library_dialog_progress);
        mWarnText = (TextView) findViewById(R.id.library_progress_message_text);
        mRotateImage = (ImageView) findViewById(R.id.library_progress_rotate_image);
        mAnimation = rotateAnimation();
    }

    public void setMessage(String message) {
        mWarnMessage = message;
        mWarnText.setText(message);
    }

    @Override
    public void show() {
        mRotateImage.setAnimation(mAnimation);
        mWarnText.setText(mWarnMessage);
        mAnimation.start();
        super.show();
    }

    @Override
    public void dismiss() {
        mAnimation.cancel();
        super.dismiss();
    }

    private RotateAnimation rotateAnimation() {
        RotateAnimation animation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(800);
        animation.setFillAfter(false);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator());

        return animation;
    }
}
