package com.hisense.checksquare.condition.impl;

import com.hisense.checksquare.condition.IConditioner;
import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.StringUtil;

/**
 * Created by yanglijun.ex on 2017/2/24.
 */

public class RomConditioner implements IConditioner{

    /**
     * case Constants.CHECK_SERVICE_ROM_MAXSIZE
     */
    private String mType;

    public RomConditioner(String type) {
        this.mType = type;
    }

    @Override
    public boolean compare(String target, String result) {

        if (Constants.CHECK_SERVICE_ROM_MAXSIZE.equalsIgnoreCase(mType)) {
            return compareRomSize(target, result);
        }
        return false;
    }

    /**
     * target is Long
     * @param target
     * @param result
     * @return
     */
    private boolean compareRomSize(String target, String result) {
        if (!StringUtil.isEmpty(target, result)) {
            if (Long.parseLong(result) > Long.parseLong(target)) {
                return true;
            }
        }
        return false;
    }
}
