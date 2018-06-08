package com.seaboxdata.portal.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * tablayout与viewPager结合需要的adapter
 * Created by zjianning on 2016/8/12.
 */
public class ViewPagerFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mTabs;

    private String[] mTabNames;

    public ViewPagerFragmentPagerAdapter(FragmentManager fm,
                                         List<Fragment> tabs,
                                         String[] tabNames) {
        super(fm);
        this.mTabs = tabs;
        this.mTabNames = tabNames;
    }

    @Override
    public Fragment getItem(int position) {
        return mTabs.get(position);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public void destroyItem(View container, int position, Object object) {

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNames[position];
    }
}
