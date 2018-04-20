package com.kaihuang.bintutu.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseApplication;

import java.util.List;

/**
 * Created by zhoux on 2017/9/18.
 */

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.ViewHoloer> {
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClick onItemClick;
    public List<String> footnums;

    public DataListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    class ViewHoloer extends RecyclerView.ViewHolder {

        TextView text_footnum;
        TextView text_username;
        public ViewHoloer(View view) {
            super(view);
            text_footnum = (TextView)view.findViewById(R.id.text_footnum);
            text_username=(TextView)view.findViewById(R.id.text_username);

        }
    }

    @Override
    public ViewHoloer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_data_list,
                parent, false);
        ViewHoloer viewHolder = new ViewHoloer(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoloer holder, int position) {

        holder.text_footnum.setText(footnums.get(position));
        holder.text_username.setText(BaseApplication.getInstance().getUserID());

        if(null !=onItemClick){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onIteamClick(position);
                }
            });
        }

    }


    public interface OnItemClick{
        void onIteamClick(int position);
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemCount() {
        return null ==footnums?0:footnums.size();
    }
}
