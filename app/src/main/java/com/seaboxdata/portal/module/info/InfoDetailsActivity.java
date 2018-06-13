package com.seaboxdata.portal.module.info;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;

public class InfoDetailsActivity extends CommonActivity implements View.OnClickListener {
    private ImageView infomationBack;
    private TextView infoTitleCommon;
    private ImageView infoBottomWrite;
    private TextView infoBottomTv;
    private ImageView infoBottomEmoji;
    private ImageView infoBottomCollection;
    private WebView infoDetailsContent;
    private  WebSettings settings;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_details);

        initView();
        initWebView();
    }

    private void initWebView() {
        settings = infoDetailsContent.getSettings();
        //设置页面是否使用js
        settings.setJavaScriptEnabled(true);

        infoDetailsContent.loadUrl("https://www.baidu.com");
        infoDetailsContent.setWebViewClient(new MyWebViewClient());

    }

    private void initView() {

        infomationBack = (ImageView) findViewById(R.id.infomation_back);
        infoTitleCommon = (TextView) findViewById(R.id.info_title_common);
        infoBottomWrite = (ImageView) findViewById(R.id.info_bottom_write);
        infoBottomTv = (TextView) findViewById(R.id.info_bottom_tv);
        infoBottomEmoji = (ImageView) findViewById(R.id.info_bottom_emoji);
        infoBottomCollection = (ImageView) findViewById(R.id.info_bottom_collection);
        infoDetailsContent = (WebView) findViewById(R.id.info_details_content);

        infoTitleCommon.setVisibility(View.GONE);
        infomationBack.setOnClickListener(this);


    }



    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.infomation_back:
                finish();
                break;
        }
    }
}
