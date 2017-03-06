package com.hisense.checksquare.widget.log;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by T.F Guo on 2017/3/3.
 */

public class LoggerClient implements ILogClient {
    public void init(String tag) {
        Logger.init(tag)            // default PRETTYLOGGER or use just init()
                .methodCount(2)                 // default 2
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(2)                // default 0
        ; //default AndroidLogAdapter

    }

    @Override
    public ILogClient tag(String tag) {
        Logger.t(tag);
        return this;
    }

    @Override
    public void d(String message, Object... args) {
        Logger.d(message, args);
    }

    @Override
    public void d(Object object) {
        Logger.d(object);
    }

    @Override
    public void e(String message, Object... args) {
        Logger.e(null, message, args);
    }

    @Override
    public void e(Object object) {
        Logger.e(object.toString(), "");
    }

    @Override
    public void close() {
        Logger.init().logLevel(LogLevel.NONE);       // default LogLevel.FULL
    }
}
