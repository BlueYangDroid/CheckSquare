package com.hisense.checksquare.widget;

/**
 * Created by yanglijun.ex on 2017/2/14.
 */

import android.content.Context;
import android.content.SharedPreferences;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtil {


    private static final String FILE_NAME = "property";

    /**
     * 保存数据
     *
     * @param context
     * @param keyName
     * @param value
     */
    public static void put(Context context, String keyName, Object value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        if (value instanceof String) {
            editor.putString(keyName, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(keyName, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(keyName, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(keyName, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(keyName, (Long) value);
        } else {
            editor.putString(keyName, value.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 获取数据
     *
     * @param context
     * @param keyName
     * @param defaultValue 默认值
     * @return
     */
    public static Object get(Context context, String keyName, Object defaultValue) {
        SharedPreferences sp = getSharedPreferences(context);
        if (defaultValue instanceof String) {
            return sp.getString(keyName, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sp.getInt(keyName, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(keyName, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(keyName, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(keyName, (Long) defaultValue);
        }
        return null;
    }


    /**
     * 移除某个key值对应的值
     *
     * @param context
     * @param keyName
     */
    public static void remove(Context context, String keyName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(keyName);
        SharedPreferencesCompat.apply(editor);
    }

    /** 清除所有数据 */
    public static void clear(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param keyName
     * @return
     */
    public static boolean contains(Context context, String keyName) {
        return getSharedPreferences(context).contains(keyName);
    }

    /** 返回所有的键值对 */
    public static Map<String, ?> getAll(Context context) {
        return getSharedPreferences(context).getAll();
    }


    /** 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类 */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /** 反射查找apply的方法 */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        /** 如果找到则使用apply执行，否则使用commit */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }

    /**
     * @param context
     */
    private static SharedPreferences getSharedPreferences(Context context) {

        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

}
