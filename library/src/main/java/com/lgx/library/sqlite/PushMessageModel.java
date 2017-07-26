package com.lgx.library.sqlite;

import java.io.Serializable;

/**
 * Created by liugaoxin on 2017/7/18.
 * 该类用于参考，没有实际作用
 */

public class PushMessageModel implements Serializable {
    private int id;
    private String user;
    private String title;
    private String message;
    private boolean hasRead;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = (hasRead != 0);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
