package com.kaihuang.bintutu.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.mine.bean.AchievementBean;

import java.util.List;

/**
 * Created by zhoux on 2017/9/28.
 */

public class TeamAchievementsAdapter extends RecyclerView.Adapter <TeamAchievementsAdapter.ViewHoloer>{
    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;

    public List<AchievementBean> achievementBeen;

    public  TeamAchievementsAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;

    }
    @Override
    public ViewHoloer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_team_achievments,
                parent, false);
        ViewHoloer viewHolder = new ViewHoloer(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoloer holder, int position) {
        AchievementBean achievementBean = achievementBeen.get(position);
        holder.text_teamname.setText(achievementBean.getName());
        holder.text_date.setText(achievementBean.getMeasurecode());

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
        TextView text_date;
        public ViewHoloer(View itemView) {
            super(itemView);
            text_teamname =(TextView)itemView.findViewById(R.id.text_teamname);
            text_date = (TextView)itemView.findViewById(R.id.text_date);

        }
    }

    @Override
    public int getItemCount() {
        return null ==achievementBeen?0:achievementBeen.size();
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}

