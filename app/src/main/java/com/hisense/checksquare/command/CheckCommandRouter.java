package com.hisense.checksquare.command;

import com.hisense.checksquare.dagger.mcmd.CommandComponent;
import com.hisense.checksquare.dagger.mcmd.DaggerCommandComponent;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.log.LogUtil;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;

/**
 * Created by yanglijun.ex on 2017/2/21.
 */

public class CheckCommandRouter {

    @Inject @Named("CpuCommand") Lazy<IPropCommand> mCpuCommand;
    @Inject @Named("RomCommand") Lazy<IPropCommand> mRomCommand;
    private final CommandComponent commandComponent;

    public CheckCommandRouter() {
        commandComponent = DaggerCommandComponent.create();
        commandComponent.inject(this);
    }

    /**
     * 检查命令转发具体Command实现类
     * @param entity
     * @return
     */
    public CheckEntity checkProp(CheckEntity entity){
        CheckEntity resultEntity = null;
        String checkName = entity.checkName;
        if (null != checkName && null != commandComponent) {
            switch (checkName) {
                case Constants.CHECK_ITEM_CPU_NUM:
                case Constants.CHECK_ITEM_CPU_MAXFREQ:
                    IPropCommand cpuCommand = commandComponent.cpuCommand();
                    resultEntity = cpuCommand.checkProp(entity);
                    break;
                case Constants.CHECK_ITEM_ROM_MAXSIZE:
                    IPropCommand romCommand = commandComponent.romCommand();
                    resultEntity = romCommand.checkProp(entity);
                    break;
                default:
                    LogUtil.e("checkProp(): checkName cant find !");
                    break;

            }
        }
        LogUtil.d("checkProp(): <---- resultEntity = " + resultEntity);
        return resultEntity;
    }
}
