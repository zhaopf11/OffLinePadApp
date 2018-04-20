package com.kaihuang.bintutu.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.http.OkHttpHelper;
import com.kaihuang.bintutu.utils.ActivityManager;
import com.kaihuang.bintutu.utilviews.DialogUtil;

import javax.inject.Inject;


public abstract class BaseActivity extends AppCompatActivity  {

    protected BaseApplication mApplication;
    protected Activity mActivity;
    @Inject
    public OkHttpHelper mHttpHelper;
    DialogUtil dialogUtil = new DialogUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mApplication = (BaseApplication) getApplication();
        mActivity = this;
        BaseApplication.component().inject(this);
        ActivityManager.getInstance().addActivity(this);
        getIntentBundle();//上一个Activity传递过来的Bundle

        //找到资源文件的XML
        if(getLayoutId()!=0){
            View vContent = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
            ((FrameLayout)findViewById(R.id.frame_content)).addView(vContent);
        }

        initViews();// 加载正文内容 默认显示头部
        initEvents();
        initData();
    }

    /**
     * 初始化Activity的常用变量 举例:
     * <b>mLayoutResID=页面XML资源ID 必须存在的</b>
     */
    protected abstract int getLayoutId();

    /**
     * 上一个Activity传递过来的Bundle
     */
    protected void getIntentBundle(){};

    /** 初始化视图 **/
    protected abstract void initViews();

    /** 初始化事件 **/
    protected abstract void initEvents();

    /** 初始化内容 **/
    protected abstract void initData();

    public void startActivity(Intent intent, boolean isNeedLogin) {


        if (isNeedLogin) {

//            User user = BaseApplication.getInstance().getUser();
//            if (user != null) {
//                super.startActivity(intent);
//            } else {
//
//                BaseApplication.getInstance().putIntent(intent);
//                Intent loginIntent = new Intent(this
//                        , LoginActivity.class);
//                super.startActivity(loginIntent);
//
//            }
//
        } else {
            super.startActivity(intent);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
    }

}
