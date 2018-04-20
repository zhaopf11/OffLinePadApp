package com.kaihuang.bintutu.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaihuang.bintutu.R;

/**
 * Created by zhoux on 2017/9/19.
 */

public class UploadFootAdapter extends RecyclerView.Adapter<UploadFootAdapter.ViewHoloer> {
    private LayoutInflater mInflater;
    private Context context;
    private GetTextString getTextString;

    private String[] foots = new String[]{"脚长", "跖围", "跗围", "兜围", "脚腕围", "脚指周长", "外踝骨中心下缘点高度", "后跟凸点高度", "舟上弯点高度"
            , "跗围高度", "第一跖趾关节高度", "大拇趾趾尖高度", "脚腕高度", "脚指宽度", "跖围宽", "底板净宽","拇趾里宽","小趾外宽","第一跖趾里宽",
            "第五跖趾外宽","腰窝外宽","踵心底板宽度","脚踝骨内外宽度","拇趾外凸点部位长度","小趾端点部位长度","小趾外凸点部位长度","第一跖趾部位长度",
            "第五跖趾部位长度","跗骨部位长度","腰窝部位长度","舟上弯点部位长度","外踝骨中心部位长度","踵心部位长度","后跟边距长度","前掌凸点部位长度"};
    public String[] leftfoots;
    public String[] rightfoots;
    public boolean isLeftFoot = true;

    public UploadFootAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHoloer extends RecyclerView.ViewHolder {

        TextView text_footproperty;
        TextView text_num;
        EditText edit_leftfoot;
        EditText edit_rightfoot;
        RelativeLayout rel_item;
        public ViewHoloer(View view) {
            super(view);
            rel_item = (RelativeLayout) view.findViewById(R.id.rel_item);
            text_num = (TextView) view.findViewById(R.id.text_num);
            text_footproperty = (TextView) view.findViewById(R.id.text_footproperty);
            edit_leftfoot = (EditText) view.findViewById(R.id.edit_leftfoot);
            edit_rightfoot = (EditText) view.findViewById(R.id.edit_rightfoot);

        }
    }

    @Override
    public ViewHoloer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_upload_foot, parent, false);
        ViewHoloer viewHolder = new ViewHoloer(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoloer holder, int position) {
        if (holder instanceof ViewHoloer) {
            //1、为了避免TextWatcher在第2步被调用，提前将他移除。
            if ( holder.edit_leftfoot.getTag() instanceof TextWatcher) {
                holder.edit_leftfoot.removeTextChangedListener((TextWatcher) ( holder.edit_leftfoot.getTag()));
                holder.edit_rightfoot.removeTextChangedListener((TextWatcher) ( holder.edit_rightfoot.getTag()));
            }
            // 第2步：移除TextWatcher之后，设置EditText的Text。
            holder.edit_leftfoot.setText(leftfoots[position]);
            holder.edit_rightfoot.setText(rightfoots[position]);

            holder.text_num.setText((position+1)+"");
            if(position % 2 == 1){
                holder.rel_item.setBackgroundColor(context.getResources().getColor(R.color.gray_bg));
            }else{
                holder.rel_item.setBackgroundColor(context.getResources().getColor(R.color.navigation_bar_bg));
            }

            TextWatcher watcherLeft = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String valueLeft = holder.edit_leftfoot.getText().toString().trim();
                    String valueRight = holder.edit_rightfoot.getText().toString().trim();
                    if(null == valueLeft ){
                        valueLeft = "";
                    }

                    if(null == valueRight ){
                        valueRight = "";
                    }
                    getTextString.keepTextString(position,valueLeft,true);
                }
            };
            holder.edit_leftfoot.addTextChangedListener(watcherLeft);
            holder.edit_leftfoot.setTag(watcherLeft);

            TextWatcher watcherRight = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String valueRight = holder.edit_rightfoot.getText().toString().trim();
                    if(null == valueRight ){
                        valueRight = "";
                    }
                    getTextString.keepTextString(position,valueRight,false);
                }
            };
            holder.edit_rightfoot.addTextChangedListener(watcherRight);
            holder.edit_rightfoot.setTag(watcherRight);
        }


        holder.edit_leftfoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                holder.edit_leftfoot.setFocusable(true);
                holder.edit_leftfoot.setFocusableInTouchMode(true);
                holder.edit_leftfoot.requestFocus();
                return false;
            }
        });
        holder.edit_rightfoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                holder.edit_rightfoot.setFocusable(true);
                holder.edit_rightfoot.setFocusableInTouchMode(true);
                holder.edit_rightfoot.requestFocus();
                return false;
            }
        });
        holder.text_footproperty.setText(foots[position]);
    }

    @Override
    public int getItemCount() {
        return foots.length;
    }

    public interface GetTextString {
        void keepTextString(int position, String info,boolean isLeft);
    }

    public void setGetTextString(GetTextString getTextString) {
        this.getTextString = getTextString;
    }

}

