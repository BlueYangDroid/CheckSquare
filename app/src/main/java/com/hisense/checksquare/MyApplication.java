package com.hisense.checksquare;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.hisense.checksquare.widget.log.LogUtil;

import java.util.Stack;

/**
 * Created by yanglijun.ex on 2017/2/14.
 */

public class MyApplication extends Application {
    private static MyApplication app;
    public Stack<Activity> store;

    public static MyApplication getInstance() {
        return app;
    }

    public static Context getContext(){
        return app.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // logger init
        if (BuildConfig.DEBUG) {
            LogUtil.init("LogTAG");
            LogUtil.saveFile(true);
        }
        // activity life cycle manager
        store = new Stack<>();
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
    }

    private class SwitchBackgroundCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            store.add(activity);
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
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurActivity() {
        return store.lastElement();
    }
}
