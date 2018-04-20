package com.kaihuang.bintutu.mine;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.BaseFragment;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.mine.activity.EquipmentRepairActivity;
import com.kaihuang.bintutu.mine.activity.LoginActivity;
import com.kaihuang.bintutu.mine.activity.MonthAchievementsActivity;
import com.kaihuang.bintutu.mine.activity.MyTeamActivity;
import com.kaihuang.bintutu.mine.activity.PersonalInfoActivity;
import com.kaihuang.bintutu.mine.bean.UserInfoBean;
import com.kaihuang.bintutu.mine.msg.UserInfoRespMsg;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.TimeUtils;
import com.kaihuang.bintutu.utilviews.DialogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.CoreData;
import jameson.io.library.util.ToastUtils;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {


    @Bind(R.id.text_username)
    TextView text_username;
    @Bind(R.id.text_date)
    TextView text_date;
    @Bind(R.id.text_city)
    TextView text_city;
    @Bind(R.id.text_type)
    TextView text_type;
    @Bind(R.id.text_today)
    TextView text_today;
    private UserInfoBean userInfoBean = null;
    private static final int REQUEST_CODE = 1;

    DialogUtil dialogUtil = new DialogUtil();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
//        dialogUtil.showLoadingDialog(getActivity(), "正在加载...");
        Map<String, Object> params = new HashMap<>();
//        params.put("token", BaseApplication.getInstance().getToken());
        params.put("salesmanId", BaseApplication.getInstance().getUserID());
        getInfo(params);
    }

    private void getInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.API.myDetail, params, new SpotsCallBack<UserInfoRespMsg>(getActivity()) {
            @Override
            public void onSuccess(Response response, UserInfoRespMsg userInfoRespMsg) {
                if (!TextUtils.isEmpty(userInfoRespMsg.getLastLoginTime())) {
                    text_date.setText(TimeUtils.getTime(Long.parseLong(userInfoRespMsg.getLastLoginTime()), TimeUtils.DATE_FORMAT_YEAR_MONTH_DAY_SECOND));
                }
                text_username.setText(userInfoRespMsg.getSalesmanName()+ "");
                text_city.setText(userInfoRespMsg.getProvince());
                text_type.setText(userInfoRespMsg.getManagerName());
//                dialogUtil.dismissLoading();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
//                dialogUtil.dismissLoading();
               dismissDialog();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
//                dialogUtil.dismissLoading();
                dismissDialog();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                ToastUtils.show(BaseApplication.getInstance(), "请求失败，请稍后重试");
//                dialogUtil.dismissLoading();
                dismissDialog();
            }
        });


    }

    @OnClick(R.id.text_authentication)
    public void toPersonalInfo() {
        startActivity(new Intent(BaseApplication.getInstance(), PersonalInfoActivity.class).putExtra("userInfo", userInfoBean), false);
    }

    @OnClick(R.id.text_report)
    public void toReportRepair() {
        startActivity(new Intent(BaseApplication.getInstance(), EquipmentRepairActivity.class), false);

    }

    @OnClick(R.id.text_today)
    public void toAchievements() {
        startActivity(new Intent(BaseApplication.getInstance(), MonthAchievementsActivity.class).putExtra("from", 1), false);

    }

    @OnClick(R.id.text_month)
    public void toMonthsAchievements() {
        startActivity(new Intent(BaseApplication.getInstance(), MonthAchievementsActivity.class).putExtra("from", 2), false);
    }

    @OnClick(R.id.text_all)
    public void toAllAchievements() {
        startActivity(new Intent(BaseApplication.getInstance(), MonthAchievementsActivity.class).putExtra("from", 3), false);
    }

    @OnClick(R.id.rel_myteam)
    public void toMyTeam() {
        startActivity(new Intent(BaseApplication.getInstance(), MyTeamActivity.class), false);
    }

    @OnClick(R.id.img_custom)
    public void toChatCustom() {
        if (null != BaseApplication.getInstance().getToken() && !"".equals(BaseApplication.getInstance().getToken()) && null != BaseApplication.getInstance().getUserID() && !"".equals(BaseApplication.getInstance().getUserID())) {
            String[] permissions = {
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
            Ntalker.getExtendInstance().ntalkerSystem().requestPermissions(getActivity(), permissions);
            ChatParamsBody chatparams = new ChatParamsBody();
            // 咨询发起页（专有参数）
            // erp参数e
            chatparams.erpParam = "";
            // 此参数不传就默认在sdk外部打开，即在onClickUrlorEmailorNumber方法中打开
            chatparams.clickurltoshow_type = CoreData.CLICK_TO_SDK_EXPLORER;
            // 商品展示（专有参数）
//            chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
////            chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
//            chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_SDK_EXPLORER;
//            chatparams.itemparams.itemparam = "";
            //使用id方式，（SHOW_GOODS_BY_ID）
            int startChat = Ntalker.getBaseInstance().startChat(BaseApplication.getInstance(), Contants.siteid, null, chatparams);
            if (0 == startChat) {
                Log.e("startChat", "打开聊窗成功");
            } else {
                Log.e("startChat", "打开聊窗失败，错误码:" + startChat);
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), false);
        }
    }

    public void refData() {
        if (null == BaseApplication.getInstance().getToken() || "".equals(BaseApplication.getInstance().getToken())) {
            startActivityForResult(new Intent(BaseApplication.getInstance(), LoginActivity.class), REQUEST_CODE);
        } else {
            init();
        }
    }

    @OnClick(R.id.btn_loginout)
    public void logout() {
        BaseApplication.getInstance().clearToken();
        Contants.bindingUserID = "";
        Contants.relatePhone = "";
        Contants.contactSSID = "";
        Contants.ssid = "";
        Contants.bindPhone = "";
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
