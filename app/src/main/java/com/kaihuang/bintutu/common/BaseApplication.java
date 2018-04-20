package com.kaihuang.bintutu.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.kaihuang.bintutu.common.dagger.BaseApplicationComponent;
import com.kaihuang.bintutu.common.dagger.DaggerBaseApplicationComponent;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.UserLocalData;
import com.kaihuang.bintutu.utils.WifiAdminUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.xiaoneng.uiapi.Ntalker;

public class BaseApplication extends Application {
    private static BaseApplicationComponent appComponent;

    private static BaseApplication mInstance;
    public static Context appContext;

    private String token;
    private String userID;
    private WifiAdminUtils mWifiAdmin;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        mInstance = this;
        buildComponentAndInject();
        initToken();
        int enableDebug = Ntalker.getBaseInstance().enableDebug(true);
        int initSDK = Ntalker.getBaseInstance().initSDK(getApplicationContext(), Contants.siteid, Contants.sdkkey);
        mWifiAdmin = new WifiAdminUtils(getApplicationContext());

    }


    private void initToken() {
        this.token = UserLocalData.getToken(this);
        this.userID = UserLocalData.getUserID(this);

    }

    public String getToken() {
        return token;
    }

    public String getUserID(){
        return userID;
    }


    public void putToken(String token,String userID){
        this.token = token;
        this.userID = userID;
        UserLocalData.putToken(this,token);
        UserLocalData.putUserID(this,userID);
    }




    public void clearToken(){
        this.token = "";
        this.userID ="";
        UserLocalData.clearUserID(this);
        UserLocalData.clearToken(this);
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    public static BaseApplicationComponent component() {
        return appComponent;
    }

    public static void buildComponentAndInject() {
        appComponent = DaggerBaseApplicationComponent.Initializer.init(mInstance);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
