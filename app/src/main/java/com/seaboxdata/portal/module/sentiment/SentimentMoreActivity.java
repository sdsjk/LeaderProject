package com.seaboxdata.portal.module.sentiment;

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
import com.seaboxdata.portal.bean.MoreInfoBean;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class SentimentMoreActivity extends CommonActivity {
    private ImageView infomation_back;
    private TextView info_title_common;
    private RecyclerView more_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment_more);
        setToolBar();
        initView();
    }

    private void initView() {
        info_title_common= (TextView) findViewById(R.id.info_title_common);
        info_title_common.setText("更多信息");
        infomation_back= (ImageView) findViewById(R.id.infomation_back);

        more_info= (RecyclerView) findViewById(R.id.more_info);
        List<MoreInfoBean> allData=new ArrayList<>();
        allData.add(new MoreInfoBean(1,"国产航母首次海试","时尚领域标签:科技",1));
        allData.add(new MoreInfoBean(2,"全国300名学术精英讨论世界杯","时尚领域标签:世界杯",2));
        allData.add(new MoreInfoBean(3,"世界杯首个乌龙球","时尚领域标签:世界杯",1));
        allData.add(new MoreInfoBean(4,"美航取消数百航班","时尚领域标签:交通",3));
        allData.add(new MoreInfoBean(5,"文章为马伊琍骄傲","时尚领域标签:娱乐",3));
        allData.add(new MoreInfoBean(6,"江玥枪杀案判刑","时尚领域标签:科技",3));
        allData.add(new MoreInfoBean(7,"C罗连续四届进球","时尚领域标签:科技",3));
        more_info.setAdapter(new SentimentMoreAdapter(this,allData));

        more_info.setLayoutManager(new LinearLayoutManager(this));
        infomation_back.setOnClickListener(new View.OnClickListener() {
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
