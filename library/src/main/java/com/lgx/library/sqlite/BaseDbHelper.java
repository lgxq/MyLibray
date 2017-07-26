package com.lgx.library.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liugaoxin on 2017/7/18.
 * 数据库使用的基类，主要用于管理所有表的创建和数据库升级
 * 具体的表类中的字段和增删改查操作放到表的操作类中去
 */

public class BaseDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "lgx.sqlite";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String TIME_TYPE = " TIMESTAMP";
    private static final String COMMA_SEP = ",";

    //创建推送消息表
    private static final String SQL_CREATE_MESSAGE =
            "create table " + BaseDbContract.PushMessage.TABLE_NAME + " (" +
                    BaseDbContract.PushMessage._ID + INT_TYPE + " primary key autoincrement," +
                    BaseDbContract.PushMessage.COLUMN_NAME_USER + TEXT_TYPE + COMMA_SEP +
                    BaseDbContract.PushMessage.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    BaseDbContract.PushMessage.COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    BaseDbContract.PushMessage.COLUMN_NAME_READ + INT_TYPE + " DEFAULT 0," +
                    BaseDbContract.PushMessage.COLUMN_NAME_TIME + TIME_TYPE + " DEFAULT (datetime('now','localtime'))" +
            " )";

    public BaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //version 1 do nothing
    }
}
