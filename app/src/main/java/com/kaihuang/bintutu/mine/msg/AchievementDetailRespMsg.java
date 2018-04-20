package com.kaihuang.bintutu.mine.msg;

import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.mine.bean.AchievementDetailBean;

/**
 * Created by zhoux on 2017/9/23.
 */

public class AchievementDetailRespMsg extends BaseRespMsg {

    private AchievementDetailBean data;

    public AchievementDetailBean getData() {
        return data;
    }

    public void setData(AchievementDetailBean data) {
        this.data = data;
    }
}
