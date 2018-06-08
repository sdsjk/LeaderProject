package com.linewell.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 系统工具类
 *
 * @author lzhiwei@linewell.com
 * @since 2014-3-18
 */
public class SystemUtils {
    private static final String TAG = "SystemUtils";

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 是否为手机网络
     *
     * @param context 上下文
     * @return
     */
    public static boolean isMobileActive(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (null == info) {// 无网络
            return false;
        } else {
            // wifi网络
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return false;
            } else {// 手机网络
                return true;
            }
        }
    }

    /**
     * 是否联网
     *
     * @param context 上下文
     * @return
     */
    public static boolean isNetConnected(Context context) {

        if (null != context) {
            context = context.getApplicationContext();
        } else {
            return false;
        }

        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (null == info) {// 无网络
            Log.d(TAG, "net is not connected!");
            return false;
        } else {
            Log.d(TAG, "net is connected:" + info.getTypeName());
            return true;
        }
    }

    public static final String NET_WIFI = "wifi";
    public static final String NET_2G = "2g";
    public static final String NET_3G = "3g";
    public static final String NET_4G = "4g";
    public static final String NET_UNKNOW = "";

    /**
     * 获取当前网络类型
     *
     * @param context
     * @return
     */
    public static String getNetConnectType(Context context) {
        if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
            return "";
        }

        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (null == info) {// 无网络
            return null;
        }

        // wifi网络
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return NET_WIFI;
        }

        // 手机网络
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

            String strNetworkType = null;

            String _strSubTypeName = info.getSubtypeName();

            // TD-SCDMA   networkType is 17
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    strNetworkType = NET_2G;
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    strNetworkType = NET_3G;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    strNetworkType = NET_4G;
                    break;
                default:
                    // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                        strNetworkType = NET_3G;
                    } else {
                        strNetworkType = _strSubTypeName;
                    }

                    break;
            }

            return strNetworkType;
        }

        return null;

    }

    /**
     * 判断是否存在SD卡
     *
     * @return
     */
    public static boolean isExistSdCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取sd卡的根路径
     *
     * @return sd卡的根路径, 没有的话返回null
     */
    public static String getSdCardRootPath() {
        if (isExistSdCard()) {
            return Environment.getExternalStorageDirectory().toString();// 获取跟目录
        }
        return null;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context     上下文
     * @param packageName 程序的包名
     * @return 正在运行true，否则false
     */
    public static boolean isTopRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打开网络设置
     *
     * @param context 上下文
     */
    public static void openNetWorkSetting(Context context) {
        Intent intent = null;
        // 判断手机系统的版本 即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            // lyx modify 2014-6-26
            // intent = new
            // Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    /**
     * 获取IMEI号
     *
     * @param context 上下文
     * @return
     */
    public static String getImei(Context context) {
        if (!isPermission(context, READ_PHONE_STATE)) {
            Log.e(TAG, "no permission");//解决魅族5获取不到设备id的问题
            return getUniquePsuedoID();
        }
        String imei = "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        imei = manager.getDeviceId();
        if (!TextUtils.isEmpty(imei)) {
            Log.w(TAG, imei);//解决魅族5获取不到设备id的问题
        } else {
            return getUniquePsuedoID();
        }
        return imei;
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取IMSI号
     *
     * @param context 上下文
     * @return
     */
    public static String getImsi(Context context) {
        if (!checkPermission(context, READ_PHONE_STATE)) {
            return "";
        }
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = manager.getSubscriberId();
        ;
        return null == imsi ? "" : imsi;
    }

    /**
     * 强制隐藏键盘 2014年7月17日
     *
     * @param activity
     * @param flags    键盘隐藏模式
     */
    public static void hideSoftInput(Activity activity, int flags) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), flags); // 强制隐藏键盘
        } catch (Exception e) {
        }
    }

    /**
     * 强制隐藏键盘 2014年7月17日
     *
     * @param activity
     * @param flags    键盘隐藏模式
     */
    public static void hideSoftInput(Activity activity, View view, int flags) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), flags); // 强制隐藏键盘
    }

    /**
     * 强制显示软键盘
     *
     * @param activity
     * @param view
     * @param flags    键盘显示模式
     */
    public static void showSoftInput(Activity activity, View view, int flags) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, flags);// 强制显示键盘
    }

    /**
     * 检查是否有权限
     *
     * @param context
     * @return
     */
    public static boolean checkPermission(Context context, String permissionName) {
        PackageManager pm = context.getPackageManager();
        boolean permission = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int selfPermission = context.checkSelfPermission(permissionName);
            return selfPermission == PackageManager.PERMISSION_GRANTED;
        } else {
            permission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(permissionName, context.getPackageName()));
        }
        return permission;
    }

    /**
     * 获取权限是否开启
     *
     * @param context
     * @return
     */
    private static boolean isPermission(Context context, String appStr) {

        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(appStr, context.getPackageName()));

        return permission;
    }

    /**
     * 动态获取权限,6.0以上使用
     * 需要在activity内部重写onActivityResult来处理用户是否允许权限
     *
     * @param activity
     * @param permissionName
     * @param requestCode
     * @see /http://jijiaxin89.com/2015/08/30/Android-s-Runtime-Permission/
     */
    public static void requestPermission(Activity activity, String permissionName, int requestCode) {
        if (!checkPermission(activity, permissionName)) {
            if (Build.VERSION.SDK_INT >= 23) {
                activity.requestPermissions(new String[]{permissionName},
                        requestCode);
            }
        }
    }

    /**
     * 根据stringName及string文件获取string文件中的stringValue的标识
     *
     * @param rClass
     * @param stringName
     * @return 要获取String的标识
     */
    public static int getStringUnid(Class<?> rClass, String stringName) {
        Class<?> c = rClass;
        Field field = null;
        int value = 0;
        try {
            field = c.getDeclaredField(stringName);
            value = field.getInt(null);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return value;
    }

    /**
     * 获取图片的资源
     *
     * @param stringName
     * @return
     */
    public static int getDrawableUnid(Context context, String stringName) {
        Resources res = context.getResources();
        int i = res.getIdentifier(stringName, "drawable", context.getPackageName());
        return i;
    }

    /**
     * 运维管理清除内部缓存
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteDir(context.getCacheDir());
        //Toast.makeText(context, "清除内部缓存", Toast.LENGTH_SHORT).show();
    }

    /**
     * * 运维管理清除本应用所有数据库
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {
        deleteDir(new File("/data/data/"
                + context.getPackageName() + "/databases"));
        //Toast.makeText(context, "清除数据库成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * * 清除本应用SharedPreference(包括清除用户名缓存)
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("username");
        editor.commit();

        //Toast.makeText(context, "清除本应用SharedPreference", Toast.LENGTH_SHORT).show();
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    public static void cleanFiles(Context context) {
        deleteDir(context.getFilesDir());
        //Toast.makeText(context, "清除文件夹", Toast.LENGTH_SHORT).show();
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
            //Toast.makeText(context, "清除外部cache", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     */
    public static void cleanApplicationData(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        //Toast.makeText(context, "清除完毕", Toast.LENGTH_SHORT).show();
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public static void cleanCache(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
//		cleanDatabases(context);
        cleanSharedPreference(context);
//		cleanFiles(context);
        //Toast.makeText(context, "清除完毕", Toast.LENGTH_SHORT).show();
    }


    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param dir
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 计算运维APP内存大小
     *
     * @param context
     * @return
     */
    public static String calculateCacheSize(Context context) {
        // 计算缓存大小
        long fileSize = calculateCacheSizeLong(context);
        String cacheSize = "0KB";

        if (fileSize > 0)
            cacheSize = formatFileSize(fileSize);
        return cacheSize;
    }

    /**
     * 计算运维APP内存大小
     *
     * @param context
     * @return
     */
    public static long calculateCacheSizeLong(Context context) {
        // 计算缓存大小
        long fileSize = 0;

        // /data/data/package_name/files
        //File filesDir = context.getFilesDir();

        // /data/data/package_name/cache
        File cacheDir = context.getCacheDir();

        //fileSize += getDirSize(filesDir);
//		fileSize += getDirSize(cacheDir);

        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {

            File externalCacheDir = context.getExternalCacheDir();//"<sdcard>/Android/data/<package_name>/cache/"
            fileSize += getDirSize(externalCacheDir);

        }
        return fileSize;
    }

    /**
     * 计算缓存大小
     *
     * @param context
     * @return
     */
    public static String calCacheSize(Context context) {
        SystemUtils systemUtils = new SystemUtils();
        return systemUtils.calculateCacheSize(context);
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS <= 0) {
            fileSizeString = "0KB";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 拨打电话
     *
     * @param activity
     * @param phone
     */
    public static void callPhone(Activity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        activity.startActivity(intent);
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
    public static boolean isExsitMianActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    /**
     * 文件存储根目录
     *
     * @param context
     * @return
     */
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 判断是否是debug模式
     *
     * @param context
     * @return
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    private static final String CHANNEL_KEY = "UMENG_CHANNEL";

    public static String getAppChannel(Context context) {
        ApplicationInfo appInfo;
        String channel = "innoskillshare";
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString(CHANNEL_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * 设置光标颜色
     * @param editText
     * @param resourceId
     */
    public static void setTextCursor(EditText editText, int resourceId) {

        try {
            java.lang.reflect.Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, resourceId);
        } catch (Exception e) {
        }
    }

}
