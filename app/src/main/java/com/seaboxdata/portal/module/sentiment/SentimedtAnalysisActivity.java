package com.seaboxdata.portal.module.sentiment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;


public class SentimedtAnalysisActivity extends CommonActivity {
    private String[] mTitles = new String[]{"热度分析", "传播分析", "人群分析"};
    private TabLayout tabLayout;
    private ViewPager search_view_pager;
    private AnalysisAdapter myFragmentPagerAdapter;
    private List<Fragment> tabs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentimedt_analysis);

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
        initView();
    }

    private void initView() {
        tabs.add(new HotAnalysisFragment());
        tabs.add(new SpreadAnailsisFragment());
        tabs.add(new CrowdAnalysisFragment());
        tabLayout= (TabLayout) findViewById(R.id.search_table);
        search_view_pager= (ViewPager) findViewById(R.id.search_view_pager);
        myFragmentPagerAdapter = new AnalysisAdapter(getSupportFragmentManager(), tabs,mTitles);
        search_view_pager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(search_view_pager);
    }
}
