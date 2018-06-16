package com.seaboxdata.portal.module.history;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.HashMap;
import java.util.Map;

public class HistoryBrowsing extends CommonActivity implements View.OnClickListener {

    private ImageView infomationBack;
    private TextView historyPush;
    private TextView historyBrowser;
    private FrameLayout historyFragment;
    Drawable drawableSelect,drawableUnSelect;
    private HistorybrowserFragment historybrowserFragment;
    private HistoryPushFragment historyPushFragment;
    private FragmentManager mFragmentManager;
    private Map<Fragment, Object> fragmentAddFlag = new HashMap<>();

    /**
     * 当前页面
     */
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_browsing);
        setToolBar();
        mFragmentManager = getSupportFragmentManager();
        initView();
        drawableSelect=getResources().getDrawable(R.drawable.history_selecter);
        drawableUnSelect=getResources().getDrawable(R.drawable.transparent);
    }

    private void setToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            linear_bar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            linear_bar.getLayoutParams().height = getStatusBarHeight();

            if (StatusBarUtil.StatusBarLightMode(this) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }
    }

    private void initView() {
        infomationBack = (ImageView) findViewById(R.id.infomation_back);
        historyPush = (TextView) findViewById(R.id.history_push);
        historyBrowser = (TextView) findViewById(R.id.history_browser);
        historyFragment = (FrameLayout) findViewById(R.id.history_fragment);

        infomationBack.setOnClickListener(this);
        historyBrowser.setOnClickListener(this);
        historyPush.setOnClickListener(this);

        showFragment(new HistoryPushFragment());
    }

    @Override
    public void onClick(View v) {
        if(v==infomationBack){
            finish();
        }
        if(v==historyBrowser){
            historyBrowser.setBackground(drawableSelect);
            historyBrowser.setTextColor(Color.parseColor("#ffffff"));
            historyPush.setBackground(drawableUnSelect);
            historyPush.setTextColor(Color.parseColor("#4a6def"));
            if(historybrowserFragment==null){
                historybrowserFragment=new HistorybrowserFragment();
            }
            showFragment(historybrowserFragment);
        }
        if(v==historyPush){
            historyPush.setBackground(drawableSelect);
            historyPush.setTextColor(Color.parseColor("#ffffff"));
            historyBrowser.setBackground(drawableUnSelect);
            historyBrowser.setTextColor(Color.parseColor("#4a6def"));
            if(historyPushFragment==null){
                historyPushFragment=new HistoryPushFragment();
            }
            showFragment(historyPushFragment);
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if(mCurrentFragment!=null){
            fragmentTransaction.hide(mCurrentFragment);
        }
        if(fragment!=historyPushFragment&&historyPushFragment!=null){
            fragmentTransaction.hide(historyPushFragment);
        }
        if(fragment!=historybrowserFragment&&historybrowserFragment!=null){
            fragmentTransaction.hide(historybrowserFragment);
        }
        if(fragment.isAdded()|| fragmentAddFlag.get(fragment) != null){
            fragmentTransaction.show(fragment);
        }else {
            fragmentTransaction.add(R.id.history_fragment,fragment);
            fragmentAddFlag.put(fragment, true);
        }
        mCurrentFragment = fragment;
        fragmentTransaction.commitAllowingStateLoss();

    }
}
