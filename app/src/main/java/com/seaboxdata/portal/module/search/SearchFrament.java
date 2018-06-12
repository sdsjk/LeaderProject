package com.seaboxdata.portal.module.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linewell.core.view.AutoFixViewGroup;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.module.home.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhang on 2018/6/11.
 */

public class SearchFrament extends CommonFragment {

    private int mIndex = 1;
    private View view;
    @BindView(R.id.content_home_view)
    LinearLayout mContentHomeView;

    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mIndex = getArguments().getInt(KEY_DATA);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mContentHomeView.removeAllViews();

        // 重要指标
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        int layoutId = R.layout.item_search_zh;
        switch (mIndex) {
            case 0:
                layoutId = R.layout.item_search_zh;
                break;
            case 1:
                layoutId = R.layout.item_search_zb;
                break;
            case 2:
                layoutId = R.layout.item_search_info;
                break;
            case 3:
                layoutId = R.layout.item_search_info;
                break;
            case 4:
                layoutId = R.layout.item_search_info;
                break;
        }
        View cityTrafficView = layoutInflater.inflate(layoutId, mContentHomeView, false);
        mContentHomeView.addView(cityTrafficView);

        if(mIndex==0){
            AutoFixViewGroup autoFixViewGroup = (AutoFixViewGroup) cityTrafficView.findViewById(R.id.viewgroup_keywor_list);
            AutoFixViewGroup viewgroup_keywor_list_last = (AutoFixViewGroup) cityTrafficView.findViewById(R.id.viewgroup_keywor_list_last);
            ListView search_info_list= (ListView) cityTrafficView.findViewById(R.id.search_info_list);
            List<String> allData=new ArrayList<>();
            allData.add("89岁的妈妈整宿整宿不睡觉 ");
            allData.add("60岁的儿子一夜一夜陪护她");
            allData.add("杜应征画的母亲 杜应征供图");
            allData.add("边画画边陪伴母亲 杜应征供图");
            allData.add("边画画边陪伴母亲 杜应征供图");

            search_info_list.setAdapter(new ArrayAdapter(getActivity(),R.layout.search_new,allData));

            List<String> list = new ArrayList<>();
            list.add("北京流动人口");
            list.add("高新企业流动人数");
            showHotKeyword(list,autoFixViewGroup);
            showHotKeyword(list,viewgroup_keywor_list_last);
        }else if(mIndex==1) {
            AutoFixViewGroup viewgroup_keywor_list_zb = (AutoFixViewGroup) cityTrafficView.findViewById(R.id.viewgroup_keywor_list_zb);
            List<String> list = new ArrayList<>();
            list.add("交通指数");
            list.add("北京流动人口");
            list.add("高新企业从业人数");
            showHotKeyword(list,viewgroup_keywor_list_zb);

        }else if(mIndex==2||mIndex==3||mIndex==4){
            List<String> allData=new ArrayList<>();
            allData.add("89岁的妈妈整宿整宿不睡觉,60岁的儿子一夜一夜陪护她 ");
            allData.add("杜应征画的母亲 杜应征供图");
            allData.add("边画画边陪伴母亲 杜应征供图");
            allData.add("边画画边陪伴母亲 杜应征供图");
            MyListView search_info_list_data= (MyListView) cityTrafficView.findViewById(R.id.search_info_list_data);
            search_info_list_data.setAdapter(new ArrayAdapter(getActivity(),R.layout.search_new,allData));
        }

    }

    private void showHotKeyword(List<String> list, AutoFixViewGroup autoFixViewGroup) {
        if(list==null||list.size()==0){
            return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        for(final String item:list){
            View view = layoutInflater.inflate(R.layout.item_search_hot, autoFixViewGroup, false);
            TextView textView = (TextView) view.findViewById(R.id.text_keyword);
            textView.setText(item);
            autoFixViewGroup.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
