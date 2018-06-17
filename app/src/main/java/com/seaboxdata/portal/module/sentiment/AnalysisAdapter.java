package com.seaboxdata.portal.module.sentiment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.seaboxdata.portal.common.ViewPagerFragmentPagerAdapter;

import java.util.List;

/**
 * Created by zhang on 2018/6/17.
 */

public class AnalysisAdapter extends ViewPagerFragmentPagerAdapter {

    private  String[]  mTitles;
    List<Fragment> tabs;

    public AnalysisAdapter(FragmentManager fm, List<Fragment> tabs, String[] tabNames) {
        super(fm, tabs, tabNames);
        this.mTitles=tabNames;
        this.tabs=tabs;
    }


    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }


    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
