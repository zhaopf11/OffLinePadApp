package com.kaihuang.bintutu.home.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.utils.Contants;

import java.util.List;

/**
 * Created by zhoux on 2017/8/17.
 */

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    public List<ScanResult> mScanResults;
    private Context context;

    // 取得WifiManager对象
    private WifiManager mWifiManager;
    private ConnectivityManager cm;
    public WifiListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_machinename;
        TextView text_contact;
        public ViewHolder(View view) {
            super(view);
            text_machinename = (TextView) view.findViewById(R.id.text_machinename);
            text_contact = (TextView) view.findViewById(R.id.text_contact);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(int position);

        void conntactClick(int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_wifi_list,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScanResult scanResult = mScanResults.get(position);
        holder.text_machinename.setText(scanResult.SSID);

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        String g1 = wifiInfo.getSSID();
        String g2 = "\"" + scanResult.SSID + "\"";
        NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        String desc = "链接";
       if (wifi ==NetworkInfo.State.CONNECTED) {
            if (g2.endsWith(g1)) {
                desc = "已链接";
                Contants.contactSSID = scanResult.SSID;
                holder.text_contact.setBackgroundResource(R.drawable.gray_corner_bg1);
                holder.text_contact.setTextColor(context.getResources().getColor(R.color.gray));
                holder.text_contact.setText(desc);
            }else {
                holder.text_contact.setBackgroundResource(R.drawable.orange_corner_bg);
                holder.text_contact.setTextColor(context.getResources().getColor(R.color.font_orange));
                holder.text_contact.setText(desc);
                holder.text_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickLitener.conntactClick(position);
                    }
                });
            }
        }else {
           holder.text_contact.setBackgroundResource(R.drawable.orange_corner_bg);
           holder.text_contact.setTextColor(context.getResources().getColor(R.color.font_orange));
           holder.text_contact.setText(desc);
           holder.text_contact.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   mOnItemClickLitener.conntactClick(position);
               }
           });
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

    @Override
    public int getItemCount() {
        return null == mScanResults ? 0 : mScanResults.size();
    }
}

