package com.hisense.checksquare.widget.log;

/**
 * Created by T.F Guo on 2017/3/3.
 */

public interface ILogClient {

    void init(String tag);

    void saveFile(boolean flag);

    ILogClient tag(String tag);

    void d(CharSequence message, Object... args);

    void d(Object object);

    void e(CharSequence message, Object... args);

    void e(Object object);

    void close();
}
