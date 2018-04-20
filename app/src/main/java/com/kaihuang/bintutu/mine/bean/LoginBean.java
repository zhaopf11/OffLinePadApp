package com.kaihuang.bintutu.mine.bean;

import com.kaihuang.bintutu.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/23.
 */

public class LoginBean extends BaseBean {
    private String salesmanID;
    private String token;

    public String getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(String salesmanID) {
        this.salesmanID = salesmanID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
