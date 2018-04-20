package com.kaihuang.bintutu.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.mine.bean.TeamBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/18.
 */

public class TeamAdapter extends RecyclerView.Adapter <TeamAdapter.ViewHoloer>{
    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;

    public List<TeamBean> teamBeen;

    public  TeamAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;

    }
    @Override
    public ViewHoloer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_team,
                parent, false);
        ViewHoloer viewHolder = new ViewHoloer(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoloer holder, int position) {
        TeamBean teamBean = teamBeen.get(position);
        holder.text_teamname.setText(teamBean.getSalesmanName());
        if(null !=mOnItemClickLitener){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(holder.itemView,position);
                }
            });


        }

    }

    class ViewHoloer extends RecyclerView.ViewHolder{

        TextView text_teamname;
        public ViewHoloer(View itemView) {
            super(itemView);
            text_teamname =(TextView)itemView.findViewById(R.id.text_teamname);

        }
    }

    @Override
    public int getItemCount() {
        return null ==teamBeen?0:teamBeen.size();
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
