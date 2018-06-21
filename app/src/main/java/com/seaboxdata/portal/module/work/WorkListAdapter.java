package com.seaboxdata.portal.module.work;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.module.info.InfoDetailsActivity;

import java.util.List;

/**
 * Created by zhang on 2018/6/13.
 */

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.MyViewHolder> {
    private  List<WorkListBean> allData;
    private Context mContext;
    public WorkListAdapter(Context context, List<WorkListBean> data){
        mContext=context;
        allData=data;
    }

    @Override
    public WorkListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(mContext,R.layout.work_item_duban,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkListAdapter.MyViewHolder holder, int position) {
        holder.time.setText(allData.get(position).getTime());
        holder.title.setText(allData.get(position).getTitle());
        holder.content.setText(allData.get(position).getContent());
        if(allData.size()-1==position) {
            holder.split_view.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return allData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView time;
        private TextView title;
        private TextView content;
        private View split_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.item_work_time);
            title= (TextView) itemView.findViewById(R.id.item_list_title);
            content= (TextView) itemView.findViewById(R.id.item_list_content);
            split_view=itemView.findViewById(R.id.split_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, InfoDetailsActivity.class);
                    intent.putExtra("mIndexPage",1);
                    mContext.startActivity(intent);
                }
            });


        }
    }
}
