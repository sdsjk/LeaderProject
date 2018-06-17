package com.seaboxdata.portal.module.sentiment;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.bean.MoreInfoBean;

import java.util.List;

/**
 * Created by zhang on 2018/6/16.
 */

public class SentimentMoreAdapter extends RecyclerView.Adapter<SentimentMoreAdapter.MyViewhoder> {
    Context context;
    List<MoreInfoBean> allData;

    public SentimentMoreAdapter(Context context, List<MoreInfoBean> allData) {
        this.context = context;
        this.allData = allData;
    }

    @Override
    public MyViewhoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_more_info, parent, false);
        return new MyViewhoder(view);
    }

    @Override
    public void onBindViewHolder(MyViewhoder holder, int position) {

        MoreInfoBean moreInfoBean = allData.get(position);
        holder.item_list_index.setText(moreInfoBean.getIndex()+"");
        holder.item_list_title.setText(moreInfoBean.getTitle());
        holder.item_list_content.setText(moreInfoBean.getContent());

        if (position == 0) {
            holder.item_list_index.setBackgroundColor(Color.parseColor("#ed2f10"));
        } else if (position == 1) {
            holder.item_list_index.setBackgroundColor(Color.parseColor("#f95a04"));
        } else if (position == 2) {
            holder.item_list_index.setBackgroundColor(Color.parseColor("#f4af09"));
        }else {
            holder.item_list_index.setBackgroundColor(Color.parseColor("#d2d2d1"));
        }
        if (moreInfoBean.getStatus() == 1) {
            holder.item_list_icon.setImageResource(R.drawable.hotup);
        } else if (moreInfoBean.getStatus() == 2) {
            holder.item_list_icon.setImageResource(R.drawable.hotdown);
        } else {
            holder.item_list_icon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class MyViewhoder extends RecyclerView.ViewHolder {
        TextView item_list_index, item_list_title, item_list_content;
        ImageView item_list_icon;

        public MyViewhoder(View itemView) {
            super(itemView);
            item_list_index = (TextView) itemView.findViewById(R.id.item_list_index);
            item_list_title = (TextView) itemView.findViewById(R.id.item_list_title);
            item_list_content = (TextView) itemView.findViewById(R.id.item_list_content);
            item_list_icon = (ImageView) itemView.findViewById(R.id.item_list_icon);


        }
    }
}
