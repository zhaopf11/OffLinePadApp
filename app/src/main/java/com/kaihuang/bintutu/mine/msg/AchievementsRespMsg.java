package com.kaihuang.bintutu.mine.msg;

import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.mine.bean.AchievementBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/23.
 */

public class AchievementsRespMsg extends BaseRespMsg {

    private List<AchievementBean> data;
    private String conut;

    public List<AchievementBean> getData() {
        return data;
    }

    public void setData(List<AchievementBean> data) {
        this.data = data;
    }

    public String getConut() {
        return conut;
    }

    public void setConut(String conut) {
        this.conut = conut;
    }
}
