package com.linewell.core.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.linewell.core.R;
import com.linewell.core.activity.PortraitActivity;

import java.util.List;

/**
 * 判断是否有权限
 * Created by caicai on 2016-08-09.
 */
public class PermissionUtils {

    /**
     * 获取网络状态
     */
    public static String ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
    /**
     * 获取WiFi状态
     */
    public static String ACCESS_WIFI_STATE = "android.permission.ACCESS_WIFI_STATE";
    /**
     * 读取电话状态
     */
    public static String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    /**
     * 写入外部存储
     */
    public static String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static String STR_WRITE_EXTERNAL_STORAGE = "【外部存储写入】";
    /**
     * 访问网络
     */
    public static String INTERNET = "android.permission.INTERNET";
    /**
     * 读取系统日志
     */
    public static String READ_LOGS = "android.permission.READ_LOGS";
    /**
     * 获取精确位置
     */
    public static String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static String STR_ACCESS_FINE_LOCATION = "【位置信息】";
    /**
     * 获取错略位置
     */
    public static String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    /**
     * 获取任务信息
     */
    public static String GET_TASKS = "android.permission.GET_TASKS";
    /**
     * 设置调试程序
     */
    public static String SET_DEBUG_APP = "android.permission.SET_DEBUG_APP";
    /**
     * 显示系统窗口
     */
    public static String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";
    /**
     * 访问账户Gmail列表
     */
    public static String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
    /**
     * 使用证书
     */
    public static String USE_CREDENTIALS = "android.permission.USE_CREDENTIALS";
    /**
     * 管理账户
     */
    public static String MANAGE_ACCOUNTS = "android.permission.MANAGE_ACCOUNTS";
    /**
     * 录音
     */
    public static String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static String STR_RECORD_AUDIO = "【录音】";
    /**
     * SD卡访问
     */
    public static String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static String STR_READ_EXTERNAL_STORAGE = "【存储信息读取】";

    /**
     * 访问摄像头
     */
    public static String READ_CAMERA = "android.permission.CAMERA";
    public static String STR_READ_CAMERA = "【相机】";
    /**
     * 拨号
     */
    public static String CALL_PHONE="android.permission.CALL_PHONE";
    public static String STR_CALL_PHONE = "【拨号】";

    /**
     * 包名
     */
    private static String PN = "com.innochina.skillshare";

    /**
     * 读取联系人权限
     */
    public static String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static String STR_READ_CONTACTS = "【读取联系人信息】";

    /**
     * 需要特殊处理的机型
     */
    private static String HUAWEI = "HONOR";

    /**
     * 权限请求码
     */
    public static final int PERMISSION_CODE=12345;

    /**
     * 是否允许访问摄像头
     */
    public static boolean isREAD_CAMERA(Context context) {
        return isPermission(context, READ_CAMERA);
    }

    /**
     * 是否允许网络访问
     *
     * @param context
     * @return
     */
    public static boolean isACCESS_NETWORK_STATE(Context context) {
        return isPermission(context, ACCESS_NETWORK_STATE);
    }

    /**
     * 是否连接WIFI
     *
     * @param context
     * @return
     */
    public static boolean isACCESS_WIFI_STATE(Context context) {
        return isPermission(context, ACCESS_WIFI_STATE);
    }

    /**
     * 是否读取电话状态
     *
     * @param context
     * @return
     */
    public static boolean isREAD_PHONE_STATE(Context context) {
        return isPermission(context, READ_PHONE_STATE);
    }

