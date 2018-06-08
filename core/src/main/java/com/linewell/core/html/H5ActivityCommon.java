package com.linewell.core.html;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linewell.utils.ApkUtils;
import com.linewell.utils.GsonUtil;
import com.linewell.utils.StringUtil;
import com.linewell.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 直接访问html页面
 *
 * @author zjianning@linewell.com
 * @since 2016/12/14
 */
public abstract class H5ActivityCommon extends CommonHtmlActivity {

    /**
     * js对象访问android的对象
     */
    private static final String ANDROID_JS_OBJECT = "android";

    private static final int LOGIN_CALL_BACK_CODE = 200;

    /**
     * 内容的长度
     */
    private static final int CONTENT_LENGTH = 25;

    private Context mContext = this;

    private Activity mActivity;
    //分享图片
    private String img = "";
    //标题全局属性
    private String mTitle = "";


    //保存图片成功
    public static int SAVE_PIC_SUCCESS = 1;
    //无交互分享
    public static int SHARE_NO_REMOTE = 2;
    //设置TITLE显隐
    public static int TITLE_BAR_SHOW = 3;
    public static int TITLE_BAR_HIDE = 4;
    //设置title
    public static int SET_TITLE = 5;
    //显示dialog
    public static int SHOW_DIALOG = 6;
    //获取更新
    public static int GET_UPDATE = 7;
    //打开登录
    public static int OPEN_LOGIN = 8;
    //显示右上角分享按钮
    public static int SHOW_SHARE = 9;
    //隐藏右上角分享按钮
    public static int HIDE_SHARE = 10;
    //返回
    public static int BACK = 11;

    //申请读sd卡权限code
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1000;

    //请求的URL
    private String serviceUrl = "";


    //是否已经初始化过分享
    private boolean mShareInit = false;
    //是否远程获取数据
    private boolean mIsRemote = false;
    //登录后跳转的页面
    private String mActivityJson;

