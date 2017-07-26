package com.ebeitech.library.base;

import android.app.Application;

/**
 * Created by liugaoxin on 2017/3/16.
 * 用于library中需要初始化的第三方控件初始化，app模块的application需要继承该基类
 */

public class BaseApplication extends Application {
    public static BaseApplication sApplication;

    @Override
    public void onCreate() {
        sApplication = this;
        super.onCreate();
    }
}
