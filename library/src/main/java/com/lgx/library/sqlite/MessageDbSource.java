package com.lgx.library.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liugaoxin on 2017/7/18.
 * 每一个数据库表的具体操作一个类
 * 该类用于参考，没有实际作用
 * query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
 */

public class MessageDbSource {
    private static MessageDbSource sInstance;

    private BaseDbHelper mDbHelper;
    private String mUserAccount;

    private MessageDbSource(@NonNull Context context) {
        mDbHelper = new BaseDbHelper(context);
        mUserAccount = "";
    }

    public static MessageDbSource getInstance(@NonNull Context context) {
        if(sInstance == null) {
            sInstance = new MessageDbSource(context);
        }

        return sInstance;
    }

    /**
     * 插入新消息
     * @return 是否成功
     */
    public boolean insertMessage(String title, String message) {
        ContentValues cv = new ContentValues();
        cv.put(BaseDbContract.PushMessage.COLUMN_NAME_USER, mUserAccount);
        cv.put(BaseDbContract.PushMessage.COLUMN_NAME_TITLE, title);
        cv.put(BaseDbContract.PushMessage.COLUMN_NAME_MESSAGE, message);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long result = db.insert(BaseDbContract.PushMessage.TABLE_NAME, null, cv);

        db.close();
        return result > 0;
    }

    /**
     * 返回当前账号的所有列表，分页查询
     * @param lastId 从头查询传-1
     */
    public List<PushMessageModel> getUserMessage(int lastId, int pageSize) {
        List<PushMessageModel> models = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selectionString = BaseDbContract.PushMessage.COLUMN_NAME_USER + " = ?";
        String[] selectionArgs = new String[] {mUserAccount};
        //需要分页查询时
        if(lastId >= 0) {
            selectionString += " and " + BaseDbContract.PushMessage._ID + " < " + lastId;
        }

        Cursor cursor = db.query(BaseDbContract.PushMessage.TABLE_NAME,
                null,
                selectionString,
                selectionArgs,
                null,
                null,
                BaseDbContract.PushMessage._ID + " desc",
                pageSize + "");
        MessageCourse messageCourse = new MessageCourse(cursor);

        while (messageCourse.moveToNext()) {
            models.add(messageCourse.getModel());
        }

        if(cursor != null)
            cursor.close();
        db.close();

        return models;
    }

    /**
     * 将所有消息更新为已读
     * @return the number of rows affected
     */
    public int changeHasRead() {
        ContentValues cv = new ContentValues();
        cv.put(BaseDbContract.PushMessage.COLUMN_NAME_READ, 1);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int result = db.update(BaseDbContract.PushMessage.TABLE_NAME, cv,
                BaseDbContract.PushMessage.COLUMN_NAME_READ + " = ?", new String[]{0 + ""});

        db.close();
        return result;
    }

    /**
     * 返回未读消息数据
     * @return 未读消息的数目
     */
    public int getUnReadCount() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(BaseDbContract.PushMessage.TABLE_NAME,
                null,
                BaseDbContract.PushMessage.COLUMN_NAME_READ + "=0",
                null, null, null, null);

        int count = 0;
        if(cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        db.close();

        return count;
    }

    /**
     * 删除表中当前用户的所有数据
     * @return 是否删除了数据
     */
    public boolean deleteAllMessage() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long result = db.delete(BaseDbContract.PushMessage.TABLE_NAME,
                BaseDbContract.PushMessage.COLUMN_NAME_USER + " = ?",
                new String[] {mUserAccount});

        return result > 0;
    }


    //用于辅助解析cursor
    private class MessageCourse extends CursorWrapper {
        MessageCourse(Cursor cursor) {
            super(cursor);
        }

        PushMessageModel getModel() {
            if(isBeforeFirst() || isAfterLast()) {
                return null;
            }

            PushMessageModel messageModel = new PushMessageModel();
            messageModel.setId(getInt(getColumnIndex(BaseDbContract.PushMessage._ID)));
            messageModel.setUser(getString(getColumnIndex(BaseDbContract.PushMessage.COLUMN_NAME_USER)));
            messageModel.setTitle(getString(getColumnIndex(BaseDbContract.PushMessage.COLUMN_NAME_TITLE)));
            messageModel.setMessage(getString(getColumnIndex(BaseDbContract.PushMessage.COLUMN_NAME_MESSAGE)));
            messageModel.setHasRead(getInt(getColumnIndex(BaseDbContract.PushMessage.COLUMN_NAME_READ)));
            messageModel.setTime(getString(getColumnIndex(BaseDbContract.PushMessage.COLUMN_NAME_TIME)));

            return messageModel;
        }
    }
}
