package com.lgx.library.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liugaoxin on 2017/6/27.
 * SharedPreference工具类
 */
public class SpUtil {
    private static final String SP_NAME = "NO_CLEAR_TABLE";
    private static SpUtil sSpUtil;

    private SharedPreferences mPreferences;
    
    private SpUtil(Context context) {
        mPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static SpUtil getInstance(Context context) {
        if (sSpUtil == null) {
            sSpUtil = new SpUtil(context);
        }

        return sSpUtil;
    }

    /**
     * 存储数据
     */
    public void putData(String key, Object value) {
        if (value instanceof Boolean) {
            mPreferences.edit().putBoolean(key, (boolean) value).apply();

        } else if (value instanceof String) {
            mPreferences.edit().putString(key, (String) value).apply();

        } else if(value instanceof Integer) {
            mPreferences.edit().putInt(key, (Integer) value).apply();

        } else if (value instanceof Long) {
            mPreferences.edit().putLong(key, (Long) value).apply();

        } else if (value instanceof Float) {
            mPreferences.edit().putFloat(key, (Float) value).apply();
        }
    }

    /**
     * 获取数据
     */
    public <T> T getData(String key, T defaultValue) {
        T resultValue = null;

        if (defaultValue instanceof Boolean) {
            Class<T> tClass = (Class<T>) Boolean.class;
            resultValue =  tClass.cast(mPreferences.getBoolean(key, (Boolean) defaultValue));

        } else if (defaultValue instanceof String) {
            Class<T> tClass = (Class<T>) String.class;
            resultValue =  tClass.cast(mPreferences.getString(key, (String) defaultValue));

        } else if(defaultValue instanceof Integer) {
            Class<T> tClass = (Class<T>) Integer.class;
            resultValue =  tClass.cast(mPreferences.getInt(key, (Integer) defaultValue));

        } else if (defaultValue instanceof Long) {
            Class<T> tClass = (Class<T>) Long.class;
            resultValue =  tClass.cast(mPreferences.getLong(key, (Long) defaultValue));

        } else if (defaultValue instanceof Float) {
            Class<T> tClass = (Class<T>) Float.class;
            resultValue =  tClass.cast(mPreferences.getFloat(key, (Float) defaultValue));
        }

        return resultValue;
    }
}
