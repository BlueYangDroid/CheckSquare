package com.hisense.checksquare.command.impl;

import android.os.Environment;
import android.os.StatFs;
import android.util.SparseBooleanArray;

import com.hisense.checksquare.command.IPropCommand;
import com.hisense.checksquare.condition.ConditionMaster;
import com.hisense.checksquare.condition.IConditioner;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.entity.CheckService;
import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.LogUtil;
import com.hisense.checksquare.widget.StringUtil;

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
        LogUtil.d("CHECK_NAME_ROM enter ----> ");
        List<CheckService> checkServices = entity.checkServices;
        if (null != checkServices && !checkServices.isEmpty()) {
            SparseBooleanArray serviceResults = new SparseBooleanArray(5);
            int serviceSize = checkServices.size();
            for (int i = 0; i < serviceSize; i++) {
                CheckService service = checkServices.get(i);

                if (Constants.CHECK_SERVICE_ROM_MAXSIZE.equalsIgnoreCase(service.serviceName)) {
                    LogUtil.d("CHECK_SERVICE_ROM_MAXSIZE enter ---->");
                    long[] rom = getRom();
                    long result = rom[0] / (1024 * 1024);

                    // 1,update service Actual
                    LogUtil.d("CHECK_SERVICE_ROM_MAXSIZE: Actual = " + result);
                    service.serviceActual = String.valueOf(result);

                    // 2,update service Result
                    service = mergeSizeResult(service, result);

                    // 3,record this result
                    boolean b = CheckEntity.CHECK_STATUS_OK.equalsIgnoreCase(service.serviceResult);
                    serviceResults.put(i, b);
                    LogUtil.d("CHECK_SERVICE_ROM_MAXSIZE end <---- result = %s", b);
                }
            }// <---- end the service for{}

            // 4,update entity Result
            mergeAllResults(entity, serviceResults, serviceSize);

        } else {
            // services empty
            entity.checkResult = CheckEntity.CHECK_STATUS_FAIL;
        }

        LogUtil.d("CHECK_NAME_ROM end <---- result = %s", entity.checkResult);
        return entity;
    }

    private CheckService mergeSizeResult(CheckService service, long result) {
        IConditioner conditioner = ConditionMaster.newConditioner(service.serviceName);
        boolean b = false;
        if (null != conditioner) {
            b = conditioner.compare(service.serviceTarget, String.valueOf(result));
        }
        service.serviceResult = b? CheckEntity.CHECK_STATUS_OK: CheckEntity.CHECK_STATUS_FAIL;
        return service;
    }

    private void mergeAllResults(CheckEntity entity, SparseBooleanArray serviceResults, int serviceSize) {
        boolean isServicesOk = true;
        int resSize = serviceResults.size();
        if (resSize > 0 && resSize == serviceSize) {
            for (int i = 0; i < resSize; i++) {
                if (!serviceResults.valueAt(i)) {
                    isServicesOk = false;
                    break;
                }
            }
            if (isServicesOk) {
                entity.checkResult = CheckEntity.CHECK_STATUS_OK;
            } else {
                entity.checkResult = CheckEntity.CHECK_STATUS_FAIL;
            }
        } else {
            entity.checkResult = CheckEntity.CHECK_STATUS_FAIL;
        }
    }

    /**
     * total memory
     */
    public void getRam() {
        String str1 = "/proc/meminfo";
        String str2="";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 1024);
            while ((str2 = localBufferedReader.readLine()) != null) {
                LogUtil.d("CHECK_NAME_ROM getRamMemory(): ---- %s", str2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Rom info array
     * @return {total, Available}
     */
    public long[] getRom() {
        long[] romInfo = new long[2];
        //Total rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long total = totalBlocks * blockSize;
        romInfo[0] = total;

        //Available rom memory
        long availableBlocks = stat.getAvailableBlocks();
        long avail = blockSize * availableBlocks;
        romInfo[1] = avail;
        LogUtil.d("CHECK_NAME_ROM getRomMemroy(): ---- " + total/(1024*1024) + "/" + avail/(1024*1024));
        return romInfo;
    }

}
