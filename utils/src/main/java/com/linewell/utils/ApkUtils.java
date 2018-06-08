package com.linewell.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import java.io.File;
import java.util.List;

/**
 *
 */
public class ApkUtils {
    /**
     * 获取版本号
     *
     * @param context
     * @param packName
     * @return
     */
    public static int getVerCode(Context context, String packName) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(packName, 0).versionCode;
        } catch (NameNotFoundException e) {
            LogUtils.e(ApkUtils.class.getSimpleName(), e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            LogUtils.e(ApkUtils.class.getSimpleName(), e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @param packName
     * @return
     */
    public static String getVerName(Context context, String packName) {
        String verName = null;
        try {
            verName = context.getPackageManager().getPackageInfo(packName, 0).versionName;
        } catch (NameNotFoundException e) {
            LogUtils.e(ApkUtils.class.getSimpleName(), e.getMessage());
        }
        return verName;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = null;
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            LogUtils.e(ApkUtils.class.getSimpleName(), e.getMessage());
        }
        return verName;
    }

    /**
     * 根据安装包路径安装apk
     *
     * @param mContex 上下文
     * @param apkPath apk路径
     */
    public static void installApk(Context mContex, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContex.startActivity(intent);
    }

    /**
     * 进行版本比较，判断是否需要进行更新
     *
     * @param serverVersion 服务端版本信息
     * @param clientVersion 客服端版本信息
     * @return true--需要更新；false--不需要更新
     */
    public static boolean compareVersion(String serverVersion, String clientVersion) {
        boolean result = false;

        //将版本信息用"."分割
        List<String> clientStr = StringUtil.split(clientVersion, ".");
        List<String> serverStr = StringUtil.split(serverVersion, ".");

        //数组长度（防止数组越界）
        int len;
        if (clientStr.size() < serverStr.size()) {
            len = clientStr.size();
        } else {
            len = serverStr.size();
        }

        //比对每个版本号
        for (int i = 0; i < len; i++) {
            if (Integer.valueOf(serverStr.get(i)) > Integer.valueOf(clientStr.get(i))) {
                result = true;
                break;
            } else if (Integer.valueOf(serverStr.get(i)) < Integer.valueOf(clientStr.get(i))) {
                result = false;
                break;
            }
        }

        return result;
    }

}
