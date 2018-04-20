package com.kaihuang.bintutu.utilviews;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.home.adapter.WifiListAdapter;

import java.util.List;

/**
 * Created by zhaopf on 2018/3/31.
 */

public class BlueListAdapter extends RecyclerView.Adapter<BlueListAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    public List<BlueDeviceBean> blueDevicelist;
    private Context context;
    private BlueDeviceBean blueDevice;
    private OnItemClickLitener mOnItemClickLitener;
    private String[] mStateArray = {"未配对", "配对中", "已配对", "已连接"};
    public BlueListAdapter(Context context,List<BlueDeviceBean> blueDevicelist){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.blueDevicelist = blueDevicelist;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_wifi_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(blueDevicelist != null && blueDevicelist.size() > 0){
            blueDevice = blueDevicelist.get(position);
            if(TextUtils.isEmpty(blueDevice.getName())){
                holder.text_machinename.setText("未知名设备");
            }else{
                holder.text_machinename.setText(blueDevice.getName());
            }
            holder.text_contact.setText(mStateArray[blueDevice.getBondState()]);
        }


        if (null != mOnItemClickLitener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(position);
                }
            });
        }
    }


    public interface OnItemClickLitener {
        void onItemClick(int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @Override
    public int getItemCount() {
        return blueDevicelist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text_machinename;
        private TextView text_contact;
        public ViewHolder(View view) {
            super(view);
            text_machinename = (TextView) view.findViewById(R.id.text_machinename);
            text_contact = (TextView) view.findViewById(R.id.text_contact);
        }
    }
}
