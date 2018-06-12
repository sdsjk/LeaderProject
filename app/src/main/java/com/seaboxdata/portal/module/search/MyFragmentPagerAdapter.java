package com.seaboxdata.portal.module.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.seaboxdata.portal.common.CommonFragment.KEY_DATA;

/**
 * Created by zhang on 2018/6/11.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final String[] mTitles;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context, String[] mTitles) {
        super(fm);
        this.context = context;
        this.mTitles=mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return getFragment(0);
            case 1:
                return getFragment(1);
            case 2:
                return getFragment(2);
            case 3:
                return getFragment(3);
            case 4:
                return getFragment(4);
            default:
                return getFragment(2);
        }
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
