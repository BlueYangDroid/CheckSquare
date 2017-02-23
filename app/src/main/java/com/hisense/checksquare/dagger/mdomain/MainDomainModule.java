package com.hisense.checksquare.dagger.mdomain;

import com.hisense.checksquare.dagger.PerDomain;
import com.hisense.checksquare.command.CheckCommandRouter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yanglijun.ex on 2017/2/16.
 */

@Module
public class MainDomainModule {

    @Provides
    @PerDomain
    CheckCommandRouter provideCheckCommand(){
        return new CheckCommandRouter();
    }
}
