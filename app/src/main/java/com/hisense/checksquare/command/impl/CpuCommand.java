package com.hisense.checksquare.command.impl;

import android.util.SparseBooleanArray;

import com.hisense.checksquare.command.IPropCommand;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.entity.CheckService;
import com.hisense.checksquare.widget.CommandUtil;
import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.LogUtil;
import com.hisense.checksquare.widget.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by yanglijun.ex on 2017/2/23.
 */

public class CpuCommand implements IPropCommand {

    private static final String[] COMMAND_CPU_MAX_FREQ = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};

    @Override
    public CheckEntity checkProp(CheckEntity entity) {
        LogUtil.d("CHECK_NAME_CPU enter ----> ");
        List<CheckService> checkServices = entity.checkServices;
        if (null != checkServices && !checkServices.isEmpty()) {
            SparseBooleanArray serviceResults = new SparseBooleanArray(5);
            int serviceSize = checkServices.size();
            for (int i = 0; i< serviceSize; i++) {
                CheckService service = checkServices.get(i);

                if (Constants.CHECK_SERVICE_CPU_MAXFREQ.equalsIgnoreCase(service.serviceName)) {
                    LogUtil.d("CHECK_SERVICE_CPU_MAXFREQ enter ---->");
                    String strResult = CommandUtil.processCommand(COMMAND_CPU_MAX_FREQ).trim();
                    long parseLong = Long.parseLong(strResult);
                    float result = (float) parseLong / (1000 * 1000);
                    // 1,update service Actual
                    LogUtil.d("CHECK_SERVICE_CPU_MAXFREQ: Actual = %f", result);
                    service.serviceActual = String.valueOf(result);

                    // 2,update service Result
                    String serviceTarget = service.serviceTarget;
                    String serviceUnit = service.serviceUnit;
                    String format = service.format;
                    if (!StringUtil.isEmpty(serviceTarget, serviceUnit)
                            && "G".equalsIgnoreCase(serviceUnit)
                            && "FLOAT".equalsIgnoreCase(format)) {
                        if (result > Float.parseFloat(serviceTarget)) {
                            service.serviceResult = CheckEntity.CHECK_STATUS_OK;
                        } else {
                            service.serviceResult = CheckEntity.CHECK_STATUS_FAIL;
                        }
                    } else {
                        service.serviceResult = CheckEntity.CHECK_STATUS_FAIL;
                    }
                    boolean b = CheckEntity.CHECK_STATUS_OK.equalsIgnoreCase(service.serviceResult);
                    serviceResults.put(i, b);
                    LogUtil.d("CHECK_SERVICE_CPU_MAXFREQ end <---- result = %s", b);

                } else if (Constants.CHECK_SERVICE_CPU_NUM.equalsIgnoreCase(service.serviceName)) {
                    LogUtil.d("CHECK_SERVICE_CPU_NUM enter ---->");
                    int cpuNum = getCpuNum();
                    // 1,update service Actual
                    service.serviceActual = String.valueOf(cpuNum);

                    // 2,update service Result
                    if (cpuNum > 3) {
                        service.serviceResult = CheckEntity.CHECK_STATUS_OK;
                    } else {
                        service.serviceResult = CheckEntity.CHECK_STATUS_FAIL;
                    }
                    boolean b = CheckEntity.CHECK_STATUS_OK.equalsIgnoreCase(service.serviceResult);
                    serviceResults.put(i, b);
                    LogUtil.d("CHECK_SERVICE_CPU_NUM end <---- result = %d", cpuNum);
                }
            }// <---- end the service for{}

            // 3,update entity Result
            checkServiceResults(entity, serviceResults, serviceSize);

        } else {
            // services empty
            entity.checkResult = CheckEntity.CHECK_STATUS_FAIL;
        }

        LogUtil.d("CHECK_NAME_CPU end <---- result = %s", entity.checkResult);
        return entity;
    }

    private void checkServiceResults(CheckEntity entity, SparseBooleanArray serviceResults, int serviceSize) {
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

    // 获取CPU核心数
    public int getCpuNum() {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr =  new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            String cpuStr = builder.toString();
            String strPocessor = "processor";
            LogUtil.d("CHECK_NAME_CPU getCpuNum(): cpuStr = %s", cpuStr);
            return StringUtil.stringNumbers(cpuStr, strPocessor);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }
}
