package com.kaihuang.bintutu.utilviews;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.kaihuang.bintutu.home.bean.TagViewBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaopf on 2018/2/28.
 */

public class PictureTagLayout extends RelativeLayout implements View.OnTouchListener {
    private static final int CLICKRANGE = 5;
    int startX = 0;
    int startY = 0;
    int startTouchViewLeft = 0;
    int startTouchViewTop = 0;
    private View touchView, clickView;
    private boolean mIsLongPressed = false;
    private long lastDownTime;
    public List<TagViewBean> tagViewList = new ArrayList<>();//所有标记位置
    public ArrayList<String> descList = new ArrayList<>();//所有标记描述详情信息
    public static TagViewBean mtagViewBean;
    private InputPictureDialog inputPictureDialog;

    public PictureTagLayout(Context context) {
        super(context, null);
    }

    public PictureTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastDownTime = System.currentTimeMillis();
                touchView = null;

                if (clickView != null) {
                    ((PictureTagView) clickView).setStatus(PictureTagView.Status.Normal);
                    clickView = null;
                }
                startX = (int) event.getX();
                startY = (int) event.getY();
                if (hasView(startX, startY)) {
                    startTouchViewLeft = touchView.getLeft();
                    startTouchViewTop = touchView.getTop();
                } else {
                    mtagViewBean = new TagViewBean();
                    mtagViewBean.setX(startX);
                    mtagViewBean.setY(startY);
                    mtagViewBean.setIndex(tagViewList.size() + 1);
                    tagViewList.add(mtagViewBean);
                    addItem(startX, startY);
                }
                onClick.openInputDialog();
                break;
            case MotionEvent.ACTION_MOVE:
//                long eventTime = System.currentTimeMillis();
//                if (!mIsLongPressed) {
//                    mIsLongPressed = isLongPressed((float) event.getX(), (float) event.getY(), startX, startY, lastDownTime, eventTime, 200);
//                }
//                if (mIsLongPressed) {
//                    //长按模式所做的事
//                    ((PictureTagView) touchView).setStatus(PictureTagView.Status.Gone);
//                } else {
                //移动模式所做的事
//                    moveView((int) event.getX(),
//                            (int) event.getY());
//                }
                break;
            case MotionEvent.ACTION_UP:
                mIsLongPressed = false;
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                //如果挪动的范围很小，则判定为单击
                if (touchView != null && Math.abs(endX - startX) < CLICKRANGE && Math.abs(endY - startY) < CLICKRANGE) {
                    //当前点击的view进入编辑状态
//                    ((PictureTagView) touchView).setStatus(PictureTagView.Status.Edit);
                    clickView = touchView;
                }
                touchView = null;
                break;
        }
        return true;
    }


    private void addItem(int x, int y) {
        View view = null;
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        params.leftMargin = x - 20;
        view = new PictureTagView(getContext(), PictureTagView.Direction.Left);

        params.topMargin = y - 20;
        //上下位置在视图内
        if (params.topMargin < 0) {
            params.topMargin = 0;
        } else if ((params.topMargin + PictureTagView.getViewHeight()) > getHeight()) {
            params.topMargin = getHeight() - PictureTagView.getViewHeight();
        }
        PictureTagView.setStatus(PictureTagView.Status.Normal);
        this.addView(view, params);
    }

    /**
     * 移除上一次添加的图标
     */
    public void removeTagView() {
        if (tagViewList != null && tagViewList.size() > 0) {
            tagViewList.remove(tagViewList.size() - 1);
            if (descList != null && descList.size() > 0) {
                descList.remove(descList.size() - 1);
            }
            for (TagViewBean tagViewBean : tagViewList) {
                mtagViewBean = tagViewBean;
                addItem(tagViewBean.getX(), tagViewBean.getY());
            }
        }
    }

    private void moveView(int x, int y) {
        if (touchView == null) return;
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = x - startX + startTouchViewLeft;
        params.topMargin = y - startY + startTouchViewTop;
        //限制子控件移动必须在视图范围内
        if (params.leftMargin < 0 || (params.leftMargin + touchView.getWidth()) > getWidth())
            params.leftMargin = touchView.getLeft();
        if (params.topMargin < 0 || (params.topMargin + touchView.getHeight()) > getHeight())
            params.topMargin = touchView.getTop();
        touchView.setLayoutParams(params);
    }

    private boolean hasView(int x, int y) {
        //循环获取子view，判断xy是否在子view上，即判断是否按住了子view
        for (int index = 0; index < this.getChildCount(); index++) {
            View view = this.getChildAt(index);
            int left = (int) view.getX();
            int top = (int) view.getY();
            int right = view.getRight();
            int bottom = view.getBottom();
            Rect rect = new Rect(left, top, right, bottom);
            boolean contains = rect.contains(x, y);
            //如果是与子view重叠则返回真,表示已经有了view不需要添加新view了
            if (contains) {
                touchView = view;
                touchView.bringToFront();
                return true;
            }
        }
        touchView = null;
        return false;
    }

    /**
     * 判断是否有长按动作发生
     *
     * @param lastX         按下时X坐标
     * @param lastY         按下时Y坐标
     * @param thisX         移动时X坐标
     * @param thisY         移动时Y坐标
     * @param lastDownTime  按下时间
     * @param thisEventTime 移动时间
     * @param longPressTime 判断长按时间的阀值
     */
    private boolean isLongPressed(float lastX, float lastY, int thisX, int thisY, long lastDownTime, long thisEventTime, long longPressTime) {
        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
            return true;
        } else {
            return false;
        }
    }

    public OnButtonClick onClick;

    public void setOnButtonClick(OnButtonClick onClick) {
        this.onClick = onClick;
    }

    public interface OnButtonClick {
        void openInputDialog();
    }
}
