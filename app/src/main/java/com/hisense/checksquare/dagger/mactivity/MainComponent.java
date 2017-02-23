package com.hisense.checksquare.dagger.mactivity;

import com.hisense.checksquare.dagger.PerActivity;
import com.hisense.checksquare.vpresenter.MainActivity;

import dagger.Component;

/**
 * Created by yanglijun.ex on 2017/2/16.
 */

@PerActivity    //定义注解范围
@Component(modules = {MainModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
