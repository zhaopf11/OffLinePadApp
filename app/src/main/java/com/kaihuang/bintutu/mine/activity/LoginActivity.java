package com.kaihuang.bintutu.mine.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kaihuang.bintutu.MainActivity;
import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.home.msg.LoginRespMsg;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.RegexUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.radiogroup)
    RadioGroup radiogroup;
    @Bind(R.id.radiobtn_passlogin)
    RadioButton radiobtn_passlogin;
    @Bind(R.id.radiobtn_quick)
    RadioButton radiobtn_quick;

    @Bind(R.id.edit_account)
    EditText edit_account;
    @Bind(R.id.edit_code)
    EditText edit_code;
    @Bind(R.id.text_sendcode)
    TextView text_sendcode;
    @Bind(R.id.btn_next)
    Button btn_next;
    private boolean isPassLogin = true;

    private static final int TASK_TIMER_MESSAGE = 0;
    private static final int TASK_DENIED_MESSAGE = 1;
    private static final int TASK_TIMER_RESET_MESSAGE = 2;
    private Timer mTaskTimer;
    private boolean isTimerStop = false;
    private long exitTime = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (i) {
                case R.id.radiobtn_passlogin:
                    radiobtn_quick.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_36));
                    radiobtn_passlogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_60));
                    text_sendcode.setVisibility(View.GONE);
                    edit_code.setInputType(InputType.TYPE_CLASS_NUMBER);
                    edit_code.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edit_code.setHint("输入密码");
                    isPassLogin = true;
                    break;
                case R.id.radiobtn_quick:
                    radiobtn_quick.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_60));
                    radiobtn_passlogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_36));
                    text_sendcode.setVisibility(View.VISIBLE);
                    edit_code.setInputType(InputType.TYPE_CLASS_NUMBER);
                    edit_code.setHint("输入验证码");
                    isPassLogin = false;
                    break;
            }
        }
    };


    @Override
    protected void initEvents() {
        radiogroup.setOnCheckedChangeListener(onCheckedChangeListener);
        edit_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                isEnable();

            }
        });

        edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                isEnable();
            }
        });
    }


    private void isEnable() {
        String account = edit_account.getText().toString().trim();
        String code = edit_code.getText().toString().trim();
        if (isPassLogin) {
//            && code.length() > 5
            if (11 == account.length()) {
                btn_next.setEnabled(true);
            } else {
                btn_next.setEnabled(false);
            }
        } else {
            if (11 == account.length()) {
                text_sendcode.setTextColor(getResources().getColor(R.color.font_orange));
                text_sendcode.setBackgroundResource(R.drawable.orange_corner_bg);
                if (code.length() > 5) {
                    btn_next.setEnabled(true);
                }
            } else {
                text_sendcode.setTextColor(getResources().getColor(R.color.gray));
                text_sendcode.setBackgroundResource(R.drawable.gray_corner_bg1);
            }

        }
    }


    @OnClick(R.id.text_sendcode)
    public void getCode() {
        String account = edit_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(LoginActivity.this, "请输入手机号");
        }else if(!RegexUtil.isMobileNO(account)){
            ToastUtils.show(LoginActivity.this, "请输入正确的手机号");
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("phone", account);
            sendCode(params);
            if (edit_code.isFocused()) {
                //已获得焦点
            } else {
                edit_code.requestFocus();//获得焦点
                edit_code.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                    text_sendcode.setEnabled(true);
                    text_sendcode.setText(getString(R.string.send_code));
                } else {
                    text_sendcode.setEnabled(false);
                    text_sendcode.setText(getString(R.string.btn_hqyzm, counter));
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

    @OnClick(R.id.btn_next)
    public void login() {
//        BaseApplication.getInstance().putToken("", "20");
//        startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), false);
        String account = edit_account.getText().toString().trim();
        String code = edit_code.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(LoginActivity.this, "请输入手机号码");
        } else if (!RegexUtil.isMobileNO(account)) {
            ToastUtils.show(LoginActivity.this, "请输入正确的手机号码");
        } else if (TextUtils.isEmpty(code)) {
            if (isPassLogin) {
                ToastUtils.show(LoginActivity.this, "请输入密码");
            } else {
                ToastUtils.show(LoginActivity.this, "请输入验证码");
            }
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("phone", account);
            if (isPassLogin) {
                params.put("password", code);
                login(params);
            } else {
                params.put("verifycode", code);
                codeLogin(params);
            }
        }
    }

    private void sendCode(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.sendCode, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if ("success".equals(baseRespMsg.getStatus())) {
                    mTaskTimer = new Timer();
                    mTaskTimer.scheduleAtFixedRate(new ConfirmButtonTimerTask(), 0, 1000);
                    ToastUtils.show(LoginActivity.this, "验证码发送成功");
                } else {
                    ToastUtils.show(LoginActivity.this, "验证码发送失败，请稍后重试");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(LoginActivity.this, "验证码发送失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(LoginActivity.this, "验证码发送失败，请稍后重试");
            }
        });
    }

    private void codeLogin(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.phoneCodeLogin, params, new SpotsCallBack<LoginRespMsg>(this,"msg") {
            @Override
            public void onSuccess(Response response, LoginRespMsg loginRespMsg) {
                if ("success".equals(loginRespMsg.getStatus())) {
                    if (null != loginRespMsg.getData()) {
                        BaseApplication.getInstance().putToken(loginRespMsg.getData().getToken(), loginRespMsg.getData().getSalesmanID());
                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), false);
                    ToastUtils.show(LoginActivity.this, "登录成功");
                    finish();
                } else {
                    ToastUtils.show(LoginActivity.this, loginRespMsg.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(LoginActivity.this, "登录失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(LoginActivity.this, "登录失败，请稍后重试");
            }
        });


    }

    private void login(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.phonePassLogin, params, new SpotsCallBack<LoginRespMsg>(this) {
            @Override
            public void onSuccess(Response response, LoginRespMsg loginRespMsg) {
                if ("success".equals(loginRespMsg.getStatus())) {
                    if (null != loginRespMsg.getData()) {
                        BaseApplication.getInstance().putToken(loginRespMsg.getData().getToken(), loginRespMsg.getData().getSalesmanID());
                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), false);
                    ToastUtils.show(LoginActivity.this, "登录成功");
                    finish();
                } else {
                    ToastUtils.show(LoginActivity.this, loginRespMsg.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(LoginActivity.this, "登录失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(LoginActivity.this, "登录失败，请稍后重试");
            }
        });
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

    @Override
    protected void initData() {

    }
}
