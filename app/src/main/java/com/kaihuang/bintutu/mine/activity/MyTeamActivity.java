package com.kaihuang.bintutu.mine.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.mine.adapter.TeamAdapter;
import com.kaihuang.bintutu.mine.bean.TeamBean;
import com.kaihuang.bintutu.mine.msg.MyTeamRespMsg;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utilviews.HotItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

public class MyTeamActivity extends BaseActivity {

    @Bind(R.id.recycler_team)
    RecyclerView recycler_team;
    private TeamAdapter teamAdapter= null;
    private List<TeamBean> teamBeen = null;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_team;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        teamAdapter = new TeamAdapter(BaseApplication.getInstance());
        recycler_team.setLayoutManager(linearLayoutManager);
        recycler_team.addItemDecoration(new HotItemDecoration(2));
        recycler_team.setAdapter(teamAdapter);
    }

    @Override
    protected void initEvents() {
        teamAdapter.setOnItemClickLitener(new TeamAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(BaseApplication.getInstance(), TeamAchievementsActivity.class).putExtra("salesmanID",teamBeen.get(position).getSalesmanID()), false);
            }
        });
    }

    @Override
    protected void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", BaseApplication.getInstance().getToken());
        params.put("salesmanID",BaseApplication.getInstance().getUserID());
        getMyTeam(params);
    }

    private void getMyTeam(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.myTeamSalesmanList, params, new SpotsCallBack<MyTeamRespMsg>(this) {
            @Override
            public void onSuccess(Response response, MyTeamRespMsg myTeamRespMsg) {
                if ("success".equals(myTeamRespMsg.getStatus())) {
                    if(null !=myTeamRespMsg.getData() && myTeamRespMsg.getData().size()>0){
                        teamBeen = myTeamRespMsg.getData();
                        teamAdapter.teamBeen = myTeamRespMsg.getData();
                        teamAdapter.notifyDataSetChanged();
                    }
                }else{
                    ToastUtils.show(MyTeamActivity.this, myTeamRespMsg.getMessage());
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MyTeamActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MyTeamActivity.this, "请求失败，请稍后重试");
            }
        });
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }

}
