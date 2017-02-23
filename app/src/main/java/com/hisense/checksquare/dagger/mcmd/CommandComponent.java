package com.hisense.checksquare.dagger.mcmd;

import com.hisense.checksquare.command.CheckCommandRouter;
import com.hisense.checksquare.command.IPropCommand;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yanglijun.ex on 2017/2/16.
 */

@Singleton    //定义注解范围
@Component(modules = {CommandModule.class})
public interface CommandComponent {
    void inject(CheckCommandRouter router);

    @Named("RomCommand")
    IPropCommand romCommand();

    @Named("CpuCommand")
    IPropCommand cpuCommand();
}
