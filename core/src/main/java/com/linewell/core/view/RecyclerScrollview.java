package com.linewell.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 重写的scrollview, 拦截所有子控件滑动事件, 保证列表滑动流畅性
 * @author lyixin
 * @since 2016/8/30.
 */
public class RecyclerScrollview extends ScrollView{

    private int downX;
    private int downY;
    private int mTouchSlop;

    private OnScrollToBottomListener onScrollToBottom;

    /**
     *  滑动监听接口
     */
    public interface ScrollViewListener {
        void onScrollChanged(ScrollView scrollView, int x, int y,
                             int oldx, int oldy);
    }

    /**
     * 滑动监听
     */
    private ScrollViewListener scrollViewListener = null;

    public RecyclerScrollview(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public RecyclerScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public RecyclerScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                  boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(scrollY > 0 && null != onScrollToBottom){
            onScrollToBottom.onScrollBottomListener(clampedY);
        }
    }

    public interface OnScrollToBottomListener{
        public void onScrollBottomListener(boolean isBottom);
    }

    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){
        onScrollToBottom = listener;
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
}
