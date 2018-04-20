package com.kaihuang.bintutu.mine.bean;

import com.kaihuang.bintutu.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/23.
 */

public class AchievementDetailBean extends BaseBean {

    private String salesmanName;
    private String address;
    private String measurecode;
    private String salesmanPhone;
    private String time;
    private String equipmentNum;
    private String type;
    private String customerName;

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setMeasurecode(String measurecode) {
        this.measurecode = measurecode;
    }

    public String getMeasurecode() {
        return measurecode;
    }

    public void setSalesmanPhone(String salesmanPhone) {
        this.salesmanPhone = salesmanPhone;
    }

    public String getSalesmanPhone() {
        return salesmanPhone;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getEquipmentNum() {
        return equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        this.equipmentNum = equipmentNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

}
