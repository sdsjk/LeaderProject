package com.seaboxdata.portal.module.work;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

public class WorkAyActivity extends CommonActivity implements View.OnClickListener {
    RelativeLayout workduban,work_myeamil,work_myword;
    TextView info_title_common;
    ImageView infomation_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_ay);

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
        intView();
    }

    private void intView() {
        workduban= (RelativeLayout) findViewById(R.id.work_duban);
        workduban.setOnClickListener(this);
        work_myeamil= (RelativeLayout) findViewById(R.id.work_myemail);
        work_myeamil.setOnClickListener(this);
        work_myword= (RelativeLayout) findViewById(R.id.work_myword);
        work_myword.setOnClickListener(this);
        info_title_common= (TextView) findViewById(R.id.info_title_common);
        infomation_back= (ImageView) findViewById(R.id.infomation_back);
        infomation_back.setOnClickListener(this);
        info_title_common.setText("办公辅助");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.work_duban:
                WorkListActivity.StartWorkListActivity(this,0);
                break;
            case R.id.work_myemail:
                WorkListActivity.StartWorkListActivity(this,1);
                break;
            case  R.id.work_myword:
                WorkListActivity.StartWorkListActivity(this,2);
                break;
            case R.id.infomation_back:
                finish();
                break;
        }
    }
}
