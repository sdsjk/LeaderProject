package com.linewell.core.html;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linewell.core.R;
import com.linewell.core.activity.PortraitActivity;
import com.linewell.core.utils.AppSessionUtils;
import com.linewell.core.view.FontIconText;
import com.linewell.utils.StringUtil;
import com.linewell.utils.SystemUtils;

import java.util.HashMap;
import java.util.Set;

/**
 * webView加载http页面
 *
 * @author slong
 */
public abstract class CommonHtmlActivity extends PortraitActivity {
    public static final int REQUEST_LOGIN = 12220;

    /**
     * url的key
     */
    public static final String KEY_LINK_URL = "linkUrl";

    /**
     * 标题的key
     */
    public static final String KEY_LINK_TITLE = "KEY_LINK_TITLE";

    /**
     * noticeKEy
     */
    public static final String KEY_NOTICE_ID = "KEY_NOTICE_ID";

    //分享的key
    public static final String KEY_SHARE = "KEY_SHARE";
    //分享内容
    public static final String KEY_SHARE_DATA = "KEY_SHARE_DATA";
    //分享数据
    protected String[] mShareData;

    //隐藏头部KEY
    public static final String KEY_IFS = "KEY_IFS";

    //token的key
    public static final String KEY_TOKEN = "token";
    //DeviceId的key
    public static final String KEY_DEVICE_ID = "deviceId";

    /**
     * 选择图片的requestCode
     */
    public static final int REQUEST_CODE_CHOOSE_PICTURE = 10001;

    private ProgressBar progressbar;

    //http链接地址
    private String mLinkUrl;

    /**
     * 标题
     */
    private String mTitle;

    private Context mContext = this;
    /**
     * JS是否隐藏了title
     */
    private static boolean isHideTitle = false;
    //webView
    protected WebView webView;
    //加载条
    protected ProgressBar bar;
    //title栏
    protected LinearLayout mToolBarlLL;
    //title
    protected TextView mTitleTV;
    //回退
    protected FontIconText mBackFIT;
    //关闭
    protected ImageView mCloseBtn;
    //分享
    protected ImageView mShareBtn;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_html);
        isHideTitle = false;
        Intent intent = getIntent();
        //链接地址
        mLinkUrl = intent.getStringExtra(KEY_LINK_URL);
        //title
        mTitle = intent.getStringExtra(KEY_LINK_TITLE);
        //分享信息
        mShareData = intent.getStringArrayExtra(KEY_SHARE_DATA);

        initView();
    }

    @Override
    public boolean onNavigationBack() {
        setResult(RESULT_OK);
        return super.onNavigationBack();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    /**
     * JS是否隐藏了title
     */
    public static void isHideTitle(boolean flag) {
        isHideTitle = flag;
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mTitleTV = (TextView) findViewById(R.id.title_tv);
        mBackFIT = (FontIconText) findViewById(R.id.back_fit);
        mBackFIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return;
                }
                finish();
            }
        });
        mShareBtn = (ImageView) findViewById(R.id.share_btn);
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareNative(mShareData);
            }
        });
        mCloseBtn = (ImageView) findViewById(R.id.close_btn);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView = (WebView) findViewById(R.id.html_webview);
        bar = (ProgressBar) findViewById(R.id.myProgressBar);
        mToolBarlLL = (LinearLayout) findViewById(R.id.toolbar_ll);
        mTitleTV.setText("");
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleTV.setText(mTitle);
        }

        //传值
        mIntent = getIntent();
        if (mIntent != null) {
            if ("true".equals(mIntent.getStringExtra(CommonHtmlActivity.KEY_IFS))) {
                mToolBarlLL.setVisibility(View.GONE);
            } else {
                mToolBarlLL.setVisibility(View.VISIBLE);
            }
            if ("true".equals(mIntent.getStringExtra(CommonHtmlActivity.KEY_SHARE))) {
                mShareBtn.setVisibility(View.VISIBLE);
            } else {
                mShareBtn.setVisibility(View.INVISIBLE);
            }
        }
        //安卓内部自己带了分享数据
        if (mShareData != null && mShareData.length > 0) {
            mShareBtn.setVisibility(View.VISIBLE);
        } else {
            mShareBtn.setVisibility(View.INVISIBLE);
        }

        WebSettings s = webView.getSettings();
        s.setSupportZoom(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        //允许执行JS
        s.setJavaScriptEnabled(true);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setGeolocationEnabled(true);
        s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        s.setDomStorageEnabled(true);
        webView.requestFocus();
        //加载html地址
        loadWeb();
    }

    private ValueCallback<Uri> mFilePathCallback4;
    private ValueCallback<Uri[]> mFilePathCallback5;

    /**
     * 加载html地址
     */
    public void loadWeb() {

        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (!openUrl(url)) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (view.canGoBack()) {
                    mCloseBtn.setVisibility(View.VISIBLE);
                } else {
                    mCloseBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        //设置进度条功能
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android  < 3.0
            public void openFileChooser(ValueCallback<Uri> filePathCallback) {
                mFilePathCallback4 = filePathCallback;
                openPictureChoose(REQUEST_CODE_CHOOSE_PICTURE);
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback filePathCallback, String acceptType) {
                mFilePathCallback4 = filePathCallback;
                openPictureChoose(REQUEST_CODE_CHOOSE_PICTURE);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
                mFilePathCallback4 = filePathCallback;
                openPictureChoose(REQUEST_CODE_CHOOSE_PICTURE);
            }

            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mFilePathCallback5 = filePathCallback;
                openPictureChoose(REQUEST_CODE_CHOOSE_PICTURE);
                return true;
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog.Builder(mContext)
                        .setTitle("打印").setMessage(message)
                        .setPositiveButton("ok",
                                new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                        // MyWebView.this.finish();
                                    }
                                });

                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //优先显示H5标题
                if(!StringUtil.isEmpty(title)){
                    //H5标题不为空, 就显示H5标题
                    mTitle = title;
                    mTitleTV.setText(title);
                }

