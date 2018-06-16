package com.seaboxdata.portal.module.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.bean.RemindBean;
import com.seaboxdata.portal.common.CommonFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2018/6/14.
 */

public class HistorybrowserFragment extends CommonFragment {
    RecyclerView history_browser_recyclerview;
    View historyPush;
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         historyPush = inflater.inflate(R.layout.history_browser, container, false);
        initView();
        return historyPush;

    }

    private void initView() {
        history_browser_recyclerview= (RecyclerView) historyPush.findViewById(R.id.history_browser_recyclerview);
        List<RemindBean> allData=new ArrayList<>();
        allData.add(new RemindBean(1,"5月15日   周一",null,null,null,null));

        allData.add(new RemindBean(2,"5月15日","会议一","07-07","特朗普被提名为诺贝尔和平奖候选人","地点: 二楼会议室"));
        allData.add(new RemindBean(2,"5月15日","会议二","07-07","宁静今年已经46岁了","地点: 三楼会议室"));

        allData.add(new RemindBean(1,"5月16日    周一",null,null,null,null));
        allData.add(new RemindBean(2,"5月15日","会议三","07-07","但是由于保养得宜，看起来依旧非常年轻","地点: 四楼会议室"));

        allData.add(new RemindBean(1,"5月17日    周一",null,null,null,null));
        allData.add(new RemindBean(2,"5月17日","会议四","08-08","感觉有那么一丢丢说不出来的诡异","地点: 五楼会议室"));
        allData.add(new RemindBean(2,"5月17日","会议五","08-08","照片中宁静依旧走酷帅路线","地点: 六楼会议室"));
        allData.add(new RemindBean(2,"5月17日","会议六","08-08","此外宁静还蹲坐在一个柱子上，姿势相当霸气","地点: 七楼会议室"));
        history_browser_recyclerview.setAdapter(new HistoryBrowsingAdapter(getActivity(),allData));
        history_browser_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
