package com.kaihuang.bintutu.home.msg;

import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.mine.bean.LoginBean;

/**
 * Created by zhoux on 2017/9/21.
 */

public class LoginRespMsg extends BaseRespMsg {

 private LoginBean data;

    public LoginBean getData() {
        return data;
    }

    public void setData(LoginBean data) {
        this.data = data;
    }
}
