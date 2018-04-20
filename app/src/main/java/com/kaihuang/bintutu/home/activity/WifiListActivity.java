package com.kaihuang.bintutu.home.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.home.adapter.WifiListAdapter;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.PermissionsActivity;
import com.kaihuang.bintutu.utils.PermissionsChecker;
import com.kaihuang.bintutu.utils.Utils;
import com.kaihuang.bintutu.utils.WifiAdminUtils;
import com.kaihuang.bintutu.utils.WifiConnectUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;

public class WifiListActivity extends BaseActivity {


    @Bind(R.id.recycler_wifi)
    RecyclerView recycler_wifi;

    private WifiListAdapter wifiListAdapter = null;
    /**
     * 扫描到的可用WiFi列表
     */
    private List<ScanResult> mScanResults = new ArrayList<>();
    private static final int REFRESH_CONN = 100;
    // Wifi管理类
    private WifiAdminUtils mWifiAdmin;
    //下标
    private int mPosition;
    private WifiReceiver mReceiver;
    private WifiManager mWifiManager;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_WIFI_STATE"};
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_list;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiListAdapter = new WifiListAdapter(BaseApplication.getInstance());
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {
        wifiListAdapter.setOnItemClickLitener(new WifiListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                String ssid = mScanResults.get(position).SSID;
//                startActivity(new Intent(BaseApplication.getInstance(), EquipmentDetailActivity.class).putExtra("ssid", ssid), false);
            }

            @Override
            public void conntactClick(int position) {
                mPosition = position;
                ScanResult scanResult = mScanResults.get(mPosition);
                String capabilities = scanResult.capabilities;
                WifiConnectUtils.WifiCipherType type = null;
                if (!TextUtils.isEmpty(capabilities)) {
                    if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                        type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WPA;
                    } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                        type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WEP;
                    } else {
                        type = WifiConnectUtils.WifiCipherType.WIFICIPHER_NOPASS;
                    }
                }
                WifiAdminUtils wifiAdminUtils = new WifiAdminUtils(WifiListActivity.this);
                WifiConfiguration config = wifiAdminUtils.isExsits(scanResult.SSID);
                if(type == WifiConnectUtils.WifiCipherType.WIFICIPHER_NOPASS){
                    //不需要密码
                    isConnectSelf(scanResult);
                }else{
                    //输入密码
                    if(config != null && config.hiddenSSID){
                        boolean isconnect = wifiAdminUtils.addNetWork(config);
                        if(!isconnect){
                            ToastUtils.show(WifiListActivity.this,"链接失败");
                        }
                        getWifiListInfo();
                    }else{
                        alertDialog(WifiListActivity.this,scanResult,type);
                    }
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_wifi.setLayoutManager(linearLayoutManager);
        recycler_wifi.setAdapter(wifiListAdapter);
        //请求权限
        mWifiAdmin = new WifiAdminUtils(WifiListActivity.this);
        mPermissionsChecker = new PermissionsChecker(this);
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS_STORAGE)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION, PERMISSIONS_STORAGE);
        } else {
            // 获得Wifi列表信息
            getWifiListInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.show(WifiListActivity.this, "请先打开相应的权限");
                } else {
                    getWifiListInfo();
                }
                break;
        }
    }

    private void registerReceiver() {
        mReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    /**
     * 得到wifi的列表信息
     */
    private void getWifiListInfo() {
        mWifiAdmin.startScan();
        List<ScanResult> tmpList = mWifiAdmin.getWifiList();
        if (tmpList == null) {
            mScanResults.clear();
        } else {
            mScanResults = tmpList;
        }
        wifiListAdapter.mScanResults = mScanResults;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wifiListAdapter.notifyDataSetChanged();
    }

    /**
     * 输入wifi密码弹框
     * @param context
     */
    private void alertDialog(Context context,ScanResult scanResult ,WifiConnectUtils.WifiCipherType type) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.dialog_edittext, null);
        EditText edit_wifipass = (EditText) dv.findViewById(R.id.edit_wifipass);
        TextView text_dismiss = (TextView) dv.findViewById(R.id.text_dismiss);
        TextView text_sure = (TextView) dv.findViewById(R.id.text_sure);

        text_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        text_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = edit_wifipass.getText().toString().trim();
                if (TextUtils.isEmpty(pass)) {
                    ToastUtils.show(context, "请输入wifi密码");
                } else {
                    setConnect(scanResult,pass,type);
                    Utils.downSoftInput(context,edit_wifipass);
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();
    }

    /**
     * 有密码验证连接
     *
     * @param scanResult
     */
    private void setConnect(ScanResult scanResult, String pass,WifiConnectUtils.WifiCipherType type) {
        if (mWifiAdmin.isConnect(scanResult)) {
            // 已连接，显示连接状态对话框
        } else {
            // 去连接网络
            WifiAdminUtils mWifiAdmin = new WifiAdminUtils(WifiListActivity.this);
            boolean isConnect = mWifiAdmin.connect(scanResult.SSID, pass, type);
            Log.i("WifiListActivity", isConnect + "是否去连接的值");
            if(!isConnect){
                ToastUtils.show(WifiListActivity.this,"链接失败");
            }
            getWifiListInfo();
        }
    }

    /**
     * 无密码直连
     * @param scanResult
     */
    private void isConnectSelf(ScanResult scanResult) {
        if (mWifiAdmin.isConnect(scanResult)) {
            // 已连接，显示连接状态对话框
        } else {
            boolean iswifi = mWifiAdmin.connectSpecificAP(scanResult);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (iswifi) {
                Toast.makeText(WifiListActivity.this, "连接成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WifiListActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 取消广播
     */
    private void unregisterReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private class WifiReceiver extends BroadcastReceiver {
        protected static final String TAG = "MainActivity";
        //记录网络断开的状态
        private boolean isDisConnected = false;
        //记录正在连接的状态
        private boolean isConnecting = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {// wifi连接上与否
                Log.e(TAG, "网络已经改变");
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    if (!isDisConnected) {
                        Log.e(TAG, "wifi已经断开");
                        isDisConnected = true;
                        Contants.contactSSID = "";
                    }
                } else if (info.getState().equals(NetworkInfo.State.CONNECTING)) {
                    if (!isConnecting) {
                        Log.e(TAG, "正在连接...");
                        isConnecting = true;
                    }
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    Log.e(TAG, "连接到网络：" + wifiInfo.getSSID());
                    ToastUtils.show(context,"链接wifi到" + wifiInfo.getSSID() + "");
                    getWifiListInfo();
                }
            } else if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
                switch (error) {
                    case WifiManager.ERROR_AUTHENTICATING:
                        Log.e(TAG, "密码认证错误Code为：" + error);
                        Toast.makeText(getApplicationContext(), "wifi密码错误！", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                Log.e("H3c", "wifiState" + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.e(TAG, "wifi正在启用");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.e(TAG, "Wi-Fi已启用。");
                        break;
                }
            }
        }
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
