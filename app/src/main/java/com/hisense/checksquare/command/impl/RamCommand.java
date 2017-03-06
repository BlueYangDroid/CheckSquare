package com.hisense.checksquare.command.impl;

import com.hisense.checksquare.command.IPropCommand;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.widget.log.LogUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by T.F Guo on 2017/3/3.
 */

public class RamCommand implements IPropCommand {
    @Override
    public CheckEntity checkProp(CheckEntity entity) {

        // TODO: 2017/3/3
        return null;
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
                LogUtil.d("CHECK_NAME_RAM getRamMemory(): ---- %s", str2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
