package com.kaihuang.bintutu.mine.msg;

import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.mine.bean.UserInfoBean;

/**
 * Created by zhoux on 2017/9/23.
 */

public class UserInfoRespMsg extends BaseRespMsg {

    private UserInfoBean data;
    private String province;
    private String salesmanName;
    private String phone;
    private String lastLoginTime;
    private String managerName;
    private String footdataCount;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getFootdataCount() {
        return footdataCount;
    }

    public void setFootdataCount(String footdataCount) {
        this.footdataCount = footdataCount;
    }

    public UserInfoBean getData() {
        return data;
    }

    public void setData(UserInfoBean data) {
        this.data = data;
    }
}
