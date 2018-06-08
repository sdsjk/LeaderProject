package com.seaboxdata.portal.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.scrollablelayout.ScrollableLayout;

/**
 * Created by zjianning on 2017/3/31.
 */

public class CurScrollableLayout extends ScrollableLayout {

    private boolean isScrollTo = true;
    private int lastX;
    private int lastY;

    public CurScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CurScrollableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurScrollableLayout(Context context) {
        super(context);
    }

    public void setScrollTo(boolean scrollTo) {
        isScrollTo = scrollTo;
    }

    @Override
    public void scrollTo(int x, int y) {

        if (isScrollTo) {
            super.scrollTo(x, y);
        } else {

        }

    }

    private boolean enable = true;

    public void setScrollEnable(boolean enable) {
        if(enable!=this.enable){
            this.enable = enable;
            //重新绘制,不然无法生效
            this.requestLayout();
        }else{
            this.enable = enable;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if(enable){
            return super.dispatchTouchEvent(e);
        }
        boolean isIntercept = false;
        int action = e.getAction();
        int currentX = (int) e.getX();
        int currentY = (int) e.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                isIntercept = false;//点击事件分发给子控件
                lastX = currentX;
                lastY = currentY;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isParentIntercept(currentX,currentY,lastX,lastY)){//父容器拦截
                    isIntercept = true;
                }else {//点击事件分发给子控件
                    isIntercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = false;//点击事件分发给子控件
                break;
        }
        //记录上次滑动的位置
        if(isIntercept){
            return isIntercept;
        }

        return super.dispatchTouchEvent(e);
    }

    private boolean isParentIntercept(int currentX, int currentY, int lastX, int lastY) {

        if(Math.abs(currentY-lastY)>Math.abs(currentX-lastX)*8/10){
            return true;
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(enable){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        //如果是dis状态,则让高度减去第一个view
        View mHeadView = this.getChildAt(0);
        int headerViewHeight = 0;
        if(mHeadView != null) {
            this.measureChildWithMargins(mHeadView, widthMeasureSpec, 0, 0, 0);
            headerViewHeight = mHeadView.getMeasuredHeight();
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - headerViewHeight, 1073741824));
    }

    public interface ViewDataContaier{
        public boolean isEmpty();

        public void setScrollableLayout(CurScrollableLayout curScrollableLayout);
    }
}
