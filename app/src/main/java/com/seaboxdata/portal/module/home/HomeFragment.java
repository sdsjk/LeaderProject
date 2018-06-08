package com.seaboxdata.portal.module.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.category.CategorySubActivity;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.module.search.SearchActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zjianning on 2018/5/11.
 */

public class HomeFragment extends CommonFragment {


    private View view;

    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> listFragments;
    private String[] mTabTitle;
    private SearchViewPagerAdapter mPagerAdapter;

    @BindView(R.id.search_fit)
    View searchFITView;

    @BindView(R.id.category_sub)
    View categorySubView;

    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) view.findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
            linear_bar.getLayoutParams().height = getStatusBarHeight(mActivity);

            if (StatusBarUtil.StatusBarLightMode(mActivity) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }

        unbinder = ButterKnife.bind(this, view);

        initView(view);

        return view;
    }

    private void initView(View view) {

        mTabLayout = (SlidingTabLayout) view.findViewById(R.id.m_tablayout);
        mViewPager = (ViewPager) view.findViewById(R.id.m_viewpager);

        initFragment();

        searchFITView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.startAction(mActivity, "");
            }
        });

        categorySubView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySubActivity.startAction(mActivity, false);
            }
        });

    }

    /**
     * 初始化fragment页面
     */
    public void initFragment() {
        listFragments = new ArrayList<>();

        // 数据从服务端获取
        // TODO
        mTabTitle = new String[]{"城市交通", "经济运行", "环境气象", "城市生命线", "一带一路"};
        listFragments.add(getFragment(1));
        listFragments.add(getFragment(2));
        listFragments.add(getFragment(4));
        listFragments.add(getFragment(3));
        listFragments.add(getFragment(1));

        mPagerAdapter = new SearchViewPagerAdapter(listFragments, Arrays.asList(mTabTitle), getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mViewPager, mTabTitle, (FragmentActivity) mActivity, (ArrayList<Fragment>)listFragments);
    }


    private HomeContentFragment getFragment(int i) {

        HomeContentFragment fragment = new HomeContentFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DATA, i);
//        bundle.putSerializable(RecyclerViewFragment.PARAMS,
//                getListParams(id, InnochinaServiceConfig.Service.getArticleListByCategoryId, R.layout.item_discovery_news));
        fragment.setArguments(bundle);

        return fragment;
    }


}
