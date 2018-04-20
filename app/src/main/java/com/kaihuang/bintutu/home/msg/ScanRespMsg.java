package com.kaihuang.bintutu.home.msg;

import java.io.Serializable;

/**
 * Created by zhoux on 2017/10/16.
 */

public class ScanRespMsg implements Serializable {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
