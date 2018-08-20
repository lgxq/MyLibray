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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by liugaoxin on 2017/6/6.
 * 自定义dialog，主要是为了统一样式
 */

public class CustomListDialog extends Dialog {
    private String mTitle;
    private SelectFinishListener mSelectFinishListener;
    private String[] mModels;

    public CustomListDialog(@NonNull Context context) {
        this(context, R.style.LibraryDialogTheme);
    }

    public CustomListDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    //设置标题
    public CustomListDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    //设置数据源
    public CustomListDialog setModels(String[] models) {
        mModels = models;
        return this;
    }

    //设置监听
    public CustomListDialog setSelectFinishListener(SelectFinishListener listener) {
        mSelectFinishListener = listener;
        return this;
    }

    //点击空白地方是否可以取消
    public CustomListDialog setCustomCancel(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    //展示对话框
    public void show() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.library_dialog_list, null);
        view.findViewById(R.id.library_list_dialog_error_layout).setVisibility(View.GONE);
        view.findViewById(R.id.library_list_dialog_progress).setVisibility(View.GONE);

        //设置listView
        ListView listView = (ListView) view.findViewById(R.id.library_list_dialog);
        listView.setAdapter(new MyAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectFinishListener.OnSelected(position);
                dismiss();
            }
        });

        //设置标题栏
        TextView titleText = (TextView) view.findViewById(R.id.library_list_dialog_title);
        if(mTitle != null) {
            titleText.setText(mTitle);
        } else {
            titleText.setVisibility(View.GONE);
        }

        //取消按钮
        setCancelable(false);
        view.findViewById(R.id.library_list_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //最多占屏幕高度的四分之三，viewHeight获取不到，目前手动计算
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int maxHeight = dm.heightPixels / 4 * 3;
        int viewHeight = (dm.heightPixels / 640) * (65 + 55 * (mModels.length + 1)); //  = 比例 * dp
        int resultHeight = (viewHeight > maxHeight ? maxHeight : ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, resultHeight);
        setContentView(view, params);

        super.show();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mModels.length;
        }

        @Override
        public Object getItem(int position) {
            return mModels[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if(null == convertView) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.library_item_list_dialog, parent, false);
                textView = (TextView) convertView.findViewById(R.id.library_item_dialog_name);
                convertView.setTag(textView);

            } else {
                textView = (TextView) convertView.getTag();
            }

            textView.setText((String) getItem(position));
            return convertView;
        }
    }


    public interface SelectFinishListener {
        void OnSelected(int position);
    }
}
