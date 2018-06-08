package com.seaboxdata.portal.module.my.versionupdater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.google.gson.JsonObject;
import com.linewell.core.CommonApplicaton;
import com.linewell.core.exception.BugReporter;
import com.linewell.core.utils.PermissionUtils;
import com.linewell.http.AppHttpResultHandler;
import com.linewell.http.AppHttpUtils;
import com.linewell.http.file.Netroid;
import com.linewell.innochina.entity.dto.generalconfig.appversion.AppUpgradeDTO;
import com.linewell.innochina.entity.params.generalconfig.appversion.AppVersionParams;
import com.linewell.utils.ApkUtils;
import com.linewell.utils.FileUtil;
import com.linewell.utils.GsonUtil;
import com.linewell.utils.SharedPreferencesUtil;
import com.linewell.utils.ToastUtils;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CustomDialog;
import com.seaboxdata.portal.common.http.AppVersionResultHandler;
import com.seaboxdata.portal.config.SPKeyConstants;
import com.seaboxdata.portal.config.ServiceConfig;

import java.io.File;

/**
 * 版本检测更新
 *
 */
public class UpdateVersion {

    //上下文环境
    private Context mContext;

    //服务器上的最新版本
    private AppUpgradeDTO appUpgradeDTO;

    // 下载状态
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;
    private final static int DOWNLOADING = 2;

    private final static int SHOW_MANTORY_UPDATE=3;

    //下载进度
    private int curProgress;

    //下载的文件
    private File saveFile;

    //强制更新对话框
    private View dialogView;

    /**
     * 窗口监听接口
     */
    public interface DialogListener{
        void close();
    }

    private DialogListener dialogListener;

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public UpdateVersion(Context mContext){
        this.mContext = mContext;
    }

    /**
     * 从服务端获取最新的版本信息
     *
     * @param loadingStr 获取版本信息的请求提示
     *
     */
    public void getNewestVersion(String version, final AppVersionResultHandler<AppUpgradeDTO> appResultHandler, String loadingStr){

        AppVersionParams params = new AppVersionParams();
        params.setAppVersion(version);
        AppHttpUtils.postJson(mContext, ServiceConfig.BASE + "general/config/appversion/getVersion",
                params, new AppHttpResultHandler<Object>(){

                    @Override
                    public void onSuccess(Object result, JsonObject allResult) {

                        try {

                            AppUpgradeDTO appUpgradeDTO = GsonUtil.jsonToBean(result.toString(), AppUpgradeDTO.class);
                            switch (appUpgradeDTO.getUpgradeType()) {
                                case 0: // 最新
                                    appResultHandler.onNewest();
                                    break;
                                case 1: // 提示更新
                                case 2: // 强制
                                    appResultHandler.onUpdate(appUpgradeDTO);
                                    break;
                            }

                        }catch (Exception e) {
                            BugReporter.getInstance().postException(e);
                            appResultHandler.onNewest();
                        }

                    }

                    @Override
                    public void onFail(String message) {
                        appResultHandler.onNewest();
                    }

                    @Override
                    public boolean onSysFail(JsonObject allResult) {

                        appResultHandler.onNewest();

                        return super.onSysFail(allResult);
                    }
                }, loadingStr);

    }


