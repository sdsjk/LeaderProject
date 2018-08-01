package com.seaboxdata.portal.module.search;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;

public class SearchActivityNew extends CommonActivity {

    private String[] mTitles = new String[]{"综合", "指标", "资料","已阅","信息"};
    private SlidingTabLayout tabLayout;
    private ViewPager search_view_pager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new);
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
        tabLayout= (SlidingTabLayout) findViewById(R.id.search_table);
//        setTabLine(tabLayout,30,10);
        search_view_pager= (ViewPager) findViewById(R.id.search_view_pager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), SearchActivityNew.this,mTitles);
        search_view_pager.setAdapter(myFragmentPagerAdapter);
//        tabLayout.setupWithViewPager(search_view_pager);
        ArrayList<Fragment> listFragments = new ArrayList<>();
        listFragments.add(getFragment(0));
        listFragments.add(getFragment(1));
        listFragments.add(getFragment(2));
        listFragments.add(getFragment(3));
        listFragments.add(getFragment(4));
        tabLayout.setViewPager(search_view_pager, mTitles, this, (ArrayList<Fragment>)listFragments);
    }
    private Fragment getFragment(int i) {

        SearchFrament fragment = new SearchFrament();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DATA, i);
//        bundle.putSerializable(RecyclerViewFragment.PARAMS,
//                getListParams(id, InnochinaServiceConfig.Service.getArticleListByCategoryId, R.layout.item_discovery_news));
        fragment.setArguments(bundle);

        return fragment;
    }
}
