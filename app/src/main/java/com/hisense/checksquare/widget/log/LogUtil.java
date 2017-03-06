package com.hisense.checksquare.widget.log;

import android.util.Log;

import com.hisense.checksquare.widget.StringUtil;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by yanglijun.ex on 2017/2/22.
 */

public class LogUtil {

    private static ILogClient client = new LoggerClient();
    private static String defaultTag;

    public static ILogClient tag(String tag) {
        defaultTag = tag;
        return client.tag(tag);
    }

    public static void d(Object object) {
        d(null, object);
    }

    public static void d(String tag, Object object) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        client.tag(tag).d(object);
    }

    public static void d(String message, Object... args) {
        d(null, message, args);
    }

    public static void d(String tag, String message, Object... args) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        client.tag(tag).d(message, args);
    }

    public static void e(String message, Object... args) {
        e(null, message, args);
    }

    public static void e(String tag, String message, Object... args) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        client.tag(tag).e(message, args);
    }

    public static void e(Object object) {
        e(null, object);
    }

    public static void e(String tag, Object object) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        client.tag(tag).e(object);
    }

    public static void init(String tag) {
        defaultTag = tag;
        client.init(tag);
    }

    public static void setClient(ILogClient c){
        client = c;
    }

    public static void close() {
        client.close();
    }
}
