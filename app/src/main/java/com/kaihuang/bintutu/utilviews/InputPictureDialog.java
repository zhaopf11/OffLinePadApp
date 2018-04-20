package com.kaihuang.bintutu.utilviews;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.utils.RegexUtil;

/**
 * Created by zhaopf on 2018/2/28.
 */
public class InputPictureDialog extends Dialog {
    @Bind(R.id.image_cancle)
    ImageView image_cancle;
    @Bind(R.id.image_save)
    ImageView image_save;
    @Bind(R.id.input_edit)
    EditText input_edit;

    private View view;
    private Context context;
    private OnButtonClick onClick;
    public InputPictureDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_input_picture_tag, null);
        ButterKnife.bind(this, view);
        setView();
        //设置可获得焦点
        input_edit.setFocusable(true);
        input_edit.setFocusableInTouchMode(true);
        //请求获得焦点
        input_edit.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        RegexUtil.showInputMethod(context,input_edit);
    }

    @OnClick(R.id.image_save)
    public void sure(){
        String edit = input_edit.getText().toString().trim();
        RegexUtil.downSoftInput(context,input_edit);
        onClick.sureClick(edit);
    }

    @OnClick(R.id.image_cancle)
    public void cancle(){
        RegexUtil.downSoftInput(context,input_edit);
        onClick.cancleClick();
    }


    public void setOnButtonClick(OnButtonClick onClick){
        this.onClick  = onClick;
    }

    public interface OnButtonClick{
        void cancleClick();
        void sureClick(String desc);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onClick.cancleClick();
    }

    private void setView() {
        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.FILL);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
    }

}
