package com.kaihuang.bintutu.common.http;

import android.content.Context;
import dmax.dialog.SpotsDialog;
import jameson.io.library.util.ToastUtils;
import okhttp3.Request;
import okhttp3.Response;

public abstract class SpotsCallBack<T> extends SimpleCallback<T> {
    private static SpotsDialog mDialog;
    private boolean isShowDialog = true;
    private Context context;
    private String[] msg ;
    public SpotsCallBack(Context context,String...msg){
        super(context);
        this.context = context;
        this.msg = msg;
        initSpotsDialog();
    }

    private  void initSpotsDialog(){
        if(msg != null && msg.length > 0){
//            mDialog = new SpotsDialog(mContext,msg[0]);
            isShowDialog = false;
        }else{
            mDialog = new SpotsDialog(mContext,"拼命加载中...");
            isShowDialog = true;
        }
    }

    public  void showDialog(){
        if(mDialog != null) {
            mDialog.show();
        }
    }

    public  void dismissDialog(){
        if(mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }

    @Override
    public void onBeforeRequest(Request request) {
        if(isShowDialog){
            showDialog();
        }
    }

    @Override
    public void onResponse(Response response) {
        if(isShowDialog){
            dismissDialog();
        }
    }

    @Override
    public void onSuccess(Response response, T t) {
        if(isShowDialog){
            dismissDialog();
        }
    }

    @Override
    public void onFailure(Request request, Exception e) {
        super.onFailure(request, e);
        if(isShowDialog){
            dismissDialog();
        }
        ToastUtils.show(context,"服务器忙，请稍后重试");
    }
}
