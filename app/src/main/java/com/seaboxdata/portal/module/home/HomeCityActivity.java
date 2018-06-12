package com.seaboxdata.portal.module.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.category.CategorySubActivity;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.module.search.SearchActivity;
import com.seaboxdata.portal.module.search.SearchActivityNew;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeCityActivity extends CommonActivity {

    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> listFragments;
    private String[] mTabTitle;
    private SearchViewPagerAdapter mPagerAdapter;
    protected Unbinder unbinder;
    @BindView(R.id.search_fit)
    View searchFITView;

    @BindView(R.id.category_sub)
    View categorySubView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
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
        unbinder = ButterKnife.bind(this, this);
        initView();
    }

    private void initView() {
        mTabLayout = (SlidingTabLayout)findViewById(R.id.m_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.m_viewpager);

        initFragment();

        searchFITView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SearchActivity.startAction(HomeCityActivity.this, "");
                startActivity(new Intent(HomeCityActivity.this, SearchActivityNew.class));
            }
        });

        categorySubView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySubActivity.startAction(HomeCityActivity.this, false);
            }
        });

    }

    private void initFragment() {
        listFragments = new ArrayList<>();

        // 数据从服务端获取
        // TODO
        mTabTitle = new String[]{"城市交通", "经济运行", "环境气象", "城市生命线", "一带一路"};
        listFragments.add(getFragment(1));
        listFragments.add(getFragment(2));
        listFragments.add(getFragment(4));
        listFragments.add(getFragment(3));
        listFragments.add(getFragment(1));

        mPagerAdapter = new SearchViewPagerAdapter(listFragments, Arrays.asList(mTabTitle), getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mViewPager, mTabTitle, this, (ArrayList<Fragment>)listFragments);
    }

    private Fragment getFragment(int i) {
        HomeContentFragment fragment = new HomeContentFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DATA, i);
//        bundle.putSerializable(RecyclerViewFragment.PARAMS,
//                getListParams(id, InnochinaServiceConfig.Service.getArticleListByCategoryId, R.layout.item_discovery_news));
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }

    }

}
