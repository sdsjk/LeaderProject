package com.seaboxdata.portal.module.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.seaboxdata.portal.R;

import java.util.List;

/**
 * Created by zhang on 2018/6/16.
 */

public class SuggestGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> mAllData;


    public SuggestGridViewAdapter(Context context, List<Integer> allData){

        mContext=context;
        mAllData=allData;
    }

    @Override
    public int getCount() {
        return mAllData.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) {
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case 0:
                View view= LayoutInflater.from(mContext).inflate(R.layout.my_suggest_grid_item,parent,false);
                return view;
            case 1:
                View view1= LayoutInflater.from(mContext).inflate(R.layout.my_suggest_grid_item,parent,false);
                return view1;
        }
        return null;
    }
}
