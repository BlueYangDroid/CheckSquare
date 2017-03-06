package com.hisense.checksquare.command.impl;

import com.hisense.checksquare.command.IPropCommand;
import com.hisense.checksquare.condition.ConditionMaster;
import com.hisense.checksquare.condition.IConditioner;
import com.hisense.checksquare.condition.impl.CpuConditioner;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.widget.CommandUtil;
import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.FileUtil;
import com.hisense.checksquare.widget.log.LogUtil;
import com.hisense.checksquare.widget.StringUtil;

/**
 * Created by yanglijun.ex on 2017/2/23.
 */

public class CpuCommand implements IPropCommand {

    private static final String[] COMMAND_CPU_MAX_FREQ = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};

    @Override
    public CheckEntity checkProp(CheckEntity entity) {
        LogUtil.d("CpuCommand checkProp(): enter ----> ");
        CheckEntity.ConditionMap conditionMap = entity.conditionMap;
        if (null != conditionMap) {

            if (Constants.CHECK_ITEM_CPU_MAXFREQ.equalsIgnoreCase(entity.checkName)) {
                LogUtil.d("CHECK_ITEM_CPU_MAXFREQ enter ---->");
                float actual = getMaxFreq();
                // 1,update Actual
                entity.actualValue = String.valueOf(actual);

                // 2,update status
                entity.checkStatus = mergeFreqResult(conditionMap, actual);

            } else if (Constants.CHECK_ITEM_CPU_NUM.equalsIgnoreCase(entity.checkName)) {
                LogUtil.d("CHECK_ITEM_CPU_NUM enter ---->");
                int cpuNum = getCpuNum();
                // 1,update service Actual
                entity.actualValue = String.valueOf(cpuNum);

                // 2,update service Result
                entity.checkStatus = mergeCpuNumResult(conditionMap, cpuNum);
            }
        } else {
            // conditionMap empty
            entity.checkStatus = CheckEntity.CHECK_STATUS_FAIL;
        }

        LogUtil.d("CpuCommand checkProp() end <---- result = %s", entity.checkStatus);
        return entity;
    }

    private String mergeCpuNumResult(CheckEntity.ConditionMap conditions, float actual) {
        LogUtil.d("mergeCpuNumResult(): enter --> actual =  %f, conditions = %s",actual ,conditions);
        IConditioner conditioner = ConditionMaster.newConditioner(CpuConditioner.class);
        boolean b = false;
        if (null != conditioner) {
            b = conditioner.compareConditionsFloat(conditions,actual);
        }
        String status = b? CheckEntity.CHECK_STATUS_OK: CheckEntity.CHECK_STATUS_FAIL;
        LogUtil.d("mergeCpuNumResult(): <-- compare = " + b);
        return status;
    }

    private String mergeFreqResult(CheckEntity.ConditionMap conditions, float actual) {
        LogUtil.d("mergeFreqResult(): enter --> actual =  %f, conditions = %s",actual ,conditions);
        IConditioner iConditioner = ConditionMaster.newConditioner(CpuConditioner.class);
        boolean b = false;
        if (null != iConditioner) {
            b = iConditioner.compareConditionsFloat(conditions, actual);
        }
        String status = b? CheckEntity.CHECK_STATUS_OK: CheckEntity.CHECK_STATUS_FAIL;
        LogUtil.d("mergeFreqResult(): <-- compare = " + b);
        return status;
    }

    // 获取CPU核心数
    private int getCpuNum() {
        String cpuStr = FileUtil.readFile("/proc/cpuinfo");
        String strPocessor = "processor";
        LogUtil.d("getCpuNum(): <-- getCpuNum(): cpuStr = %s", cpuStr);
        return StringUtil.stringNumbers(cpuStr, strPocessor);
    }

    private float getMaxFreq() {
        String strResult = CommandUtil.processCommand(COMMAND_CPU_MAX_FREQ).trim();
        long parseLong = Long.parseLong(strResult);
        return (float) parseLong / (1000 * 1000);
    }
}