    /**
     * 是否允许写入外部存储
     *
     * @param context
     * @return
     */
    public static boolean isWRITE_EXTERNAL_STORAGE(Context context) {
        return isPermission(context, WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 是否允许网络访问
     *
     * @param context
     * @return
     */
    public static boolean isINTERNET(Context context) {
        return isPermission(context, INTERNET);
    }

    /**
     * 是否允许读取日志
     *
     * @param context
     * @return
     */
    public static boolean isREAD_LOGS(Context context) {
        return isPermission(context, READ_LOGS);
    }

    /**
     * 是否允许获取精确信息
     *
     * @param context
     * @return
     */
    public static boolean isACCESS_FINE_LOCATION(Context context) {
        return isPermission(context, ACCESS_FINE_LOCATION);
    }

    /**
     * 是否允许获取粗略信息
     *
     * @param context
     * @return
     */
    public static boolean isACCESS_COARSE_LOCATION(Context context) {
        return isPermission(context, ACCESS_COARSE_LOCATION);
    }

    /**
     * 是否允许获取任务列表
     *
     * @param context
     * @return
     */
    public static boolean isGET_TASKS(Context context) {
        return isPermission(context, GET_TASKS);
    }

    /**
     * 是否允许调试
     *
     * @param context
     * @return
     */
    public static boolean isSET_DEBUG_APP(Context context) {
        return isPermission(context, SET_DEBUG_APP);
    }

    /**
     * 是否允许显示系统窗口
     *
     * @param context
     * @return
     */
    public static boolean isSYSTEM_ALERT_WINDOW(Context context) {
        return isPermission(context, SYSTEM_ALERT_WINDOW);
    }

    /**
     * 是否允许访问账户Gmail列表
     *
     * @param context
     * @return
     */
    public static boolean isGET_ACCOUNTS(Context context) {
        return isPermission(context, GET_ACCOUNTS);
    }

    /**
     * 是否允许使用证书
     *
     * @param context
     * @return
     */
    public static boolean isUSE_CREDENTIALS(Context context) {
        return isPermission(context, USE_CREDENTIALS);
    }

    /**
     * 是否允许管理账户
     *
     * @param context
     * @return
     */
    public static boolean isMANAGE_ACCOUNTS(Context context) {
        return isPermission(context, MANAGE_ACCOUNTS);
    }

    /**
     * 是否允许使用录音功能
     *
     * @param context
     * @return
     */
    public static boolean isRECORD_AUDIO(Context context) {
        return isPermission(context, RECORD_AUDIO);
    }

    /**
     * 是否允许拨打电话
     * @param context
     * @return
     */
    public static boolean isCALL_PHONE(Context context){
        return isPermission(context,CALL_PHONE);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 是否允许访问SD卡
     *
     * @param context
     * @return
     */
    public static boolean isREAD_EXTERNAL_STORAGE(Context context) {
        boolean flag = isPermission(context, READ_EXTERNAL_STORAGE);
        if (HUAWEI.equals(Build.BRAND)) {//华为荣耀特殊处理
            if (!flag) {
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        }
        return flag;
//        return isPermission(context, READ_EXTERNAL_STORAGE);
    }

    /**
     * 读取用户联系人权限
     *
     * @param context
     * @return
     */
    public static boolean isREAD_CONTACTS(Activity context) {
        return isPermission(context, READ_CONTACTS);
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
     * 申请权限(总入口)
     * @param activity
     * @param permission
     * @param onPermissionResultListener
     */
    public static void requestPermission(Activity activity, String permission, OnPermissionResultListener onPermissionResultListener) {
        if(isPermission(activity, permission)){
            if(onPermissionResultListener!=null){
                onPermissionResultListener.onSuccess(PERMISSION_CODE, new String[]{permission});
            }
            return;
        }

        if(activity instanceof PortraitActivity){
            ((PortraitActivity)activity).setPerssionResultListener(permission, onPermissionResultListener);
        }

        //针对魅族做特殊处理
        if("Meizu".equals(Build.MANUFACTURER)||"OPPO".equals(Build.MANUFACTURER)){
            startAppSetting(activity,permission, onPermissionResultListener);
            return;
        }
        if(Build.VERSION.SDK_INT >= 23){
            requestPermission(activity,permission,PERMISSION_CODE);
            return ;
        }else{
            startAppSetting(activity,permission, onPermissionResultListener);
            return;
        }
    }
    /**
     * 申请权限
     *
     * @param activity
     * @param permission
     * @param requestCode
     */
    public static void requestPermission(Activity activity, String permission, int requestCode) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
//                permission)) {
//
//            // Show an explanation to the user *asynchronously* -- don't block
//            // this thread waiting for the user's response! After the user
//            // sees the explanation, try again to request the permission.
//            Log.i(DEBUG_TAG, "we should explain why we need this permission!");
//        } else {
//
//            // No explanation needed, we can request the permission.
//            Log.i(DEBUG_TAG, "==request the permission==");


        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                requestCode);

        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
    }
//    }

    /**
     * 跳转应用设置详情页面
     *
     * @param activity
     * @param requestCode
     */
    public static void getAppDetailSettingIntent(Activity activity, int requestCode) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivityForResult(localIntent, requestCode);
    }


    /**
     * 跳转去APP的设置页面
     * @param context
     */
    public static void requestPermission(Activity context,String permission){
        requestPermission(context, permission, new OnPermissionResultListener() {
            @Override
            public void onSuccess(int requestCode, @NonNull String[] permissions) {

            }

            @Override
            public void onCancel(int requestCode, @NonNull String[] permissions) {

            }
        });
    }


    /**
     * 跳转去APP的设置页面
     * @param context
     */
    public static AlertDialog startAppSetting(final Context context,final String permission, final OnPermissionResultListener onPermissionResultListener){


        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.permission);
        dialog.setMessage("当前应用缺少" + getDialogTipsPart(getTips(permission))
                + "权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。");
        dialog.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name= Build.MANUFACTURER;
                if("HUAWEI".equals(name)){
                    goHuaWeiMainager(context);
                }else if ("vivo".equals(name)){
                    goVivoMainager(context);
                }else if ("OPPO".equals(name)){
                    goOppoMainager(context);
                }else if ("Coolpad".equals(name)){
                    goCoolpadMainager(context);
                }else if ("Meizu".equals(name)){
                    goMeizuMainager(context);
                    // getAppDetailSettingIntent();
                }else if ("Xiaomi".equals(name)){
                    goXiaoMiMainager(context);
                }else if ("samsung".equals(name)){
                    goSangXinMainager(context);
                }else if("OnePlus".equals(name)){
                    goOnePlusMainager(context);
                }else{
                    goIntentSetting(context);
                }
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        if(onPermissionResultListener!=null){
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    onPermissionResultListener.onCancel(PERMISSION_CODE, new String[]{permission});
                }
            });
        }
        AlertDialog customDialog = dialog.create();
        customDialog.show();
        return customDialog;
    }

    private static String getTips(String permission){
        return "";
    }

    /**
     * 获取权限类型提示
     */
    private static String getDialogTipsPart(){
        return "必要";
    }

    /**
     * 获取权限类型提示
     */
    private static String getDialogTipsPart(String tip){
        if(TextUtils.isEmpty(tip)){
            getDialogTipsPart();
        }else{
            return tip;
        }
        return "";
    }


    /**
     * 跳转到华为设置
     * @param context
     */
    private static void goHuaWeiMainager(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
//            Toast.makeText(MainActivity.this, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    /**
     * 跳转小米
     * @param context
     */
    private static void goXiaoMiMainager(Context context){
        try {
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter","com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            goIntentSetting(context);
        }
    }

    /**
     * 跳转1+手机
     * @param context
     */
    private static void goOnePlusMainager(Context context){
        try {
            Intent localIntent = new Intent();
            localIntent
                    .setClassName("com.oneplus.security",
                            "com.oneplus.security.permission.PermissionAppMainActivity");
            localIntent.putExtra("extra_pkgname",context.getPackageName());
            context.startActivity(localIntent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            goIntentSetting(context);
        }
    }

    /**
     * 跳转魅族
     * @param context
     */
    private static void goMeizuMainager(Context context){
        try {
            Intent intent=new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", PN);
            context.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            goIntentSetting(context);
        }
    }

    /**
     * 跳转oppo
     */
    private static void goOppoMainager(Context context){

        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", PN);
            ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            doStartApplicationWithPackageName(context,"com.color.safecenter");
        }

    }

    /**
     * 乐视手机
     * @param context
     */
    private static void goLeshiMainager(Context context){
        try{
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", PN);
            ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
            intent.setComponent(comp);
        }catch (ActivityNotFoundException localActivityNotFoundException){
            doStartApplicationWithPackageName(context,"com.color.safecenter");
        }
    }
    /**
     * 跳转三星
     * @param context
     */
    private static void goSangXinMainager(Context context){
        //三星4.3可以直接跳转
        goIntentSetting(context);
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     startActivity(open);
     本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
     */
    private static void goCoolpadMainager(Context context){
        doStartApplicationWithPackageName(context,"com.yulong.android.security:remote");
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    //vivo
    private static void goVivoMainager(Context context){
        doStartApplicationWithPackageName(context,"com.bairenkeji.icaller");
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }


    /**
     * 通过包名开启设置
     * @param context
     * @param packagename
     */
    private static void doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
//        Log.i("MainActivity","resolveinfoList"+resolveinfoList.size());
//        for (int i = 0; i < resolveinfoList.size(); i++) {
//            Log.i("MainActivity",resolveinfoList.get(i).activityInfo.packageName+resolveinfoList.get(i).activityInfo.name);
//        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            try {
                context.startActivity(intent);
            }catch (Exception e){
                goIntentSetting(context);
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转到设置页面
     * @param context
     */
    private static void goIntentSetting(Context context){
        int xx=Build.VERSION.SDK_INT;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",context.getPackageName(), null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public interface OnPermissionResultListener{
        void onSuccess(int requestCode, @NonNull String[] permissions);

        void onCancel(int requestCode, @NonNull String[] permissions);
    }

}
