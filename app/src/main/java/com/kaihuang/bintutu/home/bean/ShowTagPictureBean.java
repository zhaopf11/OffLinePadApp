package com.kaihuang.bintutu.home.bean;

import java.util.ArrayList;

/**
 * Created by zhaopf on 2018/3/1.
 */

public class ShowTagPictureBean {
    private String isFrom;//1.为本地存的  空为足型样例
    private String fileName;
    private int position;
    private String leftOrRight;
    private ArrayList<String> descList;

    public String getLeftOrRight() {
        return leftOrRight;
    }

    public void setLeftOrRight(String leftOrRight) {
        this.leftOrRight = leftOrRight;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<String> getDescList() {
        return descList;
    }

    public void setDescList(ArrayList<String> descList) {
        this.descList = descList;
    }

    public String getIsFrom() {
        return isFrom;
    }

    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
