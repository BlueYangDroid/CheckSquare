package com.hisense.checksquare.condition;

import com.hisense.checksquare.command.impl.CpuCommand;
import com.hisense.checksquare.condition.impl.CpuConditioner;
import com.hisense.checksquare.condition.impl.RomConditioner;
import com.hisense.checksquare.widget.Constants;

import java.util.Comparator;

/**
 * 条件计算、判断器的管理类
 * Created by yanglijun.ex on 2017/2/24.
 */

public class ConditionMaster {

    public synchronized static IConditioner newConditioner(String serviceName) {
        IConditioner conditioner = null;
        switch (serviceName) {
            case Constants.CHECK_SERVICE_CPU_NUM:
            case Constants.CHECK_SERVICE_CPU_MAXFREQ:
                conditioner = new CpuConditioner(serviceName);
                break;

            case Constants.CHECK_SERVICE_ROM_MAXSIZE:
                conditioner = new RomConditioner(serviceName);
                break;
            default:
                break;
        }
        return conditioner;
    }
}
