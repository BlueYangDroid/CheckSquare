package com.hisense.checksquare.command.impl;

import android.os.Environment;
import android.os.StatFs;
import android.util.SparseBooleanArray;

import com.hisense.checksquare.command.IPropCommand;
import com.hisense.checksquare.condition.ConditionMaster;
import com.hisense.checksquare.condition.IConditioner;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.log.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by yanglijun.ex on 2017/2/23.
 */

public class RomCommand implements IPropCommand {


    @Override
    public CheckEntity checkProp(CheckEntity entity) {
        LogUtil.d("RomCommand checkProp(): enter ----> ");
        CheckEntity.ConditionMap conditionMap = entity.conditionMap;
        if (null != conditionMap) {
            if (Constants.CHECK_ITEM_ROM_MAXSIZE.equalsIgnoreCase(entity.checkName)) {
                LogUtil.d("CHECK_ITEM_ROM_MAXSIZE enter ---->");
                float[] rom = getRom();
                float result = rom[0] / (1024 * 1024);

                // 1,update service Actual
                LogUtil.d("CHECK_SERVICE_ROM_MAXSIZE: Actual = " + result);
                entity.actualValue = String.valueOf(result);

                // 2,update service Result
                entity.checkStatus = mergeSizeResult(conditionMap, result);

            }


        } else {
            // services empty
            entity.checkStatus = CheckEntity.CHECK_STATUS_FAIL;
        }

        LogUtil.d("RomCommand checkProp(): end <---- result = %s", entity.checkStatus);
        return entity;
    }

    private String mergeSizeResult(CheckEntity.ConditionMap conditions, float actual) {
        LogUtil.d("mergeSizeResult(): enter --> actual =  %f, conditions = %s",actual ,conditions);
        IConditioner conditioner = ConditionMaster.newConditioner(RomCommand.class);
        boolean b = false;
        if (null != conditioner) {
            b = conditioner.compareConditionsFloat(conditions,actual);
        }
        String status = b? CheckEntity.CHECK_STATUS_OK: CheckEntity.CHECK_STATUS_FAIL;
        LogUtil.d("mergeSizeResult(): <-- compare = " + b);
        return status;
    }


    /**
     * Rom info array
     * @return {total, Available}
     */
    public float[] getRom() {
        float[] romInfo = new float[2];
        //Total rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long total = totalBlocks * blockSize;
        romInfo[0] = (float) total;

        //Available rom memory
        long availableBlocks = stat.getAvailableBlocks();
        long avail = blockSize * availableBlocks;
        romInfo[1] = (float) avail;
        LogUtil.d("CHECK_NAME_ROM getRomMemroy(): ---- " + total/(1024*1024) + "/" + avail/(1024*1024));
        return romInfo;
    }

}
