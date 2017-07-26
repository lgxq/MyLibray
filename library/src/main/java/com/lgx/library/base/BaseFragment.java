package com.lgx.library.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.shengpay.dialog.CustomCommonDialog;
import com.shengpay.dialog.CustomProgressDialog;

/**
 * Created by liugaoxin on 2017/3/16.
 * 应用中fragment的基类
 */

public abstract class BaseFragment extends Fragment {
    private CustomProgressDialog mProgressDialog;

    protected abstract int getLayoutId(); //指定要加载的xml
    protected abstract void initView(View view); //初始化布局

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mProgressDialog = new CustomProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);

        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);

        return view;
    }

    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showWarnAlter(String message) {
        new CustomCommonDialog(getActivity())
                .setMessage(message)
                .setOnlyPositive(true)
                .show();
    }

    protected void setOnclickListener(View view, int id, View.OnClickListener onClickListener) {
        view.findViewById(id).setOnClickListener(onClickListener);
    }

    protected void gotoActivity(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    protected void println(String message) {
        if(BuildConfig.DEBUG) {
            System.out.println(message);
        }
    }

    protected void hideSoftMethod() {
        View view = getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showProgress() {
        mProgressDialog.show();
    }

    protected void hideProgress() {
        mProgressDialog.dismiss();
    }
}
