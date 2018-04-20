package com.kaihuang.bintutu.home.activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.home.adapter.DataListAdapter;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.PermissionsActivity;
import com.kaihuang.bintutu.utils.PermissionsChecker;
import com.kaihuang.bintutu.utils.RegexUtil;
import com.kaihuang.bintutu.utils.UploadUtil;
import com.kaihuang.bintutu.utils.Utils;
import com.kaihuang.bintutu.utilviews.DialogUtil;
import com.kaihuang.bintutu.utilviews.FullOffDecoration;
import com.kaihuang.bintutu.utilviews.UploadProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

public class DataListActivity extends BaseActivity {

    @Bind(R.id.recycler_data)
    RecyclerView recycler_data;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    private UploadProgressDialog uploadProgressDialog;

    private DataListAdapter dataListAdapter;
    DialogUtil dialogUtil = new DialogUtil();
    private Timer timer;
    private List<String> fileNames = new ArrayList<>();
    private List<String> filePaths = new ArrayList<>();
    private List<String> successFileUrl = new ArrayList<>();
    private int count = 0;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_PERMISSION = 4;  //权限请求

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_list;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        btn_submit.setText("上传");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        dataListAdapter = new DataListAdapter(BaseApplication.getInstance());
        recycler_data.setLayoutManager(linearLayoutManager);
        recycler_data.addItemDecoration(new FullOffDecoration(2));
        recycler_data.setAdapter(dataListAdapter);

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {
        uploadProgressDialog = new UploadProgressDialog();
        mPermissionsChecker = new PermissionsChecker(this);
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS_STORAGE)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                    PERMISSIONS_STORAGE);
        } else {
            getDataList();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.show(DataListActivity.this, "请先打开相应的权限");
                } else {
                    getDataList();
                }
                break;
        }
    }

    private void getDataList() {
        File external = Environment.getExternalStorageDirectory();
        File file = new File(external, "/bintutu/bintutu/");
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (null != files && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (!files[i].isDirectory() && files[i].getName().contains(".zip")) {
                            fileNames.add(files[i].getName());
                            filePaths.add(files[i].getAbsolutePath());
                        }
                    }
                }
                if (null != fileNames && fileNames.size() > 0) {
                    dataListAdapter.footnums = fileNames;
                    dataListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    int uploadPosition = 0;

    @OnClick(R.id.btn_submit)
    public void upLoad() {
        if (null != filePaths && filePaths.size() > 0) {
            dialogUtil.infoDialog(DataListActivity.this, "上传3D原始扫描数据", "请在链接Wi-Fi情况下上传", true, true);
            dialogUtil.setOnClick(new DialogUtil.OnClick() {
                @Override
                public void leftClick() {
                    dialogUtil.dialog.dismiss();
                }

                @Override
                public void rightClick() {
                    if (!Utils.isWifiNetworkConnected(DataListActivity.this)) {
                        ToastUtils.show(DataListActivity.this, "请在wifi情况下上传");
                    } else {
                        uploadUtil.setUploadListener(new UploadUtil.UploadListener() {
                            @Override
                            public void uploadListener(String result) {
                                int what = 0;
                                if (null != result && !"".equals(result)) {
                                    successFileUrl.add(result);
                                    if ((uploadPosition + 1) == filePaths.size() / 2) {
                                        uploadProgressDialog.setProgress(5000);
                                    }
                                    if (uploadPosition < filePaths.size() - 1) {
                                        uploadPosition++;
                                        upLoadFile(new File(filePaths.get(uploadPosition)));
                                    } else {
                                        what = 1;
                                    }
                                } else {
                                    what = 2;

                                }
                                handler.sendEmptyMessage(what);
                            }
                        });
                        uploadProgressDialog.uploadProgress(DataListActivity.this);
                        upLoadFile(new File(filePaths.get(uploadPosition)));
                        dialogUtil.dialog.dismiss();
                    }
                }
            });
        } else {
            ToastUtils.show(DataListActivity.this, "暂无原始数据");
        }


    }

    UploadUtil uploadUtil = new UploadUtil();

    private void upLoadFile(File file) {
        new Thread(new Runnable() {  //开启线程上传文件
            @Override
            public void run() {
                Looper.prepare();
                uploadUtil.uploadFile(file, Contants.UPLOAD_URL);

            }
        }).start();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    uploadProgressDialog.setProgress(10000);
                    Map<String, Object> params = new HashMap<>();
                    JSONArray jsonArray = new JSONArray();
                    for (String string : successFileUrl) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("path", string);
                            jsonObject.put("equipmentID", "5");
                            jsonObject.put("salesmanID", BaseApplication.getInstance().getUserID());
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != jsonArray && jsonArray.length() > 0) {
                        params.put("initialFootDataJsonArray", jsonArray);
                        params.put("token", BaseApplication.getInstance().getToken());
                        params.put("salesmanID", BaseApplication.getInstance().getUserID());
                        upLoadInfo(params);
                    }
                    break;
                case 2:
                    uploadProgressDialog.dialog.dismiss();
                    ToastUtils.show(DataListActivity.this, "上传失败，请稍后重试");
                    break;
            }

        }
    };


    private void upLoadInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.API.uploadInitialFootData, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                ToastUtils.show(DataListActivity.this, baseRespMsg.getMessage());
                uploadProgressDialog.dialog.dismiss();
                if ("success".equals(baseRespMsg.getStatus())) {
                    RegexUtil.deleteFile();
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                uploadProgressDialog.dialog.dismiss();
                ToastUtils.show(DataListActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                uploadProgressDialog.dialog.dismiss();
                ToastUtils.show(DataListActivity.this, "请求失败，请稍后重试");

            }
        });

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
