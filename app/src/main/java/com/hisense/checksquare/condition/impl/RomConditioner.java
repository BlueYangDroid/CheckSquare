package com.hisense.checksquare.condition.impl;

import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.log.StringUtil;

/**
 * Created by yanglijun.ex on 2017/2/24.
 */

public class RomConditioner extends CommonConditioner{

    /**
     * case Constants.CHECK_ITEM_ROM_MAXSIZE
     */
    private String mType;

    public RomConditioner() {
    }

    public RomConditioner(String type) {
        this.mType = type;
    }

    @Override
    public boolean compare(String target, String actual) {

        if (Constants.CHECK_ITEM_ROM_MAXSIZE.equalsIgnoreCase(mType)) {
            return compareRomSize(target, actual);
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
