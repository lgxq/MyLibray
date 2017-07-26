package com.ebeitech.library.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ebeitech.library.base.BaseApplication;

/**
 * Created by liugaoxin on 2017/3/16.
 * 用于sharedPreference数据存储，存储在用户退出后需要清空的数据
 */

public class SharedManagerClear {
    private static final String SP_TABLE_NAME = "com.lgx.clearTableName";
    private static SharedPreferences sSharedPreference = BaseApplication.sApplication.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);

    public static void getString(String key) {
        sSharedPreference.getString(key, "");
    }
}
