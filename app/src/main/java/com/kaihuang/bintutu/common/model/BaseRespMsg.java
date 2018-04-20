
package com.kaihuang.bintutu.common.model;

import java.io.Serializable;


public class BaseRespMsg implements Serializable {
    public final static int TO_LOGIN = -2;


    protected String message;
    protected String status;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
