package com.kaihuang.bintutu.mine.bean;

import com.kaihuang.bintutu.common.model.BaseBean;

/**
 * Created by zhoux on 2017/9/23.
 */

public class AchievementBean extends BaseBean {
        private String Measurecode;
        private String userfoottypedataID;
        private String name;
        private String time;

    public String getMeasurecode() {
        return Measurecode;
    }

    public void setMeasurecode(String measurecode) {
        Measurecode = measurecode;
    }

    public String getUserfoottypedataID() {
        return userfoottypedataID;
    }

    public void setUserfoottypedataID(String userfoottypedataID) {
        this.userfoottypedataID = userfoottypedataID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
