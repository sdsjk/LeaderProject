package com.seaboxdata.portal.module.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.SoftKeyBoardListener;

public class InfoDetailsActivity extends CommonActivity implements View.OnClickListener {
    private ImageView infomationBack;
    private TextView infoTitleCommon;
    private ImageView infoBottomWrite;
    private TextView infoBottomTv;
    private ImageView infoBottomEmoji;
    private ImageView infoBottomCollection;
    private WebView infoDetailsContent;
    private  WebSettings settings;
    private View info_bottom_rl;
    private CommentDialog commentDialog;
    private ImageView info_bottom_delete;

    private int mIndex=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_details);
        Intent intent = getIntent();
        mIndex=intent.getIntExtra("mIndexPage",1);
        initView();
        initWebView();
        initListener();
    }

    private void initListener() {
        SoftKeyBoardListener.setListener(InfoDetailsActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                if(commentDialog!=null){
                    commentDialog.dismiss();
                }

            }
        });
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
        info_bottom_rl=findViewById(R.id.info_bottom_rl);
        info_bottom_delete= (ImageView) findViewById(R.id.info_bottom_delete);
        if(mIndex==1){
            info_bottom_delete.setVisibility(View.VISIBLE);
        }else {
            info_bottom_delete.setVisibility(View.GONE);
        }
        info_bottom_delete.setOnClickListener(this);
        infoTitleCommon.setVisibility(View.GONE);
        infomationBack.setOnClickListener(this);
        infoBottomWrite.setOnClickListener(this);
        infoBottomEmoji.setOnClickListener(this);
        infoBottomTv.setOnClickListener(this);


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
            case R.id.info_bottom_write:
                showCommentDialog();
                break;
            case R.id.info_bottom_tv:
                showCommentDialog();
                break;
            case R.id.info_bottom_emoji:
                showCommentDialog();
                break;
            case R.id.info_bottom_collection:

                break;
            case R.id.info_bottom_delete:
                Toast.makeText(this,"delete",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void showCommentDialog() {
        commentDialog= new CommentDialog("优质评论将会被优先展示", sendListener,this);
        commentDialog.show(getSupportFragmentManager(), "comment");
    }
    CommentDialog.SendListener sendListener=new CommentDialog.SendListener() {
        @Override
        public void sendComment(String inputText) {
            Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_SHORT).show();
        }
    };

}
