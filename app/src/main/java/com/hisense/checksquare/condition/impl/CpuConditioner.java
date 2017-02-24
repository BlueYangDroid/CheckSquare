package com.hisense.checksquare.condition.impl;

import com.hisense.checksquare.condition.IConditioner;
import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.LogUtil;
import com.hisense.checksquare.widget.StringUtil;

/**
 * Created by yanglijun.ex on 2017/2/24.
 */

public class CpuConditioner implements IConditioner{

    /**
     * case Constants.CHECK_SERVICE_CPU_NUM
     * case Constants.CHECK_SERVICE_CPU_MAXFREQ
     */
    private String mType;

    public CpuConditioner(String type) {
        this.mType = type;
    }

    @Override
    public boolean compare(String target, String result) {

        if (Constants.CHECK_SERVICE_CPU_NUM.equalsIgnoreCase(mType)) {
            return compareCpuNum(target, result);
        } else if (Constants.CHECK_SERVICE_CPU_MAXFREQ.equalsIgnoreCase(mType)){
            return compareCpuFreq(target, result);
        }
        return false;
    }

    /**
     * target is Float
     * @param target
     * @param result
     * @return
     */
    private boolean compareCpuFreq(String target, String result) {
        if (!StringUtil.isEmpty(target, result)) {
            if (Float.parseFloat(result) > Float.parseFloat(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * target is Int
     * @param target
     * @param result
     * @return
     */
    private boolean compareCpuNum(String target, String result) {
        if (!StringUtil.isEmpty(target, result)) {
            if (Integer.parseInt(result) >= Integer.parseInt(target)) {
                return true;
            }
        }
        return false;
    }
}
