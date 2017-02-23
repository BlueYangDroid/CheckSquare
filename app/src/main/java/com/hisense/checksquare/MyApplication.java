package com.hisense.checksquare;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by yanglijun.ex on 2017/2/14.
 */

public class MyApplication extends Application {
    private static MyApplication app;

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
        if (BuildConfig.DEBUG) {
            Logger.init("CheckSquare")            // default PRETTYLOGGER or use just init()
                    .methodCount(5)                 // default 2
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(2)                // default 0
            ; //default AndroidLogAdapter
        }
    }
}
