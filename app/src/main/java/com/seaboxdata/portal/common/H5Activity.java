package com.seaboxdata.portal.common;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.linewell.core.html.H5ActivityCommon;
import com.linewell.utils.SystemUtils;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.utils.LinkUtils;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

/**
 * Created by Administrator on 2017-9-19.
 */

public class H5Activity extends H5ActivityCommon {

    private boolean mStatusBarTransparent = false;

    @Override
    public Uri handlePhotoResult(Intent data) {
        return null;
    }

    @Override
    public void openPictureChoose(int requestCode) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);


            LinearLayout toolbar = (LinearLayout) findViewById(R.id.toolbar_ll);

            if (null != toolbar) {
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                if (StatusBarUtil.StatusBarLightMode(this) > 0) {
                    tintManager.setStatusBarTintColor(Color.TRANSPARENT);
                    mStatusBarTransparent = true;
                } else {
                    tintManager.setStatusBarTintColor(Color.BLACK);
                    mStatusBarTransparent = false;
                }

                tintManager.setStatusBarTintEnabled(true);
                toolbar.getLayoutParams().height = SystemUtils.dip2px(this, 44) + tintManager.getConfig().getStatusBarHeight();
                toolbar.setPadding(toolbar.getPaddingLeft(),
                        tintManager.getConfig().getStatusBarHeight(),
                        toolbar.getPaddingRight(),
                        toolbar.getPaddingBottom());

            }


        }

        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mBackFIT.setTextColor(getResources().getColor(R.color.colorWhite));

    }

    @Override
    public void openLoginActivity(int requestCode) {
//        LoginActivity.startAction(H5Activity.this, requestCode);
    }

    @Override
    public void openActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        try{
            JsonObject bannerObj = LinkUtils.urlToJsonObject(uri.toString());
            Intent intent = LinkUtils.getInnerLinkIntent(this, bannerObj);
            if(intent!=null){
                startActivityForResult(intent, 1);
            }else{
                super.openActivity(uri);
            }
        }catch (Exception e){

        }
    }

    /**
     * 参数规则 1： 链接 2：标题 3：内容 4：图片
     *
     * @param data
     */
    @Override
    public void shareNative(String... data) {
        String title = "";
        String content = "";
        String pic = "";
        String url = "";
        if (data != null) {
            int length = data.length;
            if (length > 0) {
                url = data[0];
            }
            if (length > 1) {
                title = data[1];
            }
            if (length > 2) {
                content = data[2];
            }
            if (length > 3) {
                pic = data[3];
            }
        } else {
            return;
        }
//        if (mShareDialog == null) {
//            mShareDialog = new ShareDialog(this, title, content, pic, url);
//        }
//        mShareDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
//        if(mShareDialog!=null){
//            mShareDialog.release();
//        }
        super.onDestroy();

        clearWebViewCache();
    }

    private void clearWebViewCache() {

//        webView.clearHistory();
//        webView.clearFormData();

        webView.clearCache(true);

        // 清除cookie即可彻底清除缓存
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
    }

    protected void addJavascriptInterface(WebView webView) {
//        super.addJavascriptInterface(webView);
        webView.addJavascriptInterface(new JavaScriptinterfaceNew(this), "android");
    }

    private class JavaScriptinterfaceNew  extends JavaScriptinterface {

        private H5ActivityCommon mActivity;

        public JavaScriptinterfaceNew(H5ActivityCommon activity) {
            super(activity);
            mActivity = activity;
        }
        /****************新版本要求的方法*************/

        @JavascriptInterface
        public boolean isStatusBarTransparent(){
            return mStatusBarTransparent;
        }
    }


    @Override
    protected String initLinkUrl() {
        return "";
    }
}
