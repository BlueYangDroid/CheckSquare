package com.hisense.checksquare.widget.log;

import android.text.TextUtils;

import com.hisense.checksquare.widget.log.LogUtil;

/**
 * Created by yanglijun.ex on 2017/2/22.
 */

public class StringUtil {
    public static boolean isEmpty(CharSequence... args){
        if (null != args && args.length > 0) {
            for (CharSequence str : args) {
                if (TextUtils.isEmpty(str)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private static int counter = 0;
    public static int stringNumbers(String parent, String child) {
        counter = 0;
        return stringNumbersAgain(parent, child);
    }

    private static int stringNumbersAgain(String parent, String child) {
        if (!parent.contains(child))
        {
            return 0;
        }
        else if(parent.contains(child))
        {
            counter++;
            stringNumbersAgain(parent.substring(parent.indexOf(child) + child.length()), child);
            LogUtil.d("stringNumbers(): counter = %d", counter);
            return counter;
        }
        return 0;
    }
}
