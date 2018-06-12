package com.seaboxdata.portal.module.home;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.module.search.SearchActivityNew;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;



/**
 * Created by zjianning on 2018/5/11.
 */

public class HomeFragment extends CommonFragment {
    private View view;
    private RecyclerView recyclerView;
    private HomeRecylerAdapter homeRecylerAdapter;
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
            linear_bar.getLayoutParams().height = getStatusBarHeight(mActivity);

            if (StatusBarUtil.StatusBarLightMode(mActivity) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }
        initView(view);
        return view;
    }
    private void initView(View view) {
        recyclerView= (RecyclerView) view.findViewById(R.id.home_fragment_new);
        homeRecylerAdapter=new HomeRecylerAdapter(getActivity());
        recyclerView.setAdapter(homeRecylerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ImageView imageView= (ImageView) view.findViewById(R.id.home_search);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SearchActivityNew.class));
            }
        });

    }


}