    /**
     * 初始化线程
     */
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setEnableToolbar(false);
        super.onCreate(savedInstanceState);
        mActivity = H5ActivityCommon.this;
        bindView();
    }

    private void bindView() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    protected void addJavascriptInterface(WebView webView) {
        webView.addJavascriptInterface(new JavaScriptinterface(this), ANDROID_JS_OBJECT);
    }


    public class JavaScriptinterface {

        private H5ActivityCommon mActivity;

        public JavaScriptinterface(H5ActivityCommon activity) {
            mActivity = activity;
        }
        /****************新版本要求的方法*************/
        /**
         * 打开内部应用
         */
        @JavascriptInterface
        public void openWindow(final String url) {
            loadUrl(url);
        }

        /**
         * 获取用户id
         */
        @JavascriptInterface
        public String getUserId() {
            return getLoginUserId();
        }

        /**
         * 获取用户token
         */
        @JavascriptInterface
        public String getToken() {
            return getLoginToken();
        }

        /**
         * 获取设备id
         *
         * @return
         */
        @JavascriptInterface
        public String getDeviceId() {
            return getMobileDeviceId();
        }

        /**
         * 设置Title
         *
         * @return
         */
        @JavascriptInterface
        public void setTitle(String title) {
            mTitle = title;
            mHandler.sendEmptyMessage(SET_TITLE);
        }

        /**
         * 设置右上角分享按钮隐藏
         */
        @JavascriptInterface
        public void hideShare() {
            mHandler.sendEmptyMessage(HIDE_SHARE);
        }

        /**
         * 设置toolbar隐藏
         */
        @JavascriptInterface
        public void hideHeader() {
            //隐藏
            mHandler.sendEmptyMessage(TITLE_BAR_HIDE);
        }

        /**
         * 普通的分享
         *
         * @param jsonData js调用方式  android.share(jsonData)
         */
        @JavascriptInterface
        public void shareInfo(String jsonData) {
            //处理js传入的Json数据
            List<String> dataList = new ArrayList<>();
            try {
                JsonObject jsonObject = GsonUtil.getJsonObject(jsonData);
                if (jsonObject == null) {
                    return;
                }
                //分享数组按照 1： 链接 2：标题 3：内容 4：图片的顺序
                //分享链接
                if (jsonObject.has("link")) {
                    dataList.add(jsonObject.get("link").getAsString());
                } else {
                    dataList.add("");
                }
                //分享标题
                if (jsonObject.has("title")) {
                    dataList.add(jsonObject.get("title").getAsString());
                } else {
                    dataList.add("");
                }
                //分享内容
                if (jsonObject.has("desc")) {
                    dataList.add(jsonObject.get("desc").getAsString());
                } else {
                    dataList.add("   ");
                }
                //分享图片
                if (jsonObject.has("imgUrl")) {
                    dataList.add(jsonObject.get("imgUrl").getAsString());
                } else {
                    dataList.add("");
                }
            } catch (Exception e) {
                mHandler.sendEmptyMessage(HIDE_SHARE);
            }
            mHandler.sendEmptyMessage(SHOW_SHARE);
            if (mShareData == null || mShareData.length < 4) {
                mShareData = new String[4];
            }
            dataList.toArray(mShareData);
        }

        /**
         * 页面回退按钮
         */
        @JavascriptInterface
        public void back() {
            mHandler.sendEmptyMessage(BACK);
        }

        /**
         * 页面回退按钮
         */
        @JavascriptInterface
        public void close() {
            mActivity.finish();
        }

        /**
         * 打开登录
         */
        @JavascriptInterface
        public void openLogin(final String url) {
            if (TextUtils.isEmpty(url)) {
                lastUri = null;
            } else {
                lastUri = Uri.parse(url);
            }
            mHandler.sendEmptyMessage(OPEN_LOGIN);
        }
        /****************新版本要求的方法end*************/

        /**
         * 普通的分享
         *
         * @param shareType js调用方式  android.share(shareType)
         */
        @JavascriptInterface
        public void share(String shareType) {
            shareNative(shareType);
        }


        /**
         * 获取当前参数
         */
        @JavascriptInterface
        public String getType() {
            return "ANDROID";
        }

        /**
         * 设置分享请求服务
         *
         * @param url
         */
        @JavascriptInterface
        public void setServiceUrl(String url) {
            serviceUrl = url;
        }

        /**
         * 获取分享请求服务
         *
         * @return
         */
        @JavascriptInterface
        public String getServiceUrl() {
            return serviceUrl;
        }

        /**
         * 设置Title
         *
         * @return
         */
        @JavascriptInterface
        public void setCenterTitle(String title) {
            mTitle = title;
            mHandler.sendEmptyMessage(SET_TITLE);
        }

        /**
         * 设置title的显隐
         *
         * @param isShow
         */
        @JavascriptInterface
        public void setEnableToolBar(boolean isShow) {
            if (!isShow) {
                //隐藏
                mHandler.sendEmptyMessage(TITLE_BAR_HIDE);
            } else {
                //显示
                mHandler.sendEmptyMessage(TITLE_BAR_SHOW);
            }

        }

        /**
         * 设置右上角分享按钮的显隐
         *
         * @param isShow
         */
        @JavascriptInterface
        public void setEnableShare(boolean isShow) {

            if (!isShow) {
                //隐藏
                mHandler.sendEmptyMessage(HIDE_SHARE);
            } else {
                //显示
                mHandler.sendEmptyMessage(SHOW_SHARE);
            }

        }

        /**
         * 获取登录状态
         */
        @JavascriptInterface
        public boolean checkLogin() {
            return isLogin();
        }


        /**
         * 是否分享初始化
         *
         * @param flag
         */
        @JavascriptInterface
        public void setShareInit(boolean flag) {
            mShareInit = flag;
        }

        /**
         * 是否分享初始化
         */
        @JavascriptInterface
        public void getUpdate() {
            mHandler.sendEmptyMessage(GET_UPDATE);
        }

        /**
         * 获取版本名
         */
        @JavascriptInterface
        public String getVerName() {
            return ApkUtils.getVerName(mContext, mContext.getPackageName());
        }

        /**
         * 获取版本号
         */
        @JavascriptInterface
        public int getVerCode() {
            return ApkUtils.getVerCode(mContext, mContext.getPackageName());
        }

        /**
         * 打开页面判断登录
         */
        @JavascriptInterface
        public void starActivityWithLogin(String jsonResult) {
            mActivityJson = jsonResult;
            mHandler.sendEmptyMessage(OPEN_LOGIN);
        }


        /**
         * 打开内部应用
         */
        @JavascriptInterface
        public void testCallback(String data, String fuction) {
            data += "+android";

            final String url = "javascript:" + fuction + "('" + data + "');";
            loadUrl(url);
        }

        /**
         * 是否可以打开图片选择器
         */
        @JavascriptInterface
        public boolean isOpenSelectPhoto() {
            return true;
        }

        /**
         * 是否可以打开图片选择器
         */
        @JavascriptInterface
        public boolean showDialog(String title, String message, final JsResult jsResult) {

            final String[] content = new String[]{title, message};
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mActivity.showDialog(content, jsResult);
                }
            });
            return true;
        }

    }


    /**
     * 不用这东西会报多线程错误
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //activityId
            if (msg.what == SAVE_PIC_SUCCESS) {
                ToastUtils.show(H5ActivityCommon.this, "保存成功");
            } else if (msg.what == SHARE_NO_REMOTE) {
                //无交互分享
            } else if (msg.what == TITLE_BAR_SHOW) {
                mToolBarlLL.setVisibility(View.VISIBLE);
            } else if (msg.what == TITLE_BAR_HIDE) {
                mToolBarlLL.setVisibility(View.GONE);
            } else if (msg.what == SET_TITLE) {
                mTitleTV.setText(mTitle);
            } else if (msg.what == SHOW_DIALOG) {
            } else if (msg.what == GET_UPDATE) {
            } else if (msg.what == OPEN_LOGIN) {
                openLoginActivity(REQUEST_LOGIN);
            } else if (msg.what == SHOW_SHARE) {
                mShareBtn.setVisibility(View.VISIBLE);
            } else if (msg.what == HIDE_SHARE) {
                mShareBtn.setVisibility(View.INVISIBLE);
            } else if (msg.what == BACK) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    mActivity.finish();
                }
            }


            return false;
        }
    });

    /**
     * 打开内部APP页面
     *
     * @param jsonResult
     */
    private void openActivity(String jsonResult) {
        if (!TextUtils.isEmpty(jsonResult)) {
            JsonObject params = GsonUtil.getJsonObject(jsonResult);
            //分享类型
            String pageId = params.get("pageId").getAsString();

            if (StringUtil.isEmpty(pageId)) {
                return;
            }

            //公用ID
            String unid = "";
            String pid = "";
            String pname = "";
            String keyid = "";
            //公用参数
            if (params.get("unid") != null) {
                unid = params.get("unid").getAsString();
            }
            if (params.get("pid") != null) {
                pid = params.get("pid").getAsString();
            }
            if (params.get("pname") != null) {
                pname = params.get("pname").getAsString();
            }
            //实例一个class
            Class<?> activityClass = null;
            try {
                activityClass = Class.forName(pageId);
                Intent intent = new Intent(mContext, activityClass);
                /**
                 * 遍历获取键值对
                 */
                Set<Map.Entry<String, JsonElement>> entries = params.entrySet();
                Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, JsonElement> next = iterator.next();
                    String key = next.getKey();
                    if (!TextUtils.isEmpty(key) && !"pageId".equals(key)) {
                        //判断传值是否是boolean型
                        if (params.getAsJsonPrimitive(key) != null && params.getAsJsonPrimitive(key).isBoolean()) {
                            boolean booleanValue = params.get(key).getAsBoolean();
                            intent.putExtra(key, booleanValue);
                        } else if (params.getAsJsonPrimitive(key) != null && params.getAsJsonPrimitive(key).isNumber()) {
                            //判断传值是否是int型
                            int numValue = params.get(key).getAsInt();
                            intent.putExtra(key, numValue);
                        } else {
                            String value = params.get(key).getAsString();
                            intent.putExtra(key, value);
                        }

                    }
                }
                //跳转页面
                startActivityForResult(intent, 0);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //复制到粘贴板
    private void copyBoard(String url) {
        //复制到粘贴板
        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(url);
        ToastUtils.show(H5ActivityCommon.this, "复制成功");
    }


    /**
     * 只能通过线程来远程链接并保存图片
     */
    private void initThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveToLocal(img);
            }
        });
    }

    /**
     * 保存到本地
     *
     * @param pic
     */
    private void saveToLocal(String pic) {
        HttpURLConnection conn = null;
        try {

            String fileName = "qc_" + pic.substring(pic.lastIndexOf("/") + 1);
            final String filePath = getFileRoot(this) + File.separator
                    + fileName;

            URL mURL = new URL(pic);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET"); //设置请求方法
            conn.setConnectTimeout(10000); //设置连接服务器超时时间
            conn.setReadTimeout(5000);  //设置读取数据超时时间

            conn.connect(); //开始连接

            int responseCode = conn.getResponseCode(); //得到服务器的响应码
            if (responseCode == 200) {
                //访问成功
                InputStream is = conn.getInputStream(); //获得服务器返回的流数据
                Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象

                //存储
                FileOutputStream outStream = null;
                File file = new File(filePath);
                if (file.isDirectory()) {//如果是目录不允许保存
                    return;
                }
                try {
                    outStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();

                    // 其次把文件插入到系统图库
                    MediaStore.Images.Media.insertImage(this.getContentResolver(),
                            bitmap, fileName, null);
                    // 最后通知图库更新
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    //发送保存成功
                    mHandler.sendEmptyMessage(SAVE_PIC_SUCCESS);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bitmap.recycle();
                        if (outStream != null) {
                            outStream.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                //访问失败
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect(); //断开连接
            }
        }
    }


    /**
     * 文件存储根目录
     *
     * @param context
     * @return
     */
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }


    @Override
    protected void onDestroy() {
        //防止内存泄漏
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void showDialog(String[] message, JsResult jsResult) {
        try {
            AlertDialog.Builder b2 = new AlertDialog.Builder(mContext)
                    .setTitle(message[0]).setMessage(message[1])
                    .setPositiveButton("确定",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // MyWebView.this.finish();
                                }
                            });

            b2.setCancelable(false);
            b2.create();
            b2.show();
        } catch (Exception e) {
        }

    }

}
