package com.seaboxdata.portal.module.info;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

public class InfoListActivity extends CommonActivity implements View.OnClickListener {
    private String titleName;
    private TextView info_title_common;
    private ImageView infomation_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);

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

        Intent intent = getIntent();
        if(intent!=null){
            titleName = intent.getStringExtra("titleName");
        }
        initView();
    }

    private void initView() {
        RecyclerView info_list= (RecyclerView) findViewById(R.id.info_list);
        info_list.setAdapter(new MyInfoListAdapter(this));
        info_list.setLayoutManager(new LinearLayoutManager(this));
        info_title_common= (TextView) findViewById(R.id.info_title_common);
        info_title_common.setText(titleName);
        infomation_back= (ImageView) findViewById(R.id.infomation_back);
        infomation_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v==infomation_back){
            finish();
        }
    }
}
