package com.kaihuang.bintutu.mine.bean;

import com.kaihuang.bintutu.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/23.
 */

public class TeamBean extends BaseBean {

    private String salesmanID;
    private String salesmanName;

    public String getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(String salesmanID) {
        this.salesmanID = salesmanID;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }
}
