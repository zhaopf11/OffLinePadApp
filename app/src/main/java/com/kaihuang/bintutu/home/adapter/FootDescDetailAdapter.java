package com.kaihuang.bintutu.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.home.bean.TagPictureDesc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaopf on 2018/2/27.
 */

public class FootDescDetailAdapter extends RecyclerView.Adapter<FootDescDetailAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public List<String> descList = new ArrayList<>();
    public List<TagPictureDesc> list = new ArrayList<>();
    public Context context;
    private String tag;
    public String leftRightFoot;
    public FootDescDetailAdapter(Context context, String tag) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.tag = tag;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_num;
        TextView text_foottype;
        TextView text_desc_detail;

        public ViewHolder(View view) {
            super(view);
            text_num = (TextView) view.findViewById(R.id.text_num);
            text_foottype = (TextView) view.findViewById(R.id.text_foottype);
            text_desc_detail = (TextView) view.findViewById(R.id.text_desc_detail);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(!TextUtils.isEmpty(tag) && "pictureTag".equals(tag)){
            view = mInflater.inflate(R.layout.item_footdesc_picturetag, parent, false);
        }else{
            view = mInflater.inflate(R.layout.item_foot_desc, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text_num.setText((position + 1) + "");
        if(!TextUtils.isEmpty(tag)){
            holder.text_desc_detail.setText(descList.get(position));
        }else{
            if(list != null && list.size() > 0){
                String leftOrRight = "左脚";
                TagPictureDesc tagPictureDesc = list.get(position);
                switch (Integer.parseInt(tagPictureDesc.getIndex())){
                    case 1:
                        leftOrRight = "[左脚脚面]:";
                        break;
                    case 2:
                        leftOrRight = "[左脚脚底]:";
                        break;
                    case 3:
                        leftOrRight = "[左脚内侧]:";
                        break;
                    case 4:
                        leftOrRight = "[左脚外侧]:";
                        break;
                    case 5:
                        leftOrRight = "[右脚脚面]:";
                        break;
                    case 6:
                        leftOrRight = "[右脚脚底]:";
                        break;
                    case 7:
                        leftOrRight = "[右脚内侧]:";
                        break;
                    case 8:
                        leftOrRight = "[右脚外侧]:";
                        break;
                }
                holder.text_foottype.setText(leftOrRight);
                holder.text_desc_detail.setText(tagPictureDesc.getDesc());
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(!TextUtils.isEmpty(tag)){
            if(descList != null && descList.size() > 0){
                count = descList.size();
            }else{
                count= 0;
            }
        }else{
            if(list != null && list.size() > 0){
                count = list.size();
            }else{
                count= 0;
            }
        }
        return count;
    }
}

