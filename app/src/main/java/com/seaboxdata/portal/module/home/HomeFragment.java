package com.seaboxdata.portal.module.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;


/**
 * Created by zjianning on 2018/5/11.
 */

public class HomeFragment extends CommonFragment {
    private View view;
    private RecyclerView recyclerView;
    private HomeRecylerAdapter homeRecylerAdapter;
    View home_title;
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_new, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) view.findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
            linear_bar.getLayoutParams().height = 0;
            if (StatusBarUtil.StatusBarLightMode(mActivity) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }
        initView(view);
        return view;
    }
    private void initView(View view) {

        home_title=view.findViewById(R.id.home_title);
        recyclerView= (RecyclerView) view.findViewById(R.id.home_fragment_new);
        homeRecylerAdapter=new HomeRecylerAdapter(getActivity());
        recyclerView.setAdapter(homeRecylerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(mOnScrollListener);


    }
    int mDistance = 0;
    int maxDistance = 255;//当距离在[0,255]变化时，透明度在[0,255之间变化]
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        //dy:每一次竖直滑动增量 向下为正 向上为负
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            mDistance += dy;
            float percent = mDistance * 1f / maxDistance;//百分比
            int alpha = (int) (percent * 255);
            int argb = Color.argb(alpha, 57, 174, 255);
            setSystemBarAlpha(alpha);
        }
    };

    /**
     * 设置标题栏背景透明度
     * @param alpha 透明度
     */
    private void setSystemBarAlpha(int alpha) {

        if (alpha >= 255) {
            home_title.setBackgroundColor(Color.argb(255, 48, 63, 159));
        } else {
            //标题栏渐变。a:alpha透明度 r:红 g：绿 b蓝
//        titlebar.setBackgroundColor(Color.rgb(57, 174, 255));//没有透明效果
            home_title.setBackgroundColor(Color.argb(alpha, 48, 63, 159));//透明效果是由参数1决定的，透明范围[0,255]
//            home_title.getBackground().setAlpha(alpha);
        }
//        home_title.setBackgroundColor(Color.argb(alpha*2, 255, 255, 255));//透明效果是由参数1决定的，透明范围[0,255]

    }

}
