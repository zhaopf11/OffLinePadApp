package com.kaihuang.bintutu.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaihuang.bintutu.MainActivity;
import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.BaseFragment;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.home.activity.ChooseUserActivity;
import com.kaihuang.bintutu.home.activity.DataListActivity;
import com.kaihuang.bintutu.home.activity.EntryDataActivity;
import com.kaihuang.bintutu.home.activity.EquipmentDetailActivity;
import com.kaihuang.bintutu.home.activity.UploadDataActivity;
import com.kaihuang.bintutu.home.activity.WifiListActivity;
import com.kaihuang.bintutu.home.msg.ScanRespMsg;
import com.kaihuang.bintutu.mine.msg.UserInfoRespMsg;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.DownloadUtil;
import com.kaihuang.bintutu.utils.PermissionsActivity;
import com.kaihuang.bintutu.utils.PermissionsChecker;
import com.kaihuang.bintutu.utils.RegexUtil;
import com.kaihuang.bintutu.utils.TimeUtils;
import com.kaihuang.bintutu.utils.ZipUtil;
import com.kaihuang.bintutu.utilviews.BluetoothListDialog;
import com.kaihuang.bintutu.utilviews.DialogChoose;
import com.kaihuang.bintutu.utilviews.DialogUtil;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;
import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {
    @Bind(R.id.text_changemachine)
    TextView text_changemachine;
    @Bind(R.id.text_wifi)
    TextView text_wifi;
    @Bind(R.id.text_changecustomer)
    TextView text_changecustomer;
    @Bind(R.id.text_chooseuser)
    TextView text_chooseuser;
    @Bind(R.id.text_related)
    TextView text_related;

    private boolean isShow;
    private String[] data = new String[]{"足型数据", "皮肤数据", "体型数据", "眼睛数据"};
    DialogChoose dialogChoose = new DialogChoose();
    DialogUtil dialogUtil = new DialogUtil();
    // 取得WifiManager对象
    private WifiManager mWifiManager;
    private ConnectivityManager cm;
    DownloadUtil downloadUtil = DownloadUtil.getInstance();
    private int progress = 0;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    public static final int LOCATION = 101;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOCATION:
                    Bundle data = msg.getData();
                    String addr = (String) data.get("location");
                    Log.e("location",addr);
                    break;
                case 100:
                    dealData("10943_1521528347443");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        ((MainActivity)getActivity()).startLocation(handler);
        mWifiManager = (WifiManager) BaseApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
        cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.show(getActivity(), "请先打开相应的权限");
                } else {
                    scanWiFi(Contants.bindingUserID);
                }
                break;
        }
    }

    /**
     * 链接wifi
     */
    @OnClick(R.id.lin_wificontact)
    public void toWifiList() {
        startActivity(new Intent(BaseApplication.getInstance(), WifiListActivity.class), false);
//        BluetoothListDialog bluetoothListDialog = new BluetoothListDialog(getActivity(), R.style.CustomDialog);
//        bluetoothListDialog.show();
    }

    @OnClick(R.id.text_changemachine)
    public void changeWifi() {
        startActivity(new Intent(BaseApplication.getInstance(), WifiListActivity.class), false);
    }

    /**
     * 更改客户
     */
    @OnClick(R.id.text_changecustomer)
    public void changeUser() {
//        dealData("10943_1521528347443");
        startActivity(new Intent(BaseApplication.getInstance(), ChooseUserActivity.class), false);
    }

    /**
     * 选择客户
     */
    @OnClick(R.id.lin_usercontact)
    public void toUserContact() {
        if (null == Contants.bindingUserID || "".equals(Contants.bindingUserID)) {
            startActivity(new Intent(BaseApplication.getInstance(), ChooseUserActivity.class), false);
        }
    }

    /**
     * 足型数据扫描
     */
    @OnClick(R.id.text_foot_data)
    public void open() {
        dialogChoose.infoDialog(getActivity(), "3D自动扫描数据", "手动输入数据", true);
        dialogChoose.setOnClick(new DialogChoose.OnClick() {
            @Override
            public void textClick1() {
                if (null == Contants.contactSSID || "".equals(Contants.contactSSID)) {
                    ToastUtils.show(getActivity(), "请选择扫描设备连接");
                    dialogChoose.dialog.dismiss();
                } else if (null == Contants.bindingUserID || "".equals(Contants.bindingUserID)) {
                    ToastUtils.show(getActivity(), "您还没有绑定用户，赶快去绑定吧");
                    dialogChoose.dialog.dismiss();
                } else {
                    mPermissionsChecker = new PermissionsChecker(getActivity());
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS_STORAGE)) {
                        PermissionsActivity.startActivityForResult(getActivity(), REQUEST_PERMISSION, PERMISSIONS_STORAGE);
                    } else {
                        scanWiFi(Contants.bindingUserID);
//                        dialogChoose.dialog.dismiss();
//                        dialogUtil.showLoadingDialog(getActivity(), "正在扫描，请稍后...");
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Looper.prepare();
//                                scanWiFi(Contants.bindingUserID);
//                            }
//                        }).start();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(1000 * 45);
//                                    handler.sendEmptyMessage(100);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
                    }
                    dialogChoose.dialog.dismiss();
                }
            }

            @Override
            public void textClick2() {
                if (null == Contants.bindingUserID || "".equals(Contants.bindingUserID)) {
                    ToastUtils.show(getActivity(), "您还没有绑定用户，赶快去绑定吧");
                } else {
                    startActivity(new Intent(BaseApplication.getInstance(), EntryDataActivity.class), false);
                }
                dialogChoose.dialog.dismiss();
            }

            @Override
            public void butonClick() {
                dialogChoose.dialog.dismiss();
            }
        });
    }

    /**
     * 上传原始足型数据文件
     */
    @OnClick(R.id.lin_updatedate)
    public void toDataList() {
        startActivity(new Intent(BaseApplication.getInstance(), DataListActivity.class), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (wifi == NetworkInfo.State.CONNECTED) {
            Contants.contactSSID = wifiInfo.getSSID().replace("\"", "");
            text_changemachine.setVisibility(View.VISIBLE);
            text_wifi.setText(Contants.contactSSID);
        } else {
            text_wifi.setText("Wi-Fi链接");
            text_changemachine.setVisibility(View.GONE);
        }
        if (null != Contants.bindingUserID && !"".equals(Contants.bindingUserID)) {
            text_changecustomer.setVisibility(View.VISIBLE);
            text_chooseuser.setText("客户：  " + Contants.bindPhone);
        } else {
            text_changecustomer.setVisibility(View.GONE);
            text_chooseuser.setText("绑定客户");
        }

        if (null != Contants.relatePhone & !"".equals(Contants.relatePhone)) {
            text_related.setVisibility(View.VISIBLE);
            text_related.setText("关联客户：  " + Contants.relatePhone);
        } else {
            text_related.setVisibility(View.GONE);
        }
    }

    private void scanWiFi(String bindUserId) {
        Map<String, Object> params = new HashMap<>();
        params.put("begin", "1");
        long currentTime = System.currentTimeMillis();
        params.put("id", bindUserId + "_" + currentTime);
        beginScan(params, bindUserId + "_" + currentTime);
    }

    private void beginScan(Map<String, Object> params, String scanId) {
        dialogUtil.showLoadingDialog(getActivity(), "正在扫描...");
        long a1 = System.currentTimeMillis();
        mHttpHelper.post(Contants.machineIP + "beginScan", params, new SpotsCallBack<ScanRespMsg>(getActivity(), "msg") {
            @Override
            public void onSuccess(Response response, ScanRespMsg scanRespMsg) {
                if (null != scanRespMsg && "1".equals(scanRespMsg.getResult())) {
                    dialogUtil.dismissLoading();
                    long b1 = System.currentTimeMillis();
                    long c = (b1 - a1) / 1000;
                    Log.e("第一个接口时间", "======" + c);
                    getScanState(scanId);
                } else {
                    ToastUtils.show(BaseApplication.getInstance(), "扫描失败，请稍后重试!");
                    dialogUtil.dismissLoading();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                dialogUtil.dismissLoading();
                ToastUtils.show(BaseApplication.getInstance(), "扫描失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                dialogUtil.dismissLoading();
                ToastUtils.show(BaseApplication.getInstance(), "扫描失败，请稍后重试");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                dialogUtil.dismissLoading();
                ToastUtils.show(BaseApplication.getInstance(), "扫描失败，请稍后重试");
            }
        });
    }

    private void getScanState(String scanId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", scanId);
        getScanState(params, scanId);
    }


    private void getScanState(Map<String, Object> params, String scanId) {
        long a2 = System.currentTimeMillis();
        mHttpHelper.post(Contants.machineIP + "requestData", params, new SpotsCallBack<ScanRespMsg>(getActivity(), "msg") {
            @Override
            public void onSuccess(Response response, ScanRespMsg scanRespMsg) {
                if (null != scanRespMsg && "1".equals(scanRespMsg.getResult())) {
                    long b2 = System.currentTimeMillis();
                    long c2 = (b2 - a2) / 1000;
                    Log.e("第二个接口时间", "======" + c2);
                    ToastUtils.show(BaseApplication.getInstance(), "扫描成功");
                    dialogUtil.dismissLoading();
                    dialogUtil.showLoadingDialog(getActivity(), "正在获取扫描数据...");
                    new Thread(new Runnable() {  //开启线程上传文件
                        @Override
                        public void run() {
                            Looper.prepare();
                            prepare(scanId);
                        }
                    }).start();
                } else {
                    ToastUtils.show(BaseApplication.getInstance(), "扫描失败，请稍后重试");
                    dismissDialog();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                dismissDialog();
                dialogUtil.dismissLoading();
                ToastUtils.show(BaseApplication.getInstance(), "扫描失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                dismissDialog();
                dialogUtil.dismissLoading();
                ToastUtils.show(BaseApplication.getInstance(), "扫描失败，请稍后重试");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                dialogUtil.dismissLoading();
                ToastUtils.show(BaseApplication.getInstance(), "扫描失败，请稍后重试");
            }
        });
    }

    String basePath = Environment.getExternalStorageDirectory().toString() + "/bintutu/bintutu/";
    List<String> newFileNames = new ArrayList<>();
    int dowloadposition = 0;
    String[] urlArr = null;
    String[] fileNames = null;

    private void prepare(String scanId) {
        String baseUrl = Contants.machineIP + scanId;
        urlArr = new String[]{baseUrl + "/0.jpg", baseUrl + "/0-1.jpg", baseUrl + "/1.jpg", baseUrl + "/1-1.jpg", baseUrl + "/2.jpg",
                baseUrl + "/2-1.jpg", baseUrl + "/3.jpg", baseUrl + "/4.jpg", baseUrl + "/footjson.txt", baseUrl + "/footjson_right.txt", baseUrl + "/output.stl", baseUrl + "/output1.stl"};
        fileNames = new String[]{"0.jpg", "0-1.jpg", "1.jpg", "1-1.jpg", "2.jpg", "2-1.jpg", "3.jpg", "4.jpg", "footjson.txt", "footjson_right.txt", "output.stl", "output1.stl"};
//        new DownloadTask().execute(urlArr, scanId, fileNames);

        dowloadposition = 0;
        downLoad(0, scanId);
    }

//    String basePath = Environment.getExternalStorageDirectory().toString() + "/bintutu/bintutu/";
//    List<String> newFileNames = null;
//    int dowloadposition = 0;
//    String[] urlArr = null;
//    String[] fileNames = null;
//
//    class DownloadTask extends AsyncTask {
//        String scanId;
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            urlArr = (String[]) objects[0];
//            scanId = (String) objects[1];
//            fileNames = (String[]) objects[2];
//            newFileNames = new ArrayList<>();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            dowloadposition = 0;
//            downLoad(0, scanId);
//        }
//    }

    private void downLoad(int position, String scanId) {
        String path = scanId + "/";
        downloadUtil.download(urlArr[position], fileNames[position], basePath + path, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path) {
                newFileNames.add(path);
                if (dowloadposition == urlArr.length - 1) {
                    dealData(scanId);
                } else {
                    dowloadposition++;
                    downLoad(dowloadposition, scanId);
                }
            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed() {
                downLoad(dowloadposition, scanId);
            }
        });
    }

    private void dealData(String scanId) {
        dialogUtil.dismissLoading();
//        if (null != newFileNames && newFileNames.size() > 0) {
        File external = Environment.getExternalStorageDirectory();
        File file = new File(external, "/bintutu/bintutu" +
                "/" + scanId + "/footjson.txt");
        File rightfile = new File(external, "/bintutu/bintutu" +
                "/" + scanId + "/footjson_right.txt");
        try {
            ZipUtil.zipFolder(new File(external, "/bintutu/bintutu/" + scanId), Environment.getExternalStorageDirectory() + "/bintutu/bintutu/" + scanId + ".zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String leftString = "";
        String rightString = "";
        if (file.exists()) {
            leftString = RegexUtil.getString(file);
        }

        if (rightfile.exists()) {
            rightString = RegexUtil.getString(rightfile);
        }

        Intent intent = new Intent(getActivity(), UploadDataActivity.class);
        if (null != rightString && !"".equals(rightString)) {
            intent.putExtra("rightjsonObject", rightString);
        } else {
            intent.putExtra("rightjsonObject", "");
        }
        if (null != leftString && !"".equals(leftString)) {
            intent.putExtra("jsonObject", leftString);
        } else {
            intent.putExtra("jsonObject", "");
        }
        intent.putExtra("scanId", scanId);
        startActivity(intent);
//        } else {
//            ToastUtils.show(BaseApplication.getInstance(), "文件下载失败");
//        }
    }
}
