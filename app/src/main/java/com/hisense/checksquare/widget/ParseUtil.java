package com.hisense.checksquare.widget;

import com.bluelinelabs.logansquare.LoganSquare;
import com.hisense.checksquare.widget.log.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by yanglijun.ex on 2017/2/24.
 */

public class ParseUtil {

    /**
     * Parse a parameterized object from an InputStream.
     *
     * @param is The InputStream, most likely from your networking library.
     */
    public static <E> E parse(InputStream is, Class<E> jsonObjectClass) {
        try {
            return LoganSquare.parse(is, jsonObjectClass);
        } catch (IOException e) {
            LogUtil.e(e.toString());
        }
        return null;
    }

    /**
     * Parse a parameterized object from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     */
    public static <E> E parse(String jsonString, Class<E> jsonObjectClass) {
        try {
            return LoganSquare.parse(jsonString, jsonObjectClass);
        } catch (IOException e) {
            LogUtil.e(e.toString());
        }
        return null;
    }

    /**
     * Parse a list of objects from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     * @param jsonObjectClass The @JsonObject class to parse the InputStream into
     */
    public static <E> List<E> parseList(String jsonString, Class<E> jsonObjectClass) {
        try {
            return LoganSquare.parseList(jsonString, jsonObjectClass);
        } catch (IOException e) {
            LogUtil.e(e.toString());
        }
        return null;
    }


    /**
     * Serialize an object to a JSON String.
     *
     * @param object The object to serialize.
     */
    @SuppressWarnings("unchecked")
    public static <E> String serialize(E object) {
        try {
            return LoganSquare.serialize(object);
        } catch (IOException e) {
            LogUtil.e(e.toString());
        }
        return null;
    }


    /**
     * Serialize a list of objects to a JSON String.
     *
     * @param list The list of objects to serialize.
     * @param jsonObjectClass The @JsonObject class of the list elements
     */
    public static <E> String serialize(List<E> list, Class<E> jsonObjectClass) {
        try {
            return LoganSquare.serialize(list, jsonObjectClass);
        } catch (IOException e) {
            LogUtil.e(e.toString());
        }
        return null;
    }

}
