package com.seaboxdata.portal.module.remind;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.bean.RemindBean;

import java.util.List;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by zhang on 2018/6/14.
 */

public class RemindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<RemindBean> mAlldata;
    private final Context mContext;

    public  RemindAdapter(Context context, List<RemindBean> alldata){
        mContext=context;
        mAlldata=alldata;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        switch (viewType){
            case 1:
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.remind_head, parent, false);
                return new HeadHolder(inflate);
            case 2:
                View content = LayoutInflater.from(mContext).inflate(R.layout.remind_content, parent, false);
                return new ListHolder(content);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)){

            case 1:
                HeadHolder headHolder= (HeadHolder) holder;
                headHolder.remind_head.setText(mAlldata.get(position).getData());
                break;
            case 2:
                ListHolder list= (ListHolder) holder;
                    RemindBean remindBean= mAlldata.get(position);
                    list.info_title_name.setText(remindBean.getTitleName());
                    list.info_content_time.setText(remindBean.getTime());
                    list.remind_content.setText(remindBean.getContent());
                    list.remind_adress.setText(remindBean.getAddress());
                try {
                    if(position<mAlldata.size()-1) {
                        if(mAlldata.get(position+1).getType()==1){
                            list.line_split.setVisibility(View.INVISIBLE);
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mAlldata.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mAlldata.get(position).getType();
    }



    public class HeadHolder extends ViewHolder {
        TextView  remind_head;
        public HeadHolder(View itemView) {
            super(itemView);
              remind_head= (TextView) itemView.findViewById(R.id.remind_head);

        }
    }

    public class ListHolder extends ViewHolder {
        TextView  info_title_name,info_content_time,remind_content,remind_adress;
        ImageView remind_icon;
        View line_split;
        public ListHolder(View itemView) {
            super(itemView);
              info_title_name= (TextView) itemView.findViewById(R.id.info_title_name);
              info_content_time= (TextView) itemView.findViewById(R.id.info_content_time);
              remind_content= (TextView) itemView.findViewById(R.id.remind_content);
              remind_adress= (TextView) itemView.findViewById(R.id.remind_adress);
                remind_icon= (ImageView) itemView.findViewById(R.id.remind_icon);
            line_split=itemView.findViewById(R.id.line_split);
        }
    }
}
