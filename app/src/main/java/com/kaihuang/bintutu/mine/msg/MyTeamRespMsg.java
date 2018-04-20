package com.kaihuang.bintutu.mine.msg;

import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.mine.bean.TeamBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/23.
 */

public class MyTeamRespMsg extends BaseRespMsg {


    private List<TeamBean> data;

    public List<TeamBean> getData() {
        return data;
    }

    public void setData(List<TeamBean> data) {
        this.data = data;
    }
}
