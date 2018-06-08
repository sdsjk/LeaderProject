package com.linewell.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * 获取metadata的工具类
 * @author lyixin
 * @since 2017/8/18.
 */
public class MetaDataUtils {

    /**
     * 获取metadata值
     * @param context
     * @param metaName
     * @param defaultValue
     * @return
     */
    public static boolean getAppMetaDataBoolean(Context context, String metaName, boolean defaultValue) {
        try {
            //application标签下用getApplicationinfo，如果是activity下的用getActivityInfo
            boolean value = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getBoolean(metaName, defaultValue);
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 获取metadata值
     * @param context
     * @param metaName
     * @param defaultValue
     * @return
     */
    public static String getAppMetaDataString(Context context, String metaName, String defaultValue) {
        try {
            //application标签下用getApplicationinfo，如果是activity下的用getActivityInfo
            String value = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString(metaName, defaultValue);
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 获取metadata值
     * @param context
     * @param metaName
     * @return
     */
    public static Object getAppMetaData(Context context, String metaName) {
        try {
            //application标签下用getApplicationinfo，如果是activity下的用getActivityInfo
            Object value = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.get(metaName);
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
