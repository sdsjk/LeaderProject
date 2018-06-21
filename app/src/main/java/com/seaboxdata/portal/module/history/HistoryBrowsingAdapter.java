package com.seaboxdata.portal.module.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.bean.RemindBean;

import java.util.List;

/**
 * Created by zhang on 2018/6/15.
 */

public class HistoryBrowsingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<RemindBean> mAlldata;

    public  HistoryBrowsingAdapter(Context context, List<RemindBean> alldata){
        mContext=context;
        mAlldata=alldata;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case 1:
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.history_browser_head, parent, false);
                return new HeadHolder(inflate);
            case 2:
                View content = LayoutInflater.from(mContext).inflate(R.layout.history_browser_content, parent, false);
                return new ListHolder(content);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){

            case 1:
                HeadHolder headHolder= (HeadHolder) holder;
                headHolder.history_browser_head.setText(mAlldata.get(position).getData());
                break;
            case 2:
                ListHolder list= (ListHolder) holder;
                RemindBean remindBean= mAlldata.get(position);
                list.history_browser_content.setText(remindBean.getContent());
                try {
                    if(position<mAlldata.size()-1) {
                        if(mAlldata.get(position+1).getType()==1){
                            list.split_view.setVisibility(View.INVISIBLE);
                        }
                    }
                }catch (Exception r){
                    r.printStackTrace();
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



    public class HeadHolder extends RecyclerView.ViewHolder {
        TextView history_browser_head;
        public HeadHolder(View itemView) {
            super(itemView);
            history_browser_head= (TextView) itemView.findViewById(R.id.history_browser_head);

        }
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        TextView  history_browser_content;
        View split_view;
        public ListHolder(View itemView) {
            super(itemView);
            history_browser_content= (TextView) itemView.findViewById(R.id.history_browser_content);
            split_view=itemView.findViewById(R.id.split_view);
        }
    }

}
