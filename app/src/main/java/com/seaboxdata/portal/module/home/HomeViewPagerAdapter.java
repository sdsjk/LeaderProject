package com.seaboxdata.portal.module.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhang on 2018/6/9.
 */

public class HomeViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    List<ImageView> mList;
    public HomeViewPagerAdapter(Context context,List<ImageView> mList){
        mContext=context;
        this.mList=mList;

    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // return super.instantiateItem(container, position);
        // 修改position
        position = position % mList.size();
        // 将图片控件添加到容器
        View view=mList.get(position);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(mList.get(position));

        // 返回
        return mList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
