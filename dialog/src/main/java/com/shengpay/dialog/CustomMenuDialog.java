package com.shengpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by liugaoxin on 2017/7/9.
 * 菜单选项的弹出框
 */

public class CustomMenuDialog extends Dialog {
    private boolean mCancelVisible = true;
    private SelectFinishListener mSelectFinishListener;
    private String[] mModels;

    public CustomMenuDialog(@NonNull Context context) {
        this(context, R.style.LibraryDialogTheme);
    }

    public CustomMenuDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    //设置数据源
    public CustomMenuDialog setModels(String[] models) {
        mModels = models;
        return this;
    }

    //设置监听
    public CustomMenuDialog setSelectFinishListener(SelectFinishListener listener) {
        mSelectFinishListener = listener;
        return this;
    }

    //设置底部的取消是否可见
    public CustomMenuDialog setCancelVisible(boolean visible) {
        mCancelVisible = visible;
        return this;
    }

    public void show() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.library_view_custom_menu, null);

        //设置listView
        ListView listView = (ListView) view.findViewById(R.id.library_menu_dialog_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.library_item_menu_dialog, mModels);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectFinishListener.OnSelected(position);
                dismiss();
            }
        });

        //取消按钮
        setCancelable(false);
        TextView mCancelText = (TextView) view.findViewById(R.id.library_menu_dialog_cancel);
        mCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if(!mCancelVisible) {
            view.findViewById(R.id.library_menu_dialog_split).setVisibility(View.INVISIBLE);
            mCancelText.setVisibility(View.GONE);
        }

        //最多占屏幕高度的四分之三，viewHeight获取不到，目前手动计算
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int maxHeight = dm.heightPixels / 4 * 3;
        int viewHeight = (dm.heightPixels / 640) * (65 + 55 * (mModels.length + 1)); //  = 比例 * dp
        int resultHeight = (viewHeight > maxHeight ? maxHeight : ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, resultHeight);
        setContentView(view, params);

        super.show();
    }


    public interface SelectFinishListener {
        void OnSelected(int position);
    }
}
