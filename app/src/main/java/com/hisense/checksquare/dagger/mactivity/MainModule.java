package com.hisense.checksquare.dagger.mactivity;

import com.hisense.checksquare.base.IMainConstract;
import com.hisense.checksquare.dagger.PerActivity;
import com.hisense.checksquare.mdomain.MainDomain;
import com.hisense.checksquare.vpresenter.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yanglijun.ex on 2017/2/16.
 */

@Module
public class MainModule {

    private IMainConstract.IMainView activity;

    public MainModule(IMainConstract.IMainView ui) {
        this.activity = ui;
    }

    @Provides
    @PerActivity
    IMainConstract.IMainView provideMainActivity(){
        return activity;
    }

    @Provides
    @PerActivity
    MainPresenter provideMainPresenter(IMainConstract.IMainView ui, IMainConstract.IMainDomain domain){
        return new MainPresenter(ui,domain);
    }

    @Provides
    @PerActivity
    IMainConstract.IMainDomain provideMainDomain(){
        return new MainDomain();
    }
}
