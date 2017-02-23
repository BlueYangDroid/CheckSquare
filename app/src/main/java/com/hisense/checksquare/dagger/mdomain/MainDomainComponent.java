package com.hisense.checksquare.dagger.mdomain;

import com.hisense.checksquare.command.CheckCommandRouter;
import com.hisense.checksquare.dagger.PerDomain;
import com.hisense.checksquare.mdomain.MainDomain;
import com.hisense.checksquare.vpresenter.MainActivity;

import dagger.Component;

/**
 * Created by yanglijun.ex on 2017/2/16.
 */

@PerDomain    //定义注解范围
@Component(modules = {MainDomainModule.class})
public interface MainDomainComponent {
    void inject(MainDomain domain);
    CheckCommandRouter mCheckRouter();
}
