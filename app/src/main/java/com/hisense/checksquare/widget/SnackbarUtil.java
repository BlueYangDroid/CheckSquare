package com.hisense.checksquare.widget;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.hisense.checksquare.MyApplication;

/**
 * Created by yanglijun.ex on 2017/3/10.
 */

public class SnackbarUtil {
    /**
     * Make a Snackbar to display a message at the top Activity
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    public static Snackbar make(String text, int duration) {
        return Snackbar.make(MyApplication.getInstance().getCurActivity().getWindow().getDecorView()
                , text, duration);
    }
}
