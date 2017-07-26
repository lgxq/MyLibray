package com.lgx.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by liugaoxin on 2017/3/16.
 * 应用中fragment的基类
 */

public abstract class BaseFragment extends Fragment {
    protected abstract int getLayoutId(); //指定要加载的xml

    protected abstract void initView(View view); //初始化布局

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);
        return view;
    }

    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected View findViewById(int id) {
        return getActivity().findViewById(id);
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
}
