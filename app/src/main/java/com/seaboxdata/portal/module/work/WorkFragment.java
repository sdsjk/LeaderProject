package com.seaboxdata.portal.module.work;

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
import android.widget.TextView;

import com.linewell.core.view.FontIconText;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.bean.RemindBean;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.module.remind.RemindAdapter;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by zjianning on 2018/5/11.
 */

public class WorkFragment extends CommonFragment {
    private View view;

    private WorkListFragment workListFragment;

    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_work, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) view.findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            linear_bar.getLayoutParams().height = getStatusBarHeight(mActivity);

            if (StatusBarUtil.StatusBarLightMode(mActivity) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }


        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        FontIconText fontIconText = (FontIconText) view. findViewById(R.id.backBtn);
        fontIconText.setVisibility(View.GONE);
        TextView textView = (TextView) view.findViewById(R.id.centerTitle);
        textView.setText("会议提醒");
        RecyclerView home_tx_recycler= (RecyclerView) view.findViewById(R.id.home_tx_recycler);
        List<RemindBean> allData=new ArrayList<>();
        allData.add(new RemindBean(1,"2018年5月15日",null,null,null,null));

        allData.add(new RemindBean(2,"2018年5月15日","会议一","07-07","特朗普被提名为诺贝尔和平奖候选人","二楼会议室"));
        allData.add(new RemindBean(2,"2018年5月15日","会议二","07-07","宁静今年已经46岁了","三楼会议室"));

        allData.add(new RemindBean(1,"2018年5月16日",null,null,null,null));
        allData.add(new RemindBean(2,"2018年5月15日","会议三","07-07","但是由于保养得宜，看起来依旧非常年轻","四楼会议室"));

        allData.add(new RemindBean(1,"2018年5月17日",null,null,null,null));
        allData.add(new RemindBean(2,"2018年5月17日","会议四","08-08","感觉有那么一丢丢说不出来的诡异","五楼会议室"));
        allData.add(new RemindBean(2,"2018年5月17日","会议五","08-08","照片中宁静依旧走酷帅路线","六楼会议室"));
        allData.add(new RemindBean(2,"2018年5月17日","会议六","08-08","此外宁静还蹲坐在一个柱子上，姿势相当霸气","七楼会议室"));
        home_tx_recycler.setAdapter(new RemindAdapter(getActivity(),allData));
        home_tx_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
