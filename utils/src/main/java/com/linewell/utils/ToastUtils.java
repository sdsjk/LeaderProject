package com.linewell.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具
 * @author lyixin
 * @since 2016/7/25.
 */
public class ToastUtils {

    /**
     * 显示吐司
     * @param context   上下文
     * @param content   内容字符串
     * @param duration  Toast.LENGTH_SHORT/Toast.LENGTH_LONG
     */
    public static void show(Context context, String content, int duration){
        if(context==null){
            return;
        }
        Toast.makeText(context, content, duration).show();
    }

    /**
     * 显示吐司(默认时间为短)
     * @param context   上下文
     * @param content   内容字符串
     */
    public static void show(Context context, String content){
        show(context, content, Toast.LENGTH_SHORT);
    }


    /**
     * 显示吐司
     * @param context       上下文
     * @param contentResId  内容资源id
     * @param duration  Toast.LENGTH_SHORT/Toast.LENGTH_LONG
     */
    public static void show(Context context, int contentResId, int duration){
        if(context==null){
            return;
        }
        Toast.makeText(context, contentResId, duration).show();
    }

    /**
     * 显示吐司(默认时间为短)
     * @param context       上下文
     * @param contentResId  内容资源id
     */
    public static void show(Context context, int contentResId){
        show(context, contentResId, Toast.LENGTH_SHORT);
    }


    /**
     * 显示吐司
     * @param activity      activity
     * @param content   内容字符串
     * @param duration  Toast.LENGTH_SHORT/Toast.LENGTH_LONG
     */
    public static void show(Activity activity, String content, int duration){
        if(activity==null){
            return;
        }
        Toast.makeText(activity, content, duration).show();
    }

    /**
     * 显示吐司(默认时间为短)
     * @param activity      activity
     * @param content   内容字符串
     */
    public static void show(Activity activity, String content){
        show(activity, content, Toast.LENGTH_SHORT);
    }


    /**
     * 显示吐司
     * @param activity      activity
     * @param contentResId  内容资源id
     * @param duration  Toast.LENGTH_SHORT/Toast.LENGTH_LONG
     */
    public static void show(Activity activity, int contentResId, int duration){
        if(activity==null){
            return;
        }
        Toast.makeText(activity, contentResId, duration).show();
    }

    /**
     * 显示吐司(默认时间为短)
     * @param activity      activity
     * @param contentResId  内容资源id
     */
    public static void show(Activity activity, int contentResId){
        show(activity, contentResId, Toast.LENGTH_SHORT);
    }
}
