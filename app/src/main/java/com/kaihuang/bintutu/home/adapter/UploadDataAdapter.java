package com.kaihuang.bintutu.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaihuang.bintutu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoux on 2017/9/19.
 */

public class UploadDataAdapter extends RecyclerView.Adapter<UploadDataAdapter.ViewHoloer> {
    private LayoutInflater mInflater;
    private Context context;
    private int count =5;

    private String[] foots = new String[]{"脚长", "跖围", "跗围", "兜围", "脚腕围", "脚指周长", "外踝骨中心下缘点高度", "后跟凸点高度", "舟上弯点高度"
            , "跗围高度", "第一跖趾关节高度", "大拇趾趾尖高度", "脚腕高度", "脚指宽度", "跖围宽", "底板净宽","拇趾里宽","小趾外宽","第一跖趾里宽",
            "第五跖趾外宽","腰窝外宽","踵心底板宽度","脚踝骨内外宽度","拇趾外凸点部位长度","小趾端点部位长度","小趾外凸点部位长度","第一跖趾部位长度",
            "第五跖趾部位长度","跗骨部位长度","腰窝部位长度","舟上弯点部位长度","外踝骨中心部位长度","踵心部位长度","后跟边距长度","前掌凸点部位长度"};
    public List<String> footInfo = new ArrayList<>();
    public List<String> rightfootInfo = new ArrayList<>();
    public boolean isLeftFoot = true;
    public UploadDataAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHoloer extends RecyclerView.ViewHolder {
        TextView text_num;
        TextView text_name;
        TextView text_leftfoot;
        TextView text_rightfoot;
        RelativeLayout rel_item;
        public ViewHoloer(View view) {
            super(view);
            text_num = (TextView)view.findViewById(R.id.text_num);
            text_name=(TextView)view.findViewById(R.id.text_name);
            text_leftfoot = (TextView)view.findViewById(R.id.text_leftfoot);
            text_rightfoot=(TextView)view.findViewById(R.id.text_rightfoot);
            rel_item = (RelativeLayout)view.findViewById(R.id.rel_item);
        }
    }

    @Override
    public ViewHoloer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_footmessage_detail,
                parent, false);
        ViewHoloer viewHolder = new ViewHoloer(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoloer holder, int position) {
        if(footInfo != null && footInfo.size() > position) {
            holder.text_leftfoot.setText(footInfo.get(position));
        }
        if(rightfootInfo != null && rightfootInfo.size() > position){
            holder.text_rightfoot.setText(rightfootInfo.get(position));
        }

        if(foots != null && foots.length > 0){
            holder.text_name.setText(foots[position]);
            holder.text_num.setText((position+1)+"");
            if(position % 2 == 1){
                holder.rel_item.setBackgroundColor(context.getResources().getColor(R.color.gray_bg));
            }else{
                holder.rel_item.setBackgroundColor(context.getResources().getColor(R.color.navigation_bar_bg));
            }
        }
    }

    @Override
    public int getItemCount() {
        return null ==foots?0:foots.length;
    }

    public void setCount(int count){
        this.count = count;
    }
}

