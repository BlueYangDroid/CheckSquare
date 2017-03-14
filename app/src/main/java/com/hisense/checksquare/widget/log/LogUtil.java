package com.hisense.checksquare.widget.log;

/**
 * Created by yanglijun.ex on 2017/2/22.
 */

public class LogUtil {

    private static ILogClient client = new XLogClient();
    private static String defaultTag;

    public static ILogClient tag(String tag) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        return client.tag(tag);
    }

    public static void dObj(Object object) {
        dObj("", object);
    }

    public static void dObj(String tag, Object object) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        client.tag(tag).d(object);
    }

    public static void d(String msg) {
        d("", msg);
    }

    public static void d(String message, Object... args) {
        d("", message, args);
    }

    public static void d(String tag, String message, Object... args) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        client.tag(tag).d(message, args);
    }

    public static void eObj(Object object) {
        eObj("", object);
    }

    public static void eObj(String tag, Object object) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        client.tag(tag).e(object);
    }

    public static void e(String msg) {
        e("", msg);
    }

    public static void e(String message, Object... args) {
        e("", message, args);
    }

    public static void e(String tag, String message, Object... args) {
        if (StringUtil.isEmpty(tag)) {
            tag = defaultTag;
        }
        client.tag(tag).e(message, args);
    }

    public static void init(String tag) {
        defaultTag = tag;
        client.init(tag);
    }

    public static void saveFile(boolean flag) {
        client.saveFile(flag);
    }

    public static void setClient(ILogClient c){
        client = c;
    }

    public static void close() {
        client.close();
    }
}
