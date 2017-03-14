package com.hisense.checksquare.widget.log;

import android.util.Log;

/**
 * Created by T.F Guo on 2017/3/3.
 */

public class LogClient implements ILogClient {
    private String tag;
    private boolean mSaveFlag;

    @Override
    public void saveFile(boolean flag) {
        mSaveFlag = flag;
    }

    @Override
    public void init(String t) {
        tag = t;
    }

    @Override
    public ILogClient tag(String t) {
        this.tag = t;
        return this;
    }

    @Override
    public void d(CharSequence message, Object... args) {
        Log.d(tag, String.format(message.toString(), args));
    }

    @Override
    public void d(Object object) {
        Log.d(tag, object.toString());
    }

    @Override
    public void e(CharSequence message, Object... args) {
        Log.e(tag, String.format(message.toString(), args));
    }

    @Override
    public void e(Object object) {
        Log.e(tag, object.toString());
    }

    @Override
    public void close() {

    }
}
