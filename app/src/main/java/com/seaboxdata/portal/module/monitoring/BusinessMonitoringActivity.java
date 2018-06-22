package com.seaboxdata.portal.module.monitoring;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.linewell.pulllistview.ListParams;
import com.linewell.pulllistview.RecyclerViewFragment;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.config.ServiceConfig;
import com.seaboxdata.portal.module.home.SearchViewPagerAdapter;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 日程的详情页
 * Created by zjianning on 2018/5/15.
 */

public class BusinessMonitoringActivity extends CommonActivity {

    @BindView(R.id.m_tablayout)
    SlidingTabLayout mTabLayout;

    @BindView(R.id.m_viewpager)
    ViewPager mViewPager;

    ImageView infomation_back;
    TextView info_title_common;
    private List<Fragment> listFragments;
    private String[] mTabTitle;
    private SearchViewPagerAdapter mPagerAdapter;

    public static void startAction(Activity activity, String title) {
        Intent intent = new Intent(activity, BusinessMonitoringActivity.class);
        intent.putExtra(KEY_TITLE,title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_business_monitoring);
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
        setCenterTitle(getIntent().getStringExtra(KEY_TITLE));
        initView();

        initData();
    }

    private void initData() {



    }

    private void initView() {
        infomation_back= (ImageView) findViewById(R.id.infomation_back);
        info_title_common= (TextView) findViewById(R.id.info_title_common);
        info_title_common.setText("业务检测指标设置");
        infomation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initFragment();
    }

    /**
     * 初始化fragment页面
     */
    public void initFragment() {
        listFragments = new ArrayList<>();

        // 数据从服务端获取
        // TODO
        mTabTitle = new String[]{"城市交通", "经济运行", "环境气象", "城市生命线", "一带一路"};
        listFragments.add(getFragment());
        listFragments.add(getFragment());
        listFragments.add(getFragment());
        listFragments.add(getFragment());
        listFragments.add(getFragment());

        mPagerAdapter = new SearchViewPagerAdapter(listFragments, Arrays.asList(mTabTitle), getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mViewPager, mTabTitle, (FragmentActivity) mCommonActivity, (ArrayList<Fragment>)listFragments);
    }


    private BusinessMonitoringListFragment getFragment() {

        BusinessMonitoringListFragment fragment = new BusinessMonitoringListFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(RecyclerViewFragment.PARAMS,
                getListParams(ServiceConfig.BASE, R.layout.item_business_monitoring));
        fragment.setArguments(bundle);

        return fragment;
    }


    /**
     *
     * @param serviceUrl
     * @param layoutId
     * @return
     */
    public ListParams getListParams(String serviceUrl, int layoutId){
        ListParams params = new ListParams();
        params.setrClass(R.id.class);
        params.setItemLayoutId(layoutId);
        params.setServiceUrl(serviceUrl);
        params.setForbidDown(true);

        // TODO
        params.setLoadLocal(true);

        return params;
    }

}
