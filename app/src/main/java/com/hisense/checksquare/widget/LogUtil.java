package com.hisense.checksquare.widget;

import com.orhanobut.logger.Logger;

/**
 * Created by yanglijun.ex on 2017/2/22.
 */

public class LogUtil {
    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void e(String message, Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }
}
