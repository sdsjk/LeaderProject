package com.seaboxdata.portal.module.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.seaboxdata.portal.R;

import java.util.List;

/**
 * Created by zhang on 2018/6/9.
 */

class HomeInfoListAdapter extends BaseAdapter {
    private final List<HomeInfoBean> mAllData;
    private final Context mContext;
    LayoutInflater from;
    public HomeInfoListAdapter(Context context, List<HomeInfoBean> allData) {
        mAllData=allData;
        mContext=context;
        from = LayoutInflater.from(context);

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= from.inflate(R.layout.home_info_list,null);
            viewHolder.title= (TextView) convertView.findViewById(R.id.home_info_list_title);
            viewHolder.time= (TextView) convertView.findViewById(R.id.home_info_list_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mAllData.get(position).getInfo());
        viewHolder.time.setText(mAllData.get(position).getInfotime());
        return convertView;
    }
    public  final class ViewHolder{
        public TextView time;
        public TextView title;
    }
}
