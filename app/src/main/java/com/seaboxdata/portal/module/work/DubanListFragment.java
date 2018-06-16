package com.seaboxdata.portal.module.work;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2018/6/13.
 */

public class DubanListFragment extends CommonFragment {
    private RecyclerView recyclerView;
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.duban_list, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
         List<WorkListBean> allData=new ArrayList<>();
        allData.add(new WorkListBean("Yu","北京市局(公司)进一步加强规范督办工作","2018年6月13日讯，美国财政部11日决定制裁俄罗斯3名个人和5家实体，“罪状”是协助俄方军事和情报部门对美方及其盟友发起网络攻击","05-28"));
        allData.add(new WorkListBean("Yu","盱眙龙虾节迎来“成人礼小龙虾成就百亿级产业链","中新网淮安6月12日电 (记者 郭亚楠)12日，第十八届中国·盱眙(金诚)国际龙虾节开幕(以下简称“盱眙龙虾节”)。已经举办了十七届的“龙虾盛宴”，今年迎来十八岁“成人礼”。从“养在深闺无人识”到如今的享誉全国","05-28"));
        allData.add(new WorkListBean("Yu","强对流天气蓝色预警：北京山东等7省有雷暴大风冰雹 南方大雨持续","中国天气网讯 今明天（13-14日），华南“龙舟水”持续，广西、广东局地有大雨或暴雨，需防范叠加灾害。同时，东北华北一带雷雨天气频发，而黄淮等地天气酷热，局地可超37℃","05-28"));
        allData.add(new WorkListBean("Yu","顾雏军:政府让我救科龙 当时最高检办案动机不纯","顾雏军案再审#【进行法庭调查】审判员张勇健：现在质证第四组证据，分别是证据37-39、41-45、47、49、51-53,全部是证人证言","05-28"));
        recyclerView= (RecyclerView) inflate.findViewById(R.id.work_duban_list);
        recyclerView.setAdapter(new WorkListAdapter(getActivity(),allData));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


}
