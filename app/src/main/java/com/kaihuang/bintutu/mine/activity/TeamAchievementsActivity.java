package com.kaihuang.bintutu.mine.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.mine.adapter.TeamAchievementsAdapter;
import com.kaihuang.bintutu.mine.bean.AchievementBean;
import com.kaihuang.bintutu.mine.msg.AchievementsRespMsg;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utilviews.FullOffDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

public class TeamAchievementsActivity extends BaseActivity {


    @Bind(R.id.text_account)
    TextView text_account;
    @Bind(R.id.recycler_achievement)
    RecyclerView recycler_achievement;
    private String salesmanID;
    private TeamAchievementsAdapter teamAchievementsAdapter;
    List<AchievementBean> achievementBeanList;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_achievements;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        teamAchievementsAdapter  = new TeamAchievementsAdapter(TeamAchievementsActivity.this);
        teamAchievementsAdapter.setOnItemClickLitener(new TeamAchievementsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                String userfoottypedataID = achievementBeanList.get(position).getUserfoottypedataID();
                startActivity(new Intent(BaseApplication.getInstance(),AchievementDetailActivity.class).putExtra("userfoottypedataID",userfoottypedataID),false);

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_achievement.setLayoutManager(linearLayoutManager);
        recycler_achievement.addItemDecoration(new FullOffDecoration(2));
        recycler_achievement.setAdapter(teamAchievementsAdapter);

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initData() {
        salesmanID = getIntent().getStringExtra("salesmanID");
        Map<String, Object> params = new HashMap<>();
        params.put("year", "");
        params.put("day","");
        params.put("month","");
        params.put("salesmanID",salesmanID);
        params.put("token",BaseApplication.getInstance().getToken());
        params.put("type","0");
        getAchievementsList(params);

    }


    private void getAchievementsList(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.getMonthOrDayOrAllResults, params, new SpotsCallBack<AchievementsRespMsg>(this) {
            @Override
            public void onSuccess(Response response, AchievementsRespMsg achievementsRespMsg) {
                if(null !=achievementsRespMsg.getData() && achievementsRespMsg.getData().size()>0){
                    text_account.setText("共"+achievementsRespMsg.getData().size()+"单");
                    teamAchievementsAdapter.achievementBeen = achievementsRespMsg.getData();
                    teamAchievementsAdapter.notifyDataSetChanged();
                    achievementBeanList = achievementsRespMsg.getData();
                }else {
                    text_account.setText("共0单");
                }
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(TeamAchievementsActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(TeamAchievementsActivity.this, "请求失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
