package com.kaihuang.bintutu;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.bean.Tab;
import com.kaihuang.bintutu.home.HomeFragment;
import com.kaihuang.bintutu.mine.MineFragment;
import com.kaihuang.bintutu.utils.PermissionsChecker;
import com.kaihuang.bintutu.utilviews.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

import jameson.io.library.util.ToastUtils;

public class MainActivity extends BaseActivity {

    private List<Tab> mTabs = new ArrayList<>(2);
    private LayoutInflater mInflater;

    public FragmentTabHost mTabhost;
    private long exitTime = 0;
    private MineFragment mineFragment;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener(new Handler());
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private boolean isFirstLocation = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        Tab tab_home = new Tab(HomeFragment.class, R.string.tab_home, R.drawable.tab_home);
        Tab tab_mine = new Tab(MineFragment.class, R.string.tab_mine, R.drawable.tab_mine);
        mTabs.add(tab_home);
        mTabs.add(tab_mine);
        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.fram_tabcontent);

        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabhost.addTab(tabSpec, tab.getFragment(), null);
        }
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);
        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(getString(R.string.tab_mine)) ) {
                    refData();
                }
            }
        });
    }

    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }

    private void refData() {
        if (null ==mineFragment ) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.tab_mine));
            if (fragment != null) {
                mineFragment = (MineFragment) fragment;
                mineFragment.refData();
            }
        } else {
            mineFragment.refData();
        }
    }
    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {
        mPermissionsChecker = new PermissionsChecker(this);
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFirstLocation = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                ToastUtils.show(this, "没有权限,请手动开启定位权限");
            } else {
                if (mLocationClient != null && mLocationClient.isStarted()) {
                    mLocationClient.requestLocation();
                } else {
                    mLocationClient.start();
                }
            }
        }else{
            mLocationClient.start();
        }
    }

    @Override
    protected void onDestroy() {
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回键将退出宾兔兔", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startLocation(Handler handler) {
        myListener = new MyLocationListener(handler);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        private Handler handler;

        public MyLocationListener(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            if(location != null){
                if(isFirstLocation){
                    String addr = location.getAddrStr();    //获取详细地址信息
                    String country = location.getCountry();    //获取国家
                    String province = location.getProvince();    //获取省份
                    String city = location.getCity();    //获取城市
                    String district = location.getDistrict();    //获取区县
                    String street = location.getStreet();    //获取街道信息
                    Message msg = handler.obtainMessage();
                    Bundle data = new Bundle();
                    data.putString("location", province + city + district + street);
                    msg.setData(data);
                    msg.what = HomeFragment.LOCATION;
                    handler.sendMessage(msg);
                }
            }else{
                ToastUtils.show(MainActivity.this,"定位失败");
            }
        }
    }
}
