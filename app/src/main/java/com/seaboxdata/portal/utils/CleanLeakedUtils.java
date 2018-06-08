package com.seaboxdata.portal.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * 内存泄漏工具类
 * Created by linyixin on 2017/12/29.
 */

public class CleanLeakedUtils {

    public static void fixInputMethodManagerLeaked(Context destContext){
        if (destContext == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }

        String [] viewArray = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field filed;
        Object filedObject;

        for (String view:viewArray) {
            try{
                filed = inputMethodManager.getClass().getDeclaredField(view);
                if (!filed.isAccessible()) {
                    filed.setAccessible(true);
                }
                filedObject = filed.get(inputMethodManager);
                if (filedObject != null && filedObject instanceof View) {
                    View fileView = (View) filedObject;
                    if (fileView.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        filed.set(inputMethodManager, null); // 置空，破坏掉path to gc节点
                    } else {
                        break;// 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }

    public static void fixHuaWeiMemoryLeak(Activity activity){
        if (!"HUAWEI".equals(Build.MANUFACTURER)) {
            return;
        }

        //测试
        try {
            Class<?> GestureBoostManagerClass = Class.forName("android.gestureboost.GestureBoostManager");
            Field sGestureBoostManagerField = GestureBoostManagerClass.getDeclaredField("sGestureBoostManager");
            sGestureBoostManagerField.setAccessible(true);
            Object gestureBoostManager = sGestureBoostManagerField.get(GestureBoostManagerClass);
            Field contextField = GestureBoostManagerClass.getDeclaredField("mContext");
            contextField.setAccessible(true);
            if (contextField.get(gestureBoostManager)==activity) {
                contextField.set(gestureBoostManager, null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        //测试
        try {
            Class<?> LeakedClass = Class.forName("android.rms.iaware.FastgrabConfigReader");
            Field mLeakedField = LeakedClass.getDeclaredField("mFastgrabConfigReader");
            mLeakedField.setAccessible(true);
            Object gestureBoostManager = mLeakedField.get(LeakedClass);
            Field contextField = LeakedClass.getDeclaredField("mContext");
            contextField.setAccessible(true);
            if (contextField.get(gestureBoostManager)==activity) {
                contextField.set(gestureBoostManager, null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
