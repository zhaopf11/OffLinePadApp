package com.kaihuang.bintutu.home.msg;

import com.kaihuang.bintutu.common.model.BaseRespMsg;

/**
 * Created by zhoux on 2017/10/10.
 */

public class ChooseUserRespMsg extends BaseRespMsg {

    private String bindingUserID;
    private String userfoottypedatarelateid;

    public String getUserfoottypedatarelateid() {
        return userfoottypedatarelateid;
    }

    public void setUserfoottypedatarelateid(String userfoottypedatarelateid) {
        this.userfoottypedatarelateid = userfoottypedatarelateid;
    }

    public String getBindingUserID() {
        return bindingUserID;
    }

    public void setBindingUserID(String bindingUserID) {
        this.bindingUserID = bindingUserID;
    }
}
