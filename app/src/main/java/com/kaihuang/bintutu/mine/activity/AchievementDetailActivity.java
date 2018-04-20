package com.kaihuang.bintutu.mine.activity;

import android.view.View;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.mine.bean.AchievementDetailBean;
import com.kaihuang.bintutu.mine.msg.AchievementDetailRespMsg;
import com.kaihuang.bintutu.utils.Contants;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

/**
 * 业绩详情
 */

public class AchievementDetailActivity extends BaseActivity {

    @Bind(R.id.text_username)
    TextView text_username;
    @Bind(R.id.text_footnum)
    TextView text_footnum;
    @Bind(R.id.text_address)
    TextView text_address;
    @Bind(R.id.text_data)
    TextView text_data;
    @Bind(R.id.text_scantype)
    TextView text_scantype;
    @Bind(R.id.text_equipmentnum)
    TextView text_equipmentnum;
    @Bind(R.id.text_uploadstate)
    TextView text_uploadstate;
    @Bind(R.id.text_operator)
    TextView text_operator;
    @Bind(R.id.text_phonenum)
    TextView text_phonenum;


    private String userfoottypedataID;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_achievement_detail;
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
        userfoottypedataID = getIntent().getStringExtra("userfoottypedataID");
        Map<String, Object> params = new HashMap<>();
        params.put("token", BaseApplication.getInstance().getToken());
        params.put("userfoottypedataid",userfoottypedataID);
        getAchievementDetail(params);

    }

    private void getAchievementDetail(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.getResultsDetail, params, new SpotsCallBack<AchievementDetailRespMsg>(this) {
            @Override
            public void onSuccess(Response response, AchievementDetailRespMsg achievementDetailRespMsg) {
                if ("success".equals(achievementDetailRespMsg.getStatus())) {
                    if(null !=achievementDetailRespMsg.getData() && !"".equals(achievementDetailRespMsg.getData())){
                        AchievementDetailBean detailBean = achievementDetailRespMsg.getData();
                        text_username.setText(detailBean.getCustomerName());
                        text_footnum.setText(detailBean.getMeasurecode());
                        text_address.setText(detailBean.getAddress());
                        text_data.setText(detailBean.getTime());
                        text_scantype.setText(detailBean.getType());
                        text_operator.setText(detailBean.getSalesmanName());
                        text_phonenum.setText(detailBean.getSalesmanPhone());
                        text_equipmentnum.setText(detailBean.getEquipmentNum());
                    }
                }else{
                    ToastUtils.show(AchievementDetailActivity.this, achievementDetailRespMsg.getMessage());
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(AchievementDetailActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(AchievementDetailActivity.this, "请求失败，请稍后重试");
            }
        });

    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
