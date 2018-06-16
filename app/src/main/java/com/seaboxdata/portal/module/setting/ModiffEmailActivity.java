package com.seaboxdata.portal.module.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ModiffEmailActivity extends CommonActivity {

    private Unbinder unbinder;
    //    标题
    @BindView(R.id.info_title_common)
    TextView infoTitleCommon;

    @BindView(R.id.verification_phone)
    View verification_phone;
    @BindView(R.id.setting_verifition_safe_view)
    LinearLayout setting_verifition_safe_view;
    @BindView(R.id.verification_view)
    LinearLayout verification_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modiff_email);

        setToolBar();
        unbinder = ButterKnife.bind(this);
        infoTitleCommon.setText("安全校验");

    }

    //手机验证码验证
    @OnClick(R.id.verification_phone)
    public void phoneVerification() {

    }

    //邮箱验证码验证
    @OnClick(R.id.verification_email)
    public void emailVerification() {
        startActivity(new Intent(this, EmailSettingActivity.class));
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
}
