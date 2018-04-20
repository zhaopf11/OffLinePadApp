package com.kaihuang.bintutu.utilviews;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kaihuang.bintutu.R;

/**
 * Created by zhoux on 2017/10/10.
 */

public class DialogChoose{

    private OnClick onClick;
    public Dialog dialog;


    public void setOnClick(OnClick onClick){
        this.onClick  = onClick;
    }

    public interface OnClick{
        void textClick1();
        void textClick2();
        void butonClick();
    }
    public void infoDialog(Context context, String textinfo1,String textinfo2, boolean isShowButton) {
        dialog = new Dialog(context, R.style.CustomDialog);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dv = inflater.inflate(R.layout.dialog_choosetype, null);
        TextView text_equipment = (TextView) dv.findViewById(R.id.text_equipment);
        TextView text_inputdata =(TextView)dv.findViewById(R.id.text_inputdata);
        Button btn_submit=(Button)dv.findViewById(R.id.btn_submit);

        text_equipment.setText(textinfo1);
        text_inputdata.setText(textinfo2);
        if(!isShowButton){
            btn_submit.setVisibility(View.GONE);
        }
        text_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.textClick1();
            }
        });
        text_inputdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.textClick2();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.butonClick();
            }
        });

        dialog.setContentView(dv);
        dialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失
        dialog.show();
    }
}
