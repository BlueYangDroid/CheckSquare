package com.hisense.checksquare.command.impl;

import android.os.Environment;
import android.os.StatFs;
import android.util.SparseBooleanArray;

import com.hisense.checksquare.command.IPropCommand;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.entity.CheckService;
import com.hisense.checksquare.widget.CommandUtil;
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

        getRom();
        LogUtil.d("CHECK_NAME_ROM end <---- result = %s", entity.checkResult);

        return entity;
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
