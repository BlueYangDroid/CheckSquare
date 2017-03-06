package com.hisense.checksquare.widget;

import com.hisense.checksquare.widget.log.LogUtil;

/**
 * Created by yanglijun.ex on 2017/2/22.
 */

public class StringUtil {
    public static boolean isEmpty(String... args){
        if (null != args && args.length > 0) {
            for (String str : args) {
                if (str == null || str.length() == 0) {
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
