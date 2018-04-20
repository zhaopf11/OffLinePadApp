package com.kaihuang.bintutu.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.mine.adapter.AchievementsAdapter;
import com.kaihuang.bintutu.mine.bean.AchievementBean;
import com.kaihuang.bintutu.mine.msg.AchievementsRespMsg;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Response;

public class MonthAchievementsActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView text_title;
    @Bind(R.id.text_account)
    TextView text_account;
    @Bind(R.id.expand_achievement)
    ExpandableListView expand_achievement;
    //1：今日2：本月3：全部4：团队
    private int from;
    private AchievementsAdapter achievementsAdapter = null;

    private String year;
    private String month;
    private String day;
    private List<String> grouplist = new ArrayList<>();
    private Map<String, List<AchievementBean>> childMap = new HashMap<>();
    private String type = "0";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_month_achievements;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        Calendar calendar = Calendar.getInstance();
        from = getIntent().getIntExtra("from", 0);
        switch (from) {
            case 1:
                text_title.setText("今日业绩");
                year = calendar.get(Calendar.YEAR) + "";
                month = calendar.get(Calendar.MONTH) + 1 + "";
                day = calendar.get(Calendar.DAY_OF_MONTH) + "";
                type = "2";
                break;
            case 2:
                text_title.setText("本月业绩");
                year = calendar.get(Calendar.YEAR) + "";
                month = calendar.get(Calendar.MONTH) + 1 + "";
                type = "1";
                break;
            case 3:
                text_title.setText("全部业绩");
                break;
            case 4:
                text_title.setText("团队业绩");
                break;
        }
    }

    @Override
    protected void initEvents() {
        expand_achievement.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String userfoottypedataID = childMap.get(grouplist.get(i)).get(i1).getUserfoottypedataID();
                startActivity(new Intent(BaseApplication.getInstance(), AchievementDetailActivity.class).putExtra("userfoottypedataID", userfoottypedataID), false);
                return false;
            }
        });

    }

    @Override
    protected void initData() {
        achievementsAdapter = new AchievementsAdapter(BaseApplication.getInstance());
        expand_achievement.setAdapter(achievementsAdapter);
        int groupCount = achievementsAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expand_achievement.expandGroup(i);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("day", day);
        params.put("month", month);
        params.put("salesmanID", BaseApplication.getInstance().getUserID());
        params.put("token", BaseApplication.getInstance().getToken());
        params.put("type", type);
        getAchievementsList(params);

    }

    private void getAchievementsList(Map<String, Object> params) {

        mHttpHelper.post(Contants.API.getMonthOrDayOrAllResults, params, new SpotsCallBack<AchievementsRespMsg>(this) {
            @Override
            public void onSuccess(Response response, AchievementsRespMsg achievementsRespMsg) {
                if (null != achievementsRespMsg.getData() && achievementsRespMsg.getData().size() > 0) {
                    text_account.setText("共" + achievementsRespMsg.getConut() + "单");
                    if (1 == from) {
                        grouplist.add(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_YEAR_MONTH_DAY));
                        childMap.put(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_YEAR_MONTH_DAY), achievementsRespMsg.getData());
                    } else if (2 == from) {
                        grouplist.add(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_YEAR_MONTH));
                        childMap.put(TimeUtils.getCurrentTimeInString(TimeUtils.DATE_FORMAT_YEAR_MONTH), achievementsRespMsg.getData());
                    } else if (3 == from) {
                        for (AchievementBean achievementBean : achievementsRespMsg.getData()) {
                            String data = achievementBean.getTime().substring(0, 7);
                            data = data.replace("-", "年") + "月";
                            if (!childMap.containsKey(data)) {
                                List<AchievementBean> achievementBeen = new ArrayList<AchievementBean>();
                                childMap.put(data, achievementBeen);
                                grouplist.add(data);

                            }
                        }

                        for (AchievementBean achievementBean : achievementsRespMsg.getData()) {
                            String data = achievementBean.getTime().substring(0, 7);
                            data = data.replace("-", "年") + "月";
                            childMap.get(data).add(achievementBean);
                        }
                    }
                    if (null != grouplist && grouplist.size() > 0) {
                        achievementsAdapter.grouplist = grouplist;
                        achievementsAdapter.childMap = childMap;
                        achievementsAdapter.notifyDataSetChanged();
                        int groupCount = achievementsAdapter.getGroupCount();
                        for (int i = 0; i < groupCount; i++) {
                            expand_achievement.expandGroup(i);
                        }
                    }
                } else {
                    ToastUtils.show(MonthAchievementsActivity.this, achievementsRespMsg.getMessage());
                    text_account.setText("共0单");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(MonthAchievementsActivity.this, "请求失败，请稍后重试");
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(MonthAchievementsActivity.this, "请求失败，请稍后重试");

            }
        });
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        finish();
    }
}
