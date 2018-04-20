package com.kaihuang.bintutu.home.activity;

import android.view.View;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EquipmentDetailActivity extends BaseActivity {


    private String ssid;
    @Bind(R.id.text_equipmentnum)
    TextView text_equipmentnum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_equipment_detail;
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
        ssid = getIntent().getStringExtra("ssid");
        text_equipmentnum.setText(ssid);

    }

    @OnClick(R.id.lin_bottom)
    public void submit() {
        finish();
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
