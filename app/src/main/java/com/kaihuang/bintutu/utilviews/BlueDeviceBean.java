package com.kaihuang.bintutu.utilviews;

/**
 * Created by zhaopf on 2018/3/31.
 */

public class BlueDeviceBean {
    private String name;
    private String address;
    private int bondState;

    public int getBondState() {
        return bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
