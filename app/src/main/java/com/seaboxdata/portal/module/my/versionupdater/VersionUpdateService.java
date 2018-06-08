package com.seaboxdata.portal.module.my.versionupdater;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.linewell.http.file.Netroid;
import com.linewell.utils.ApkUtils;
import com.linewell.utils.FileUtil;
import com.seaboxdata.portal.R;

import java.io.File;

/**
 * 版本更新下载服务
 *
 * @author cguangcong@linewell.com
 * @since 2016-08-31
 *
 */
public class VersionUpdateService extends Service {

    // 文件存储
    private File saveFile;
    private String apkUrl;

    // 通知栏
    private NotificationManager updateNotificationManager = null;
    private NotificationCompat.Builder mBuilder;

    // 下载状态
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;
    private final static int DOWNLOADING = 2;

    private RemoteViews contentView;
    private String fileName;

    //下载进度
    private int curProgress;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            return super.onStartCommand(intent, flags, startId);
        }

        fileName = getResources().getString(R.string.app_name) + ".apk";
        contentView = new RemoteViews(getPackageName(), R.layout.layout_update_progress);
        contentView.setTextViewText(R.id.update_version_title,fileName);

        // 获取传值
        String downloadDir = intent.getStringExtra("downloadDir");
        String version = intent.getStringExtra("version");
        apkUrl = intent.getStringExtra("apkUrl");

        this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        // 设置通知栏显示内容
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContent(contentView);

        // 发出通知
        updateNotificationManager.notify(0, mBuilder.build());

        //下载apk
        downLoadApk(downloadDir,fileName);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 下载apk安装包
     *
     * @param downloadDir       下载存放的文件夹
     * @param fileName           文件名
     */
    private  void downLoadApk(String downloadDir,String fileName){
        if(TextUtils.isEmpty(downloadDir)){
            return;
        }

        Netroid mNetroid = Netroid.getNetroid(this);
        boolean isExist = FileUtil.isFolderExists(downloadDir);

        if(isExist) {
            String filePath = downloadDir + "/" + fileName;
            saveFile = new File(filePath);
            if (saveFile.exists()) {
                saveFile.delete();
            }

            //文件下载
            mNetroid.addFileDownload(filePath, apkUrl, new Listener<Void>() {
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
     * 下载过程中的事件处理的方法
     */
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_COMPLETE://当下载完毕，自动安装APK
                    //关闭通知栏
                    updateNotificationManager.cancel(0);

                    //安装apk
                    ApkUtils.installApk(VersionUpdateService.this, saveFile.getAbsolutePath());

                    break;
                case DOWNLOAD_FAIL:// 下载失败
                    //点击通知栏事件
                    mBuilder.setAutoCancel(true);
                    mBuilder.setContentIntent(PendingIntent.getActivity(VersionUpdateService.this, 0, new Intent(), 0));
                    contentView.setTextViewText(R.id.update_version_title,getResources().getString(R.string.app_name)+getResources().getString(R.string.update_version_download_error));
                    mBuilder.setContent(contentView);
                    updateNotificationManager.notify(0, mBuilder.build());

                    break;
                case DOWNLOADING://下载进行中

                    contentView.setTextViewText(R.id.update_progressBar_text, msg.obj+"%");
                    contentView.setProgressBar(R.id.update_detail_progressBar, 100, (int)msg.obj, false);

                    mBuilder.setContent(contentView);
                    updateNotificationManager.notify(0, mBuilder.build());
                    break;
                default:
                    break;
            }
        }
    };

}
