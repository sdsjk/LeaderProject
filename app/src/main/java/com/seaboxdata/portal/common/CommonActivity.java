package com.seaboxdata.portal.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.linewell.core.activity.PortraitActivity;
import com.linewell.utils.SystemUtils;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.utils.CleanLeakedUtils;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 公共activity
 * @author lyixin
 * @since 2017/6/12.
 */
public class CommonActivity extends PortraitActivity {

    public static final String KEY_DATA = "KEY_DATA";

    public static final String KEY_ID = "id";

    protected Activity mCommonActivity;

    protected Context mCommonContext;

    protected Unbinder unbinder;

    Handler mHandler = new Handler();

//    protected ShareDialog mShareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setEnableToolbar(false);

        super.onCreate(savedInstanceState);
//        MobclickAgent.setCatchUncaughtExceptions(true);

//        PushUtils.init(getApplicationContext());

        mCommonActivity = this;
        mCommonContext = this;
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        initStatusBar();
        bindCommonView();
    }

    public void setContentView(int layoutResID, int toolbarResId) {
        setEnableToolbar(true);
        super.setContentView(layoutResID, toolbarResId);
        unbinder = ButterKnife.bind(this);
//        initStatusBar();
        bindCommonView();
    }

    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            if (null != toolbar) {
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                if (StatusBarUtil.StatusBarLightMode(mCommonActivity) > 0) {
                    tintManager.setStatusBarTintColor(Color.TRANSPARENT);
                } else {
//                    tintManager.setStatusBarTintColor(Color.BLACK);
                }

                tintManager.setStatusBarTintEnabled(true);
                toolbar.getLayoutParams().height = getAppBarHeight();
                toolbar.setPadding(toolbar.getPaddingLeft(),
                        tintManager.getConfig().getStatusBarHeight(),
                        toolbar.getPaddingRight(),
                        toolbar.getPaddingBottom());

            }
        }
    }

    private void bindCommonView() {

        View backView = findViewById(R.id.backBtn);
        if (null != backView) {
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBack();
                }
            });
        }

    }

    public void goBack() {
        finish();
    }

    @Override
    public void hideLoadingView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingViewSuper();
            }
        }, 500);
    }

    public void hideLoadingViewSuper(){
        super.hideLoadingView();
    }

    public void setCenterTitle(int stringResId) {
        TextView textView = (TextView) findViewById(R.id.centerTitle);
        if (null != textView) {
            textView.setText(stringResId);
        }
    }

    public void setCenterTitle(String stringTitle) {
        TextView textView = (TextView) findViewById(R.id.centerTitle);
        if (null != textView) {
            textView.setText(stringTitle);
        }
    }

    @Override
    public View initLoadingView() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View loadingView = inflater.inflate(R.layout.loading, null, false);
//        GifImageView gifImageView = (GifImageView) loadingView.findViewById(R.id.image_loading);
//        try {
//            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
//            // 将gif图资源转化为GifDrawable
//            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.img_loading);
//
//            // gif1加载一个动态图gif
//            gifImageView.setImageDrawable(gifDrawable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return loadingView;
        return null;
    }

    @Override
    public View initErrorView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view  = inflater.inflate(R.layout.layout_error_tip, null, false);
        ImageView image = (ImageView) view.findViewById(R.id.recycleview_view_error_tip_img);
        image.setImageResource(R.drawable.img_blank_network_error);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        return view;
    }

    public void refresh(){

    }

    public int getAppBarHeight() {
        return SystemUtils.dip2px(this, 44) + getStatusBarHeight();
    }

    public int getStatusBarHeight() {
        return new SystemBarTintManager(this).getConfig().getStatusBarHeight();
    }

    @Override
    protected void onDestroy() {
        if (null != unbinder) {
            unbinder.unbind();
        }
        resultListenerHashMap.clear();
        mHandler.removeCallbacksAndMessages(null);

        CleanLeakedUtils.fixHuaWeiMemoryLeak(this);

//        if(mShareDialog!=null){
//            mShareDialog.release();
//        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //是否有回调
        OnResultListener onResultListener =resultListenerHashMap.get(requestCode);
        if(onResultListener!=null){
            onResultListener.onResult(requestCode, resultCode, data);
        }
    }

    /**
     * 回调map，存放request和回调
     */
    private Map<Integer, OnResultListener> resultListenerHashMap = new HashMap<>();

    /**
     * 回调接口
     */
    public interface OnResultListener{
        void onResult(int requestCode, int resultCode, Intent data);
    }

    /**
     * 增加回调
     * @param requestCode
     * @param onResultListener
     */
    public void addOnResultListener(int requestCode, OnResultListener onResultListener){
        resultListenerHashMap.put(requestCode, onResultListener);
    }



    //展示进度框
    public ProgressDialog createProgressDialog(final Activity acitvity, String loadingMsg) {
        ProgressDialog mDialog = new ProgressDialog(acitvity) {
            @Override
            public void cancel() {
                if (acitvity != null && !acitvity.isFinishing()) {
                    super.cancel();
                }
            }
        };
        mDialog.setMessage(loadingMsg);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
        return mDialog;
    }


}
