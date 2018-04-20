package com.kaihuang.bintutu.utilviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.kaihuang.bintutu.R;

/**
 * Created by zhoux on 2017/10/13.
 */

public class CircularProgressbarDialog {

    public Dialog dialog;
    private ImageView img_show;
    public ClipDrawable drawable ;
    public void circularProgress(Context context) {
        dialog = new Dialog(context, R.style.CustomDialog);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.dialog_circular_progressbar, null);

        img_show = (ImageView) dv.findViewById(R.id.img_show);
        drawable = (ClipDrawable) img_show.getBackground();
        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();
    }

    public void setProgress(int progress){
        if(progress>10000){
            dialog.dismiss();
        }else {
            drawable.setLevel(progress);

        }
    }
}
