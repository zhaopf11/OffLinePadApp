package com.kaihuang.bintutu.utilviews;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhoux on 2017/7/18.
 */

public class FullOffDecoration extends RecyclerView.ItemDecoration  {

    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mSpace;
    }

    public FullOffDecoration(int space) {
        this.mSpace = space;
    }
}