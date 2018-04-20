package com.kaihuang.bintutu.mine.bean;

import com.kaihuang.bintutu.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/23.
 */

public class UserInfoBean extends BaseBean {

    private String loginTIme;
    private String IDCard;
    private String phone;
    private String count;
    private String name;
    private String storeName;
    private String provinceName;


    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getLoginTIme() {
        return loginTIme;
    }

    public void setLoginTIme(String loginTIme) {
        this.loginTIme = loginTIme;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
