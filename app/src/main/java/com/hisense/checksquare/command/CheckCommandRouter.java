package com.hisense.checksquare.command;

import android.os.SystemClock;

import com.hisense.checksquare.dagger.mcmd.CommandComponent;
import com.hisense.checksquare.dagger.mcmd.DaggerCommandComponent;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.widget.Constants;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;

/**
 * Created by yanglijun.ex on 2017/2/21.
 */

public class CheckCommandRouter {

    @Inject @Named("CpuCommand") Lazy<IPropCommand> mCpuCommand;
    @Inject @Named("RomCommand") Lazy<IPropCommand> mRomCommand;

    public CheckCommandRouter() {
        CommandComponent commandComponent = DaggerCommandComponent.create();
        commandComponent.inject(this);
    }


    public CheckEntity checkProp(CheckEntity entity){
        if (Constants.CHECK_NAME_ROM.equalsIgnoreCase(entity.checkName)) {


            entity = mRomCommand.get().checkProp(entity);

        } else if (Constants.CHECK_NAME_CPU.equalsIgnoreCase(entity.checkName)) {
            entity = mCpuCommand.get().checkProp(entity);

        }

        return entity;
    }
}
