package com.lgx.library.sqlite;

import android.provider.BaseColumns;

/**
 * Created by liugaoxin on 2017/7/18.
 * 存储数据库的表结构
 */

public class BaseDbContract {
    private BaseDbContract() {}

    public static abstract class PushMessage implements BaseColumns {
        public static final String TABLE_NAME = "push_message";
        public static final String COLUMN_NAME_USER = "user";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_READ = "read";
        public static final String COLUMN_NAME_TIME = "time";
    }
}
