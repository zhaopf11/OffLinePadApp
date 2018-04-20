package com.kaihuang.bintutu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.mine.activity.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动页
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                if (null != BaseApplication.getInstance().getToken() && !"".equals(BaseApplication.getInstance().getToken()) && null != BaseApplication.getInstance().getUserID() && !"".equals(BaseApplication.getInstance().getUserID())) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                finish();
            }
        }, 1000);
    }

}