    /**
     * 版本更新提示框
     *
     * @param serverVersion 服务端的版本信息
     */
    public void showManualUpdateDialog(final AppUpgradeDTO serverVersion){
        appUpgradeDTO = serverVersion;

        //下载更新对话框
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final CustomDialog dialog = new CustomDialog(mContext, R.style.curDialog);
        View layout = inflater.inflate(R.layout.pop_dialog, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        ((TextView)layout.findViewById(R.id.pop_dialog_title)).setText("发现新版本");
        layout.findViewById(R.id.pop_dialog_sub_title).setVisibility(View.VISIBLE);
        ((TextView)layout.findViewById(R.id.pop_dialog_sub_title)).setText(String.format(mContext.getString(R.string.update_version_code),
                serverVersion.getVersion()));
        ((TextView)layout.findViewById(R.id.pop_dialog_content)).setMovementMethod(new ScrollingMovementMethod());//设置文本框超过最大行数时可以滚动 2016-09-21
        ((TextView)layout.findViewById(R.id.pop_dialog_content)).setText(Html.fromHtml("更新内容：<br/>" + serverVersion.getUpdateContent()
                .replaceAll("\n", "<br/>")).toString());

        //立即更新按钮
        ((Button)layout.findViewById(R.id.pop_dialog_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                manualUpdate();
            }
        });

        if(serverVersion.getUpgradeType() == 2){
            ((Button)layout.findViewById(R.id.pop_dialog_ok)).setText("立刻更新");
            layout.findViewById(R.id.pop_dialog_cancle).setVisibility(View.GONE);
//            ((ImageView)layout.findViewById(R.id.update_version_iv)).setImageResource(R.drawable.img_force_update_dialog_bg);
        }else{
            ((Button)layout.findViewById(R.id.pop_dialog_ok)).setText("马上更新");
//            ((ImageView)layout.findViewById(R.id.update_version_iv)).setImageResource(R.drawable.img_free_update_dialog_bg);
            //取消
            ((Button)layout.findViewById(R.id.pop_dialog_cancle)).setText("以后再说");
            ((Button)layout.findViewById(R.id.pop_dialog_cancle)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(serverVersion.getUpgradeType() == 2 ){
                        //强制退出app
                        exitApp();
                    }else{
                        dialog.dismiss();
                    }
                }
            });
        }


        dialog.show();
    }

    public void manualUpdate(){
        if(!PermissionUtils.isWRITE_EXTERNAL_STORAGE(mContext)){
            PermissionUtils.requestPermission((Activity) mContext, PermissionUtils.WRITE_EXTERNAL_STORAGE, 1);
            ToastUtils.show(mContext, R.string.write_external_storage);
            return;
        }
        if(appUpgradeDTO==null){
            return;
        }
        //启动下载服务
        Intent updateIntent = new Intent(mContext, VersionUpdateService.class);
        updateIntent.putExtra("apkUrl",appUpgradeDTO.getUrl());
        updateIntent.putExtra("version",appUpgradeDTO.getVersion());
        updateIntent.putExtra("downloadDir",Environment.getExternalStorageDirectory()+"/"+mContext.getPackageName());

        mContext.startService(updateIntent);
    }

    /**
     * 版本自动检测更新提示框
     *
     * @param serverVersion 服务端的版本信息
     */
    public void showAutoUpdateDialog(final AppUpgradeDTO serverVersion){
        appUpgradeDTO = serverVersion;

        //下载更新对话框
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final CustomDialog dialog = new CustomDialog(mContext, R.style.curDialog);
        View layout = inflater.inflate(R.layout.pop_dialog, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        ((TextView)layout.findViewById(R.id.pop_dialog_title)).setText("发现新版本");
        layout.findViewById(R.id.pop_dialog_sub_title).setVisibility(View.VISIBLE);
        ((TextView)layout.findViewById(R.id.pop_dialog_sub_title)).setText(String.format(mContext.getString(R.string.update_version_code),
                serverVersion.getVersion()));
        ((TextView)layout.findViewById(R.id.pop_dialog_content)).setMovementMethod(new ScrollingMovementMethod());//设置文本框超过最大行数时可以滚动 2016-09-21
        ((TextView)layout.findViewById(R.id.pop_dialog_content)).setText(Html.fromHtml("更新内容：<br/>" + serverVersion.getUpdateContent()
                .replaceAll("\n", "<br/>")).toString());

        //立即更新按钮
        ((Button)layout.findViewById(R.id.pop_dialog_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                autoUpdate();
            }
        });

        if(serverVersion.getUpgradeType() == 2){
            layout.findViewById(R.id.pop_dialog_cancle).setVisibility(View.GONE);

//            ((ImageView)layout.findViewById(R.id.update_version_iv)).setImageResource(R.drawable.img_force_update_dialog_bg);


            ((Button)layout.findViewById(R.id.pop_dialog_ok)).setText("立刻更新");
        }else{
//            ((ImageView)layout.findViewById(R.id.update_version_iv)).setImageResource(R.drawable.img_free_update_dialog_bg);
            ((Button)layout.findViewById(R.id.pop_dialog_ok)).setText("马上更新");
            //取消
            ((Button)layout.findViewById(R.id.pop_dialog_cancle)).setText("以后再说");
            ((Button)layout.findViewById(R.id.pop_dialog_cancle)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(serverVersion.getUpgradeType() == 2){

                        //强制退出app
                        exitApp();
                    }else{

                        SharedPreferencesUtil.save(mContext, SPKeyConstants.UPDATE_SERVICES.KEY_VERSION, serverVersion.getVersion());

                        dialog.dismiss();
                        if(dialogListener!=null){
                            dialogListener.close();
                        }
                    }
                }
            });
        }

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void autoUpdate(){
        if(!PermissionUtils.isWRITE_EXTERNAL_STORAGE(mContext)){
            PermissionUtils.requestPermission((Activity) mContext, PermissionUtils.WRITE_EXTERNAL_STORAGE, 0);
            ToastUtils.show(mContext, R.string.write_external_storage);
            return;
        }

        if(appUpgradeDTO==null){
            return;
        }

        if(appUpgradeDTO.getUpgradeType() == 2){
            //强制更新下载
            Message message = updateHandler.obtainMessage();
            message.what = SHOW_MANTORY_UPDATE;
            updateHandler.sendMessage(message);

            mandatoryDownload(appUpgradeDTO);
        }else{
            //启动下载服务
            Intent updateIntent = new Intent(mContext, VersionUpdateService.class);
            updateIntent.putExtra("apkUrl",appUpgradeDTO.getUrl());
            updateIntent.putExtra("version",appUpgradeDTO.getVersion());
            updateIntent.putExtra("downloadDir", Environment.getExternalStorageDirectory()+"/"+mContext.getPackageName());

            mContext.startService(updateIntent);
        }
    }

    /**
     * 强制更新下载的对话框
     */
    public void mandatoryUpdate(){
        //下载更新对话框
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final CustomDialog mandatoryDialog = new CustomDialog(mContext, R.style.curDialog);
        dialogView = inflater.inflate(R.layout.pop_dialog_update_progress, null);
        mandatoryDialog.addContentView(dialogView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        //取消
        ((Button)dialogView.findViewById(R.id.pop_dialog_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mandatoryDialog.dismiss();

                //退出应用
                exitApp();
            }
        });

        mandatoryDialog.setCanceledOnTouchOutside(false);
        mandatoryDialog.setCancelable(false);
        mandatoryDialog.show();
    }

    /**
     * 新apk包的强制下载
     *
     * @param serverVersion         服务端的版本信息
     */
    private void mandatoryDownload(AppUpgradeDTO serverVersion){
        Netroid mNetroid = Netroid.getNetroid(mContext);
        String downloadDir = Environment.getExternalStorageDirectory()+"/"+mContext.getPackageName();
        boolean isExist = FileUtil.isFolderExists(downloadDir);

        if(isExist) {
            String filePath = downloadDir + "/" + mContext.getResources().getString(R.string.app_name) + ".apk";
            saveFile = new File(filePath);
            if (saveFile.exists()) {
                saveFile.delete();
            }

            //文件下载
            mNetroid.addFileDownload(filePath, serverVersion.getUrl(), new Listener<Void>() {
                @Override
                public void onSuccess(Void response) {//下载完成
                    Message message = updateHandler.obtainMessage();
                    message.what=DOWNLOAD_COMPLETE;
                    updateHandler.sendMessage(message);
                    curProgress = 0;
                }

                @Override
                public void onError(NetroidError error) {//下载出错
                    Message message = updateHandler.obtainMessage();
                    message.what = DOWNLOAD_FAIL;
                    updateHandler.sendMessage(message);
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onFinish() {

                }

                @Override
                public void onProgressChange(long fileSize, long downloadedSize) {
                    Message message = updateHandler.obtainMessage();
                    int progress =(int)(((float)downloadedSize / fileSize) * 100);


                    if(progress>curProgress){//下载进度变化进行通知
                        curProgress = progress;
                        Message msg = new Message();
                        msg.what = DOWNLOADING;
                        msg.obj = progress;
                        updateHandler.sendMessage(msg);
                    }
                }
            });
        }
    }


    /**
     * 强制更新下载过程中的事件处理的方法
     */
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_COMPLETE://当下载完毕，自动安装APK

                    //安装apk
                    ApkUtils.installApk(mContext, saveFile.getAbsolutePath());

                    //退出app
                    exitApp();

                    break;
                case DOWNLOAD_FAIL:// 下载失败
                    ((TextView)dialogView.findViewById(R.id.pop_dialog_download_text)).setText(mContext.getResources().getString(R.string.app_name)
                            + mContext.getResources().getString(R.string.update_version_download_error));

                    break;
                case DOWNLOADING://下载进行中
                    ((TextView)dialogView.findViewById(R.id.pop_dialog_process_text)).setText((int) msg.obj+"%");
                    ((ProgressBar)dialogView.findViewById(R.id.update_apk_propressBar)).setProgress((int) msg.obj);

                    break;
                case SHOW_MANTORY_UPDATE://强制更新对话框
                    //打开强制下载更新对话框
                    mandatoryUpdate();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 退出app
     */
    private void exitApp(){
        CommonApplicaton application = (CommonApplicaton) ((Activity) mContext).getApplication();
        application.finishAllActivities();

//        MobclickAgent.onKillProcess(mContext);

        System.exit(0);
    }

    public void setAppUpgradeDTO(AppUpgradeDTO appUpgradeDTO) {
        this.appUpgradeDTO = appUpgradeDTO;
    }
}
