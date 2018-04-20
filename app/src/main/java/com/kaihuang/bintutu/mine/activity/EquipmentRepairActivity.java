package com.kaihuang.bintutu.mine.activity;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.utils.Contants;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

public class EquipmentRepairActivity extends BaseActivity {

    @Bind(R.id.radiogroup_equipment)
    RadioGroup radiogroup_equipment;
    @Bind(R.id.radiogroup_question)
    RadioGroup radiogroup_question;
    @Bind(R.id.edit_equipmentnum)
    EditText edit_equipmentnum;
    @Bind(R.id.edit_problem)
    EditText edit_problem;

    private int repairType=1;
    private int repairProblem =1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_equipment_repair;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initEvents() {

        radiogroup_equipment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    //设备类型  1 3d设备  2 手持设备
                    case R.id.radiobtn_scanequipment:
                        repairType =1;
                        break;
                    case R.id.radiobtn_holdequipment:
                        repairType=2;
                        break;
                }
            }
        });

        radiogroup_question.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    //1 是异常  2是损坏
                    case R.id.radiobtn_unusual:
                        repairProblem =1;
                        break;
                    case R.id.radiobtn_damage:
                        repairProblem =2;
                        break;
                }
            }
        });

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_next)
    public void repair(){

        String equipnun = edit_equipmentnum.getText().toString().trim();
        String equipcontent = edit_problem.getText().toString().trim();
        if(TextUtils.isEmpty(equipnun)){
            ToastUtils.show(EquipmentRepairActivity.this,"请输入设备号");
        }else {
            Map<String, Object> params = new HashMap<>();
            params.put("token", BaseApplication.getInstance().getToken());
            params.put("deviceId",equipnun);//设备号
            params.put("deviceType",repairType+"");//设备类型
            params.put("repairStatus",repairProblem+"");//报修问题
            params.put("repairPerson",BaseApplication.getInstance().getUserID());
            params.put("repairProblem",equipcontent);//详细描述
            equipRepair(params);
        }
    }

    private void equipRepair(Map<String, Object> params) {
        mHttpHelper.post(Contants.API.addRepair, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if ("success".equals(baseRespMsg.getStatus())) {
                    ToastUtils.show(EquipmentRepairActivity.this, "报修成功");
                    finish();
                }else if ("fail".equals(baseRespMsg.getStatus())) {
                    ToastUtils.show(EquipmentRepairActivity.this,baseRespMsg.getMessage());
                } else {
                    ToastUtils.show(EquipmentRepairActivity.this, baseRespMsg.getMessage());
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(EquipmentRepairActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(EquipmentRepairActivity.this, "请求失败，请稍后重试");

            }
        });


    }
    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
