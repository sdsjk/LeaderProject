package com.seaboxdata.portal.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by zjianning on 2018/5/17.
 */

public class PopWindowUtil {

    private static PopWindowUtil instance;

    private PopupWindow mPopupWindow;

    // 私有化构造方法，变成单例模式
    private PopWindowUtil() {

    }

    // 对外提供一个该类的实例，考虑多线程问题，进行同步操作
    public static PopWindowUtil getInstance() {
        if (instance == null) {
            synchronized (PopWindowUtil.class) {
                if (instance == null) {
                    instance = new PopWindowUtil();
                }
            }
        }
        return instance;
    }

    /**
     * @param cx
     *            activity
     * @param view1
     *            传入内容的view
     * @return
     */
    public PopWindowUtil makePopupWindow(Context cx, View view1) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wmManager=(WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        wmManager.getDefaultDisplay().getMetrics(dm);

        mPopupWindow = new PopupWindow(cx);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // 设置PopupWindow的大小（宽度和高度）
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow的内容view
        mPopupWindow.setContentView(view1);
        mPopupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
        mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
        mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸

        return instance;
    }

    /**
     *
     * @param cx 此处必须为Activity的实例
     * @param view 显示在该控件之下
     * @param xOff 距离view的x轴偏移量
     * @param yOff 距离view的y轴偏移量
     * @return
     */
    public PopupWindow showLocationWithAnimation(final Context cx, View view,
                                                 int xOff, int yOff) {

//        mPopupWindow.setAnimationStyle(R.style.pop_anim);

        // 弹出PopupWindow时让后面的界面变暗
//        WindowManager.LayoutParams parms = ((Activity) cx).getWindow().getAttributes();
//        parms.alpha =0.5f;
//        ((Activity) cx).getWindow().setAttributes(parms);
//
//        int[] positon = new int[2];
//        view.getLocationOnScreen(positon);
        // 弹窗的出现位置，在指定view之下
        mPopupWindow.showAsDropDown(view,  xOff,  yOff);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // PopupWindow消失后让后面的界面变亮
//                WindowManager.LayoutParams parms = ((Activity) cx).getWindow().getAttributes();
//                parms.alpha =1.0f;
//                ((Activity) cx).getWindow().setAttributes(parms);

                if (mListener != null) {
                    mListener.dissmiss();
                }
            }
        });

        return mPopupWindow;
    }

    public interface OnDissmissListener{

        void dissmiss();

    }

    private OnDissmissListener mListener;

    public void setOnDissmissListener(OnDissmissListener listener) {
        mListener = listener;
    }
}
