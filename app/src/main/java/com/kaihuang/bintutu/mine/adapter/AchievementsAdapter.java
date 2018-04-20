package com.kaihuang.bintutu.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.mine.bean.AchievementBean;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoux on 2017/9/18.
 */

public class AchievementsAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;
    private Context context;
    public List<String> grouplist;
    public Map<String, List<AchievementBean>> childMap;

    public AchievementsAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return null == grouplist ? 0 : grouplist.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<AchievementBean> achievementBeen = childMap.get(grouplist.get(i));
        return null == achievementBeen ? 0 : achievementBeen.size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_group_achievements, parent, false);
        }
        TextView text_group_achievement = (TextView) convertView.findViewById(R.id.text_group_achievement);
        text_group_achievement.setText(grouplist.get(i));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.adapter_child_achievements, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AchievementBean achievementBean = childMap.get(grouplist.get(groupPosition)).get(childPosition);
        viewHolder.text_name.setText(achievementBean.getName() + "的足型数据");
        viewHolder.text_footnum.setText(achievementBean.getMeasurecode());
        return convertView;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_footnum;

        public ViewHolder(View view) {
            super(view);
            text_name = (TextView) view.findViewById(R.id.text_name);
            text_footnum = (TextView) view.findViewById(R.id.text_footnum);
        }


    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
