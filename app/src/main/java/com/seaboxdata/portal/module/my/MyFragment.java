package com.seaboxdata.portal.module.my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linewell.core.view.FontIconText;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.category.CategorySubActivity;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.module.history.HistoryBrowsing;
import com.seaboxdata.portal.module.monitoring.BusinessMonitoringActivity;
import com.seaboxdata.portal.module.my.message.MyMeaasgeListActivity;
import com.seaboxdata.portal.module.setting.MySettingActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zjianning on 2018/5/11.
 */

public class MyFragment extends CommonFragment {

    private View view;

    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my, container, false);

        setToolbar();
        unbinder = ButterKnife.bind(this, view);

        FontIconText fontIconText = (FontIconText) view.findViewById(R.id.backBtn);
        fontIconText.setVisibility(View.GONE);
        TextView textView = (TextView) view.findViewById(R.id.centerTitle);
        textView.setText("我");

        return view;
    }

    private void setToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) view.findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            linear_bar.getLayoutParams().height = getStatusBarHeight(mActivity);

            if (StatusBarUtil.StatusBarLightMode(mActivity) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }
    }
//    历史浏览
    @OnClick(R.id.my_read_hostory)
    public void openHistory() {
        mActivity.startActivity(new Intent(mActivity, HistoryBrowsing.class));
    }


    //    业务检测指标
    @OnClick(R.id.my_monitoring)
    public void openMOnitoring() {
        BusinessMonitoringActivity.startAction(mActivity,
                mActivity.getString(R.string.activity_business_monitoring_title));
    }

    //    首页应用设置
    @OnClick(R.id.category_sub)
    public void categroySub() {
        CategorySubActivity.startAction(mActivity, true);
    }

    //消息
    @OnClick(R.id.my_meessage)
    public void openMeassage() {
        mActivity.startActivity(new Intent(mActivity, MyMeaasgeListActivity.class));
    }

    //    设置
    @OnClick(R.id.my_app_setting)
    public void openSetting() {
        mActivity.startActivity(new Intent(mActivity, MySettingActivity.class));
    }
}
