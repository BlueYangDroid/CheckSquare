package com.hisense.checksquare.dagger.mcmd;

import com.hisense.checksquare.command.impl.CpuCommand;
import com.hisense.checksquare.command.IPropCommand;
import com.hisense.checksquare.command.impl.RomCommand;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yanglijun.ex on 2017/2/16.
 */

@Module
public class CommandModule {

    @Provides
    @Named("CpuCommand")
    IPropCommand provideCpuCommand(){
        return new CpuCommand();
    }

    @Provides
    @Named("RomCommand")
    IPropCommand provideRomCommand(){
        return new RomCommand();
    }
}
