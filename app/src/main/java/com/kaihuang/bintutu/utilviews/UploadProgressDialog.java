package com.kaihuang.bintutu.utilviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.kaihuang.bintutu.R;

/**
 * Created by zhoux on 2017/10/16.
 */

public class UploadProgressDialog {

    public Dialog dialog;
    private ImageView img_bg;
    private ImageView img_show;
    public ClipDrawable drawable ;
    private ImageView img_show1;
    public ClipDrawable drawable1 ;
    private int progress;
    public void uploadProgress(Context context) {
        dialog = new Dialog(context, R.style.CustomDialog);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.dialog_progress_upload, null);
        img_bg = (ImageView)dv.findViewById(R.id.img_bg);
        img_show = (ImageView) dv.findViewById(R.id.img_show);
        img_show1 = (ImageView)dv.findViewById(R.id.img_show1);
        drawable = (ClipDrawable) img_show.getBackground();
        drawable1 = (ClipDrawable)img_show1.getBackground();
        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress){
        this.progress = progress;
        if(5000==progress){
            handler.sendEmptyMessage(5000);
        }else if(10000 ==progress){
            handler.sendEmptyMessage(10000);
        } else if(progress>10000){
            dialog.dismiss();
        }
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 5000:
                    img_bg.setImageResource(R.drawable.progress50);
                    drawable.setLevel(10000);
                    break;
                case 10000:
                    img_bg.setImageResource(R.drawable.progress100);
                    drawable.setLevel(0);
                    drawable1.setLevel(10000);
                    break;
            }
        }
    };
}
