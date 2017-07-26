package com.lgx.library.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by liugaoxin on 2017/3/16.
 * 数据库使用的基类，主要用于管理所有表的创建和数据库升级
 * 所有的表都要继承该类，并且不能覆盖该类中的onCreate和onUpgrade方法
 * 具体的表类中的字段和增删改查操作放到子类中去
 */
public abstract class BaseSQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "lgx.sqlite";
    private static final int DB_VERSION = 1;

    public BaseSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
