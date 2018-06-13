package com.seaboxdata.portal.module.work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

public class WorkListActivity extends CommonActivity implements View.OnClickListener {
//    标题栏文本
    private TextView info_title_common;
//    返回键
    private  ImageView infomation_back;
//    控制显示列表
    private int mIndex;
    public static final int  WORKDUBAN=0;
    public static final int  WORKEMAIL=1;
    public static final int  WORKWORD=2;
    public static final String MINDEX="mIndex";
    public static void StartWorkListActivity(Context context,int index){
        Intent intent=new Intent(context,WorkListActivity.class);
        intent.putExtra(MINDEX,index);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);
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
        mIndex = intent.getIntExtra("mIndex", 0);
        initView();
    }

    private void initView() {
        info_title_common= (TextView) findViewById(R.id.info_title_common);
        infomation_back= (ImageView) findViewById(R.id.infomation_back);
        infomation_back.setOnClickListener(this);
        info_title_common.setText("办公辅助");
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        switch (mIndex){
            case WORKDUBAN:
                fragmentTransaction.add(R.id.work_list_content,new DubanListFragment());
                break;
            case WORKEMAIL:
                fragmentTransaction.add(R.id.work_list_content,new EmailListFragment());
                break;
            case WORKWORD:
                fragmentTransaction.add(R.id.work_list_content,new DubanListFragment());
                break;
        }
        fragmentTransaction.commit();
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
