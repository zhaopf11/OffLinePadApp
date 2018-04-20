package com.kaihuang.bintutu.utilviews;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.OkHttpHelper;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.home.adapter.FootDescDetailAdapter;
import com.kaihuang.bintutu.utils.BitmapUtils;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.PermissionsActivity;
import com.kaihuang.bintutu.utils.PermissionsChecker;
import com.kaihuang.bintutu.utils.UploadUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

/**
 * Created by zhaopf on 2018/2/28.
 */

public class PictureTagActivity extends Activity implements UploadUtil.UploadListener, PictureTagLayout.OnButtonClick {
    @Bind(R.id.picture_tag_view)
    PictureTagLayout picture_tag_view;
    @Bind(R.id.recyclerView_desc)
    RecyclerView recyclerView_desc;
    @Bind(R.id.lin_pre_step)
    LinearLayout lin_pre_step;
    @Bind(R.id.lin_clear_tag)
    LinearLayout lin_clear_tag;

    private FootDescDetailAdapter mFootDescDetailAdapter;
    private String fileName;
    private String leftRightFoot;
    private int position;
    private String editType;//1 点击一张足型示例图编辑保存图片
    private String userFootTypeDataID;//足型数据id
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private File pictureTagDir = null;
    private File external = Environment.getExternalStorageDirectory();
    private String rootDir = "/" + "bintutuImgage";
    DialogUtil dialogUtil = new DialogUtil();
    private OkHttpHelper okHttpHelper = new OkHttpHelper();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                File nfile = (File) msg.obj;
                dialogUtil.showLoadingDialog(PictureTagActivity.this, "正在保存...");
                new Thread(new Runnable() {  //开启线程上传文件
                    @Override
                    public void run() {
                        Looper.prepare();
                        UploadUtil uploadUtil = new UploadUtil();
                        uploadUtil.setUploadListener(PictureTagActivity.this);
                        uploadUtil.uploadFile(nfile, Contants.upload_image, "FootdataLabelImage");
                    }
                }).start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_tag);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#000000"));
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mPermissionsChecker = new PermissionsChecker(this);
        editType = getIntent().getStringExtra("editType");
        userFootTypeDataID = getIntent().getStringExtra("userFootTypeDataID");
        Integer imageId = getIntent().getIntExtra("imageId", 0);
        leftRightFoot = getIntent().getStringExtra("leftRightFoot");
        position = getIntent().getIntExtra("position", 1);
        if (imageId != 0) {
            Bitmap bitmap = BitmapUtils.drawable2Bitmap(getResources().getDrawable(imageId));
            picture_tag_view.setBackground(BitmapUtils.bitmap2Drawable(bitmap));
        }

        picture_tag_view.setOnButtonClick(this);

        //细节描述
        mFootDescDetailAdapter = new FootDescDetailAdapter(BaseApplication.getInstance(), "pictureTag");
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(BaseApplication.getInstance()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        linearLayoutManager2.setReverseLayout(false);
        recyclerView_desc.setLayoutManager(linearLayoutManager2);
        recyclerView_desc.setAdapter(mFootDescDetailAdapter);
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity();
            } else {
                establishFile(external, rootDir);
            }
        } else {
            establishFile(external, rootDir);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("=====onResume====", "onResume");
        mFootDescDetailAdapter.descList = picture_tag_view.descList;
        mFootDescDetailAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onResume();
        Log.e("=====onPause====", "onPause");
    }

    @OnClick(R.id.text_save)
    public void saveData() {
        if (picture_tag_view.descList != null && picture_tag_view.descList.size() > 0) {
            Bitmap bm = BitmapUtils.loadBitmapFromView(picture_tag_view);
            switch (position) {
                //第三 第四张图片进行旋转，区分左右脚
                case 2:
                    if ("1".equals(leftRightFoot)) {
                        //左脚内侧旋转90度
                        bm = BitmapUtils.rotateBitmap(bm, 90);
                    } else if ("2".equals(leftRightFoot)) {
                        //右脚内侧旋转-90度
                        bm = BitmapUtils.rotateBitmap(bm, -90);
                    }
                    break;
                case 3:
                    if ("1".equals(leftRightFoot)) {
                        //左脚外侧旋转-90度
                        bm = BitmapUtils.rotateBitmap(bm, -90);
                    } else if ("2".equals(leftRightFoot)) {
                        //右脚外侧旋转90度
                        bm = BitmapUtils.rotateBitmap(bm, 90);
                    }
                    break;
            }
            fileName = Contants.bindingUserID + "_" + System.currentTimeMillis() + ".png";
            String filePath = pictureTagDir.getPath() + File.separator + fileName;
            try {
                bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
                File file = new File(filePath);
                Message message = new Message();
                message.obj = file;
                message.what = 100;
                handler.sendMessage(message);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.show(BaseApplication.getInstance(), "您还没有标点描述哦！不能保存");
        }
    }

    private void establishFile(File external, String rootDir) {
        pictureTagDir = new File(external, rootDir + "/pictureTag");
        if (!pictureTagDir.exists()) {
            pictureTagDir.mkdirs();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.show(this, "如果拒绝访问权限，部分功能可能无法使用");
                    finish();
                } else {
                    establishFile(external, rootDir);
                }
                break;
        }
    }

    @OnClick(R.id.text_cancle)
    public void cancle() {
        finish();
    }

    @OnClick(R.id.text_pre_step)
    public void preStep() {
        picture_tag_view.removeAllViews();
        picture_tag_view.removeTagView();
    }

    @OnClick(R.id.text_clear_tag)
    public void clearTag() {
        picture_tag_view.removeAllViews();
        picture_tag_view.tagViewList.clear();
        picture_tag_view.descList.clear();

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void uploadListener(String result) {
        dialogUtil.dialogLoading.dismiss();
        if (null != result && !"".equals(result)) {
            ToastUtils.show(this, "上传成功");
            Intent intent = new Intent();
            intent.putExtra("fileName", fileName);
            intent.putExtra("leftRightFoot", leftRightFoot);
            intent.putExtra("position", position);
            intent.putStringArrayListExtra("descList", picture_tag_view.descList);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            ToastUtils.show(this, "上传失败，请稍后重试");
        }
        Looper.loop();
    }

    /**
     * 单张图片的信息保存
     */
    private void sendMessage() {
        Map<String, Object> params = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", "83");
            jsonObject.put("uid", Contants.bindingUserID);
            jsonObject.put("token", BaseApplication.getInstance().getToken());
            jsonObject.put("footshape_data_id", userFootTypeDataID);
            jsonObject.put("foot_label_image", fileName);
            if ("1".equals(leftRightFoot)) {
                position = position + 1;
            } else if ("2".equals(leftRightFoot)) {
                position = position + 5;
            }
            jsonObject.put("foot_label_number", position);
            JSONArray jsonArray = new JSONArray();
            if (picture_tag_view.descList != null && picture_tag_view.descList.size() > 0) {
                for (String ss : picture_tag_view.descList) {
                    JSONObject jb = new JSONObject();
                    jb.put("footshape_label_desc", ss);
                    jsonArray.put(jb);
                }
            }
            jsonObject.put("footshape_label_desc_List", jsonArray);
            params.put("sendmsg", jsonObject.toString());
            savePictureMessage(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void savePictureMessage(Map<String, Object> params) {
        okHttpHelper.post(Contants.BASE_URL, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg != null) {
//                    if (baseRespMsg.getResult() > 0) {
//                        ToastUtils.show(BaseApplication.getInstance(), "保存成功");
//                        finish();
//                    } else {
//                        ToastUtils.show(BaseApplication.getInstance(), baseRespMsg.getRetmsg());
//                    }
                } else {
                    ToastUtils.show(BaseApplication.getInstance(), "操作失败，请稍后重试");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
            }
        });
    }

    @Override
    public void openInputDialog() {
        InputPictureDialog inputPictureDialog = new InputPictureDialog(this, R.style.CustomDialog);
        inputPictureDialog.setOnButtonClick(new InputPictureDialog.OnButtonClick() {
            @Override
            public void cancleClick() {
                picture_tag_view.descList.add("");
                picture_tag_view.removeAllViews();
                picture_tag_view.removeTagView();
                mFootDescDetailAdapter.descList = picture_tag_view.descList;
                mFootDescDetailAdapter.notifyDataSetChanged();
                inputPictureDialog.dismiss();
            }

            @Override
            public void sureClick(String desc) {
                if (!TextUtils.isEmpty(desc)) {//内容为空不允许添加标记
                    picture_tag_view.descList.add(desc);
                    mFootDescDetailAdapter.descList = picture_tag_view.descList;
                    mFootDescDetailAdapter.notifyDataSetChanged();
                    inputPictureDialog.dismiss();
                } else {
                    ToastUtils.show(PictureTagActivity.this, "输入内容不能为空");
                }
            }
        });
        inputPictureDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        inputPictureDialog.show();
    }
}