//                String androidTitle = mTitleTV.getText().toString();
//                if (TextUtils.isEmpty(androidTitle) && TextUtils.isEmpty(mTitle)) {
//                    mTitle = title;
//                    mTitleTV.setText(title);
//                }
                initShareData();
            }
        });

        addJavascriptInterface(webView);

        initLinkUrl();

        webView.loadUrl(mLinkUrl);
//        webView.loadUrl(" file:///android_asset/test.html ");
    }

    /**
     * 初始化分享信息
     */
    protected void initShareData() {
        //显示了分享 没有分享信息的情况
        if (mShareBtn.getVisibility() == View.VISIBLE && (mShareData == null || mShareData.length <= 0)) {
            mShareData = new String[]{mTitle, "  ", mLinkUrl, ""};
        }
    }

    /**
     * 初始化url
     */
    protected String initLinkUrl() {
        boolean hasParams = false;
        if (mLinkUrl.indexOf("?") != -1) {
            hasParams = true;
        }
        String deviceId = getMobileDeviceId();
        if (!TextUtils.isEmpty(deviceId)) {
            mLinkUrl += hasParams ? "&" : "?";
            mLinkUrl += KEY_DEVICE_ID + "=" + deviceId;
        }
        String token = getLoginToken();
        if (!TextUtils.isEmpty(token)) {
            mLinkUrl += "&" + KEY_TOKEN + "=" + token;
        }
        return mLinkUrl;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE_PICTURE) {
            Uri uri = Uri.EMPTY;
            if (resultCode == RESULT_OK) {
                uri = handlePhotoResult(data);
            }
            if (mFilePathCallback4 != null) {
                mFilePathCallback4.onReceiveValue(uri);
            }
            if (mFilePathCallback5 != null) {
                mFilePathCallback5.onReceiveValue(new Uri[]{uri});
            }
            mFilePathCallback4 = null;
            mFilePathCallback5 = null;
        } else if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                reloadWeb();
                if (lastUri == null) {
                    return;
                }
                openActivity(lastUri);
            }
        }
    }

    public abstract Uri handlePhotoResult(Intent data);

    public abstract void shareNative(String... data);

    public void reloadWeb() {
        if (webView != null) {
            webView.loadUrl(mLinkUrl);
        }

    }

    protected void addJavascriptInterface(WebView webView) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        ViewParent parent = webView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(webView);
        }
        webView.stopLoading();
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearHistory();
        webView.clearView();
        webView.removeAllViews();

        super.onDestroy();
        webView.destroy();
    }

    public void loadUrl(final String url) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView != null) {
                    if (!openUrl(url)) {
                        webView.loadUrl(url);
                    }
                }
            }
        });
    }

    public WebView getWebView() {
        return webView;
    }

    protected Uri lastUri;

    public boolean openUrl(String url) {
        // 步骤2：根据协议的参数，判断是否是所需要的url
        // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
        //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
        Uri uri = Uri.parse(url);
        // 如果url的协议 = 预先约定的 js 协议
        // 就解析往下解析参数
        String scheme = uri.getScheme().toLowerCase();
        if (scheme.equals("native")) {
            // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
            // 所以拦截url,下面JS开始调用Android需要的方法
            if (uri.getAuthority().equals("openwindow")) {

                //  步骤3：
                // 执行JS所需要调用的逻辑
                // 可以在协议上带有参数并传递到Android上
                openActivity(uri);
            }

            return true;
        } else if (scheme.equals("http") || scheme.equals("https")) {
            //外链
            webView.loadUrl(url);
        }
        return false;
    }

    public void openActivity(Uri uri) {
        if (uri == null) {
            return;
        }

        boolean needLogin = uri.getBooleanQueryParameter("nl", false);
        if (needLogin && !isLogin()) {
            lastUri = uri;
            openLoginActivity(REQUEST_LOGIN);
            return;
        }
        lastUri = null;

        //分享类型
        String pageId = uri.getQueryParameter("pageId");

        if (StringUtil.isEmpty(pageId)) {
            return;
        }


        //公用ID
        String unid = "";
        String pid = "";
        String pname = "";
        String keyid = "";
        //公用参数
        if (uri.getQueryParameter("unid") != null) {
            unid = uri.getQueryParameter("unid");
        }
        if (uri.getQueryParameter("pid") != null) {
            pid = uri.getQueryParameter("pid");
        }
        if (uri.getQueryParameter("pname") != null) {
            pname = uri.getQueryParameter("pname");
        }
        //实例一个class
        Class<?> activityClass = null;
        try {
            activityClass = Class.forName(pageId);
            Intent intent = new Intent(mContext, activityClass);
            /**
             * 遍历获取键值对
             */
            HashMap<String, Object> params = new HashMap<>();
            Set<String> collection = uri.getQueryParameterNames();
            if (collection == null) {
                return;
            }
            for (String name : collection) {
                String value = uri.getQueryParameter(name);
                if (value.equals("true") || value.equals("false")) {
                    intent.putExtra(name, Boolean.valueOf(value));
                } else {
                    intent.putExtra(name, value);
                }
            }
            //跳转页面
            startActivityForResult(intent, 0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean isLogin() {
        boolean mIsLogin = false;
        //验证是否登录
        if (AppSessionUtils.getInstance() != null) {
            mIsLogin = AppSessionUtils.getInstance().isLogin(mContext);
        } else {
            mIsLogin = false;

        }
        return mIsLogin;
    }

    /**
     * 打开图片选择器
     */
    public abstract void openPictureChoose(int requestCode);

    public abstract void openLoginActivity(int requestCode);

    /**
     * 获取设备id
     *
     * @return
     */
    public String getMobileDeviceId() {
        return SystemUtils.getImei(mContext);
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public String getLoginUserId() {
        String userId = "";
        //验证是否登录
        if (AppSessionUtils.getInstance() != null) {
            if (AppSessionUtils.getInstance().getLoginInfo(mContext) != null) {
                userId = AppSessionUtils.getInstance().getLoginInfo(mContext).getUserId();
            }
        }
        return userId;
    }

    /**
     * 获取用户token
     *
     * @return
     */
    public String getLoginToken() {
        String token = "";
        //验证是否登录
        if (AppSessionUtils.getInstance() != null) {
            if (AppSessionUtils.getInstance().getLoginInfo(mContext) != null) {
                token = AppSessionUtils.getInstance().getToken(mContext);
            }
        }
        return token;
    }


}
