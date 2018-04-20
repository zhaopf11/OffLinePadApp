package com.kaihuang.bintutu.home.activity;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.home.msg.ChooseUserRespMsg;
import com.kaihuang.bintutu.mine.activity.LoginActivity;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.RegexUtil;
import com.kaihuang.bintutu.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

public class ChooseUserActivity extends BaseActivity {

    @Bind(R.id.lin_contact)
    LinearLayout lin_contact;
    @Bind(R.id.edit_userphone)
    EditText edit_userphone;
    @Bind(R.id.edit_code)
    EditText edit_code;
    @Bind(R.id.edit_contactphone)
    EditText edit_contactphone;
    @Bind(R.id.edit_contactcode)
    EditText edit_contactcode;
    @Bind(R.id.text_getcode)
    TextView text_getcode;
    @Bind(R.id.text_getcode1)
    TextView text_getcode1;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    @Bind(R.id.text_contactphone)
    TextView text_contactphone;

    private static final int TASK_TIMER_MESSAGE = 0;
    private static final int TASK_DENIED_MESSAGE = 1;
    private static final int TASK_TIMER_RESET_MESSAGE = 2;
    private Timer mTaskTimer;
    private Timer mTaskTimer1;
    private boolean isTimerStop = false;
    private boolean isTimerStop1 = false;
    private int sendCodeFlag = 0;
    private boolean isShow = false;
    private String bindPhone;
    private String bindCode;
    private String relateCode;
    private String relatePhone;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback netcallback = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_user;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        btn_submit.setText("提交");
        initNetWork();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.text_getcode)
    public void getCode() {
        String phone = edit_userphone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(ChooseUserActivity.this, "请输入手机号");
        }else if(!RegexUtil.isMobileNO(phone)){
            ToastUtils.show(ChooseUserActivity.this, "请输入正确的手机号");
        } else {
            sendCodeFlag = 1;
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            sendCode(params);
            if (edit_code.isFocused()) {
                //已获得焦点
            } else {
                edit_code.requestFocus();//获得焦点
            }
        }
    }

    @OnClick(R.id.text_getcode1)
    public void getRelatedCode() {
        String phone = edit_contactphone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(ChooseUserActivity.this, "请输入手机号");
        }else if(!RegexUtil.isMobileNO(phone)){
            ToastUtils.show(ChooseUserActivity.this, "请输入正确的手机号");
        } else {
            sendCodeFlag = 2;
            Map<String, Object> params = new HashMap<>();
            params.put("phone", phone);
            sendCode(params);
            if (edit_contactcode.isFocused()) {
                //已获得焦点
            } else {
                edit_contactcode.requestFocus();//获得焦点
            }
        }
    }

    private final Handler timerHandler = new Handler(new Handler.Callback() {
        private int counter = 61;

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == TASK_TIMER_MESSAGE) {
                counter--;
                if (counter == 0) {
                    mTaskTimer.cancel();
                    counter = 61;
                    isTimerStop = true;
                    text_getcode.setEnabled(true);
                    text_getcode.setText(getString(R.string.send_code));
                } else {
                    text_getcode.setEnabled(false);
                    text_getcode.setText(getString(R.string.btn_hqyzm, counter));
                }
            } else if (msg.what == TASK_DENIED_MESSAGE) {
            } else if (msg.what == TASK_TIMER_RESET_MESSAGE) {
            }
            return true;
        }

    });

    private final Handler timerHandler1 = new Handler(new Handler.Callback() {
        private int counter = 61;

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == TASK_TIMER_MESSAGE) {
                counter--;
                if (counter == 0) {
                    mTaskTimer1.cancel();
                    counter = 61;
                    isTimerStop1 = true;
                    text_getcode1.setEnabled(true);
                    text_getcode1.setText(getString(R.string.send_code));
                } else {
                    text_getcode1.setEnabled(false);
                    text_getcode1.setText(getString(R.string.btn_hqyzm, counter));
                }
            } else if (msg.what == TASK_DENIED_MESSAGE) {
            } else if (msg.what == TASK_TIMER_RESET_MESSAGE) {
            }
            return true;
        }

    });

    private class ConfirmButtonTimerTask extends TimerTask {
        public ConfirmButtonTimerTask() {
            timerHandler.sendEmptyMessage(TASK_TIMER_RESET_MESSAGE);
        }

        @Override
        public void run() {
            timerHandler.sendEmptyMessage(TASK_TIMER_MESSAGE);
        }
    }


    private class ConfirmButtonTimerTask1 extends TimerTask {
        public ConfirmButtonTimerTask1() {
            timerHandler1.sendEmptyMessage(TASK_TIMER_RESET_MESSAGE);
        }

        @Override
        public void run() {
            timerHandler1.sendEmptyMessage(TASK_TIMER_MESSAGE);
        }
    }


    private void sendCode(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.sendCode, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if ("success".equals(baseRespMsg.getStatus())) {
                    if (1 == sendCodeFlag) {
                        mTaskTimer = new Timer();
                        mTaskTimer.scheduleAtFixedRate(new ConfirmButtonTimerTask(), 0, 1000);
                    } else if (2 == sendCodeFlag) {
                        mTaskTimer1 = new Timer();
                        mTaskTimer1.scheduleAtFixedRate(new ConfirmButtonTimerTask1(), 0, 1000);
                    }
                    sendCodeFlag = 0;
                    ToastUtils.show(ChooseUserActivity.this, "验证码发送成功");
                } else {
                    ToastUtils.show(ChooseUserActivity.this, "验证码发送失败，请稍后重试");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ChooseUserActivity.this, "验证码发送失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ChooseUserActivity.this, "验证码发送失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.btn_submit)
    public void subMit() {
        bindPhone = edit_userphone.getText().toString().trim();
        bindCode = edit_code.getText().toString().trim();
        relatePhone = edit_contactphone.getText().toString().trim();
        relateCode = edit_contactcode.getText().toString().trim();
        if (TextUtils.isEmpty(bindPhone)) {
            ToastUtils.show(ChooseUserActivity.this, "请输入手机号");
        } else if (!RegexUtil.isMobileNO(bindPhone)) {
            ToastUtils.show(ChooseUserActivity.this, "请输入正确的手机号");
        } else if (TextUtils.isEmpty(bindCode)) {
            ToastUtils.show(ChooseUserActivity.this, "请输入验证码");
        } else {
            if(isShow){
                if (TextUtils.isEmpty(relatePhone)) {
                    ToastUtils.show(ChooseUserActivity.this, "请输入关联手机号");
                } else if (!RegexUtil.isMobileNO(relatePhone)) {
                    ToastUtils.show(ChooseUserActivity.this, "请输入正确的关联手机号");
                } else if (TextUtils.isEmpty(relateCode)) {
                    ToastUtils.show(ChooseUserActivity.this, "请输入关联手机号的验证码");
                }else{
                    params();
                }
            }else{
                if(!isShow){
                    relatePhone ="";
                    relateCode = "";
                }
                params();
            }
        }
    }


    public void params(){
        Map<String, Object> params = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("verifycode", bindCode);
            jsonArray.put(jsonObject);
            if (!TextUtils.isEmpty(relateCode)) {
                JSONObject relateObject = new JSONObject();
                relateObject.put("verifycode", relateCode);
                jsonArray.put(relateObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("verifycodeArray", jsonArray);
        params.put("bindingPhone", bindPhone);
        params.put("relationPhone", relatePhone);
        params.put("token", BaseApplication.getInstance().getToken());
        chooseUser(params);
    }

    private void chooseUser(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.relationUser, params, new SpotsCallBack<ChooseUserRespMsg>(this) {
            @Override
            public void onSuccess(Response response, ChooseUserRespMsg chooseUserRespMsg) {
                if ("success".equals(chooseUserRespMsg.getStatus())) {
                    Contants.bindingUserID = chooseUserRespMsg.getBindingUserID();
                    Contants.relateUserFootId = chooseUserRespMsg.getUserfoottypedatarelateid();
                    Contants.bindPhone = (String) params.get("bindingPhone");
                    Contants.relatePhone = (String) params.get("relationPhone");
                    finish();
                }
                ToastUtils.show(ChooseUserActivity.this, chooseUserRespMsg.getMessage());
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(ChooseUserActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(ChooseUserActivity.this, "请求失败，请稍后重试");

            }
        });


    }

    /**
     * 初始化网络为指定网络类型
     */
    private void initNetWork(){
        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        netcallback = new ConnectivityManager.NetworkCallback() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.e("test", "已根据功能和传输类型找到合适的网络");
                // 可以通过下面代码将app接下来的请求都绑定到这个网络下请求
                if (Build.VERSION.SDK_INT >= 23) {
                    connectivityManager.bindProcessToNetwork(network);
                } else {// 23后这个方法舍弃了
                    ConnectivityManager.setProcessDefaultNetwork(network);
                }
            }
        };
        Utils.selectNetwork(connectivityManager,netcallback,ChooseUserActivity.this);
    }


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        if(netcallback != null){
            connectivityManager.unregisterNetworkCallback(netcallback);
        }
        finish();
    }

    @OnClick(R.id.text_contactphone)
    public void contactPhone() {
        if(!isShow){
            Drawable drawable = getResources().getDrawable(R.drawable.up_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            text_contactphone.setCompoundDrawables(null,null,drawable,null);
            lin_contact.setVisibility(View.VISIBLE);
            isShow = true;
        }else{
            Drawable drawable = getResources().getDrawable(R.drawable.down_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            text_contactphone.setCompoundDrawables(null,null,drawable,null);
            lin_contact.setVisibility(View.GONE);
            isShow = false;
        }
    }
}
