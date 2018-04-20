package com.kaihuang.bintutu.mine.activity;

import android.view.View;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.mine.bean.UserInfoBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoActivity extends BaseActivity {


    @Bind(R.id.text_username)
    TextView text_username;
    @Bind(R.id.text_identifycode)
    TextView text_identifycode;
    @Bind(R.id.text_phonenum)
    TextView text_phonenum;
    @Bind(R.id.text_hastock)
    TextView text_hastock;
    @Bind(R.id.text_type)
    TextView text_type;

    private UserInfoBean userInfoBean = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {
        userInfoBean = (UserInfoBean) getIntent().getSerializableExtra("userInfo");
        if (null != userInfoBean) {
            text_username.setText(userInfoBean.getName());
            text_identifycode.setText(userInfoBean.getIDCard());
            text_phonenum.setText(userInfoBean.getPhone());
            text_hastock.setText(userInfoBean.getCount() + "单");
            if (null != userInfoBean.getStoreName() && !"".equals(userInfoBean.getStoreName())) {
                text_type.setText(userInfoBean.getStoreName());
            } else {
                text_type.setText("个人");
            }

        }

    }


    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
