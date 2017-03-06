package com.hisense.checksquare.condition;

import com.hisense.checksquare.command.impl.CpuCommand;
import com.hisense.checksquare.condition.impl.CommonConditioner;
import com.hisense.checksquare.condition.impl.CpuConditioner;
import com.hisense.checksquare.condition.impl.RomConditioner;
import com.hisense.checksquare.widget.Constants;

import java.util.Comparator;

/**
 * 条件计算、判断器的管理类
 * Created by yanglijun.ex on 2017/2/24.
 */

public class ConditionMaster {

    /**
     * new 一个 common conditioner 调节器，将按照float类型对参数进行转换计算
     * @return
     */
    public synchronized static IConditioner newConditioner() {
        return newConditioner(CommonConditioner.class);
    }

    /**
     * 分发条件判断器件
     * @return
     */
    public synchronized static IConditioner newConditioner(Class conClazz) {
        IConditioner conditioner = null;

        if (CpuConditioner.class.getSimpleName().equalsIgnoreCase(conClazz.getSimpleName())) {
            conditioner = new CpuConditioner();
        } else if ("RomConditioner".equalsIgnoreCase(conClazz.getSimpleName())) {
            conditioner = new RomConditioner();
        } else {
            conditioner = new CommonConditioner();
        }
        return conditioner;
    }
}
