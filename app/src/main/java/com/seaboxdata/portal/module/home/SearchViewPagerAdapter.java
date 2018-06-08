package com.seaboxdata.portal.module.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Description  (用一句话描述这个类的作用)
 * author yh
 * date: 2016/09/18 15:21
 */
public class SearchViewPagerAdapter extends FragmentStatePagerAdapter
{
    private List<? extends Fragment> list;
    private List<String> mFragmentTitles;
    FragmentManager fm;
    boolean[] fragmentsUpdateFlag = { false, false, false, false };

    public SearchViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public SearchViewPagerAdapter(List<? extends Fragment> list, FragmentManager fm)
    {
        super(fm);
        this.list = list;
        this.fm=fm;
    }

    public SearchViewPagerAdapter(List<? extends Fragment> list, List<String> titles, FragmentManager fm)
    {
        this(list, fm);
        this.mFragmentTitles = titles;
        this.fm=fm;
    }

    @Override
    public Fragment getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mFragmentTitles != null)
             return mFragmentTitles.get(position);
        return super.getPageTitle(position);
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        //得到缓存的fragment
//        Fragment fragment = (Fragment) super.instantiateItem(container,
//                position);
//        //得到tag，这点很重要
//        String fragmentTag = fragment.getTag();
//
//        if (fragmentsUpdateFlag[position]) {
//            //如果这个fragment需要更新
//
//            FragmentTransaction ft = fm.beginTransaction();
//            //移除旧的fragment
//            ft.remove(fragment);
//            //换成新的fragment
//            fragment = list.get(position);
//            //添加新fragment时必须用前面获得的tag，这点很重要
//            ft.add(container.getId(), fragment, fragmentTag);
//            ft.attach(fragment);
//            ft.commit();
//
//            //复位更新标志
//            fragmentsUpdateFlag[position] = false;
//        }
//        return fragment;
//    }
}
