package com.seaboxdata.portal.module.sentiment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

public class BriefMakeActivity extends CommonActivity {
    private ImageView infomation_back;
    private TextView info_title_common,infomation_close,info_title_common_commit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_make);
        setToolBar();
        initView();
    }

    private void initView() {
        info_title_common= (TextView) findViewById(R.id.info_title_common);
        infomation_close= (TextView) findViewById(R.id.infomation_close);
        info_title_common_commit= (TextView) findViewById(R.id.info_title_common_commit);

        info_title_common.setText("制作简报");
        infomation_back= (ImageView) findViewById(R.id.infomation_back);
        infomation_back.setVisibility(View.GONE);
        infomation_close.setVisibility(View.VISIBLE);
        info_title_common_commit.setVisibility(View.VISIBLE);
        info_title_common_commit.setText("创建");
        infomation_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
