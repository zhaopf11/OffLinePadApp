package com.kaihuang.bintutu.common.dagger;

import android.app.Application;
import android.content.res.Resources;


import com.kaihuang.bintutu.common.BaseApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhoux on 2017/7/11.
 */

@Module
public class BaseApplicationModule {


    private final BaseApplication bApp;



    public BaseApplicationModule(BaseApplication bApp) {
        this.bApp = bApp;
    }
    @Provides
    @Singleton
    protected Application provideApplication(){
        return bApp;
    }


    @Provides
    @Singleton
    protected Resources provideResources() {
        return bApp.getResources();
    }
}
