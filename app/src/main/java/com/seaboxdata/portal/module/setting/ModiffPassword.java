package com.seaboxdata.portal.module.setting;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ModiffPassword extends CommonActivity {



    private Unbinder unbinder;
//    标题
    @BindView(R.id.info_title_common)
    TextView infoTitleCommon;

//    填写新旧密码View
    @BindView(R.id.setting_moddif_view)
    View setting_moddif_view;
//旧密码
    @BindView(R.id.old_password)
    EditText old_password;
//新密码
    @BindView(R.id.new_password)
    EditText new_password;
//重复新密码
    @BindView(R.id.repeapt_password)
    EditText repeapt_password;
//提交
    @BindView(R.id.setting_btn)
    Button setting_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modiff_password);
        setToolBar();
        unbinder = ButterKnife.bind(this);
        infoTitleCommon.setText("修改密码");
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
