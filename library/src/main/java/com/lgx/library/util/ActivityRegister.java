package com.lgx.library.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.LinkedList;

/**
 * A simple Activity stack tool.offer a top activity while should show a dialog
 * Created by lzh on 2017/2/10.
 */
public class ActivityRegister implements Application.ActivityLifecycleCallbacks {
    private static ActivityRegister manager = new ActivityRegister();
    private LinkedList<Activity> stack = new LinkedList<>();

    public static ActivityRegister get () {
        return manager;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (!stack.contains(activity)) {
            stack.add(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (stack.contains(activity)) {
            stack.remove(activity);
        }
    }

    public Activity topActivity () {
        Activity activity = null;
        if (!stack.isEmpty()) {
            activity = stack.getLast();
        }
        return activity;
    }

    /**
     * Register this to {@link Application}
     * @param context Application context
     */
    public void registerSelf(Context context) {
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(ActivityRegister.get());
    }
}
