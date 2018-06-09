package com.seaboxdata.portal.module.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seaboxdata.portal.R;

import java.util.List;

/**
 * Created by zhang on 2018/6/9.
 */

public class HomeGridViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<HomeFragmentYwBean> mAllData;
    private final LayoutInflater mInflater;

    public HomeGridViewAdapter(Context context, List<HomeFragmentYwBean> allData) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mAllData = allData;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_gridview_item, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.gridName = (TextView) convertView
                    .findViewById(R.id.grid_names);
            viewHolder.gridIcon = (ImageView) convertView
                    .findViewById(R.id.grid_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.gridName.setText(mAllData.get(position).getIconName());
        viewHolder.gridIcon.setBackgroundResource(mAllData.get(position).getIcon());

        return convertView;
    }

    private final class ViewHolder {
        ImageView gridIcon;
        TextView gridName;
    }
}
