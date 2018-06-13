package com.seaboxdata.portal.module.work;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seaboxdata.portal.R;

import java.util.List;

/**
 * Created by zhang on 2018/6/13.
 */

public class EmailListAdapter extends RecyclerView.Adapter<EmailListAdapter.MyViewHolder> {
    private List<WorkListBean> allData;
    private Context mContext;
    public EmailListAdapter(Context context, List<WorkListBean> data){
        mContext=context;
        allData=data;
    }

    @Override
    public EmailListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(mContext, R.layout.work_item_eamil,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.time.setText(allData.get(position).getTime());
        holder.title.setText(allData.get(position).getTitle());
        holder.content.setText(allData.get(position).getContent());
        holder.work_title_name.setText(allData.get(position).getFristName());
        holder.work_email_fristname.setText(allData.get(position).getFristName());
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView time;
        private TextView title;
        private TextView content;
        private TextView work_title_name,work_email_fristname;

        public MyViewHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.item_work_time);
            title= (TextView) itemView.findViewById(R.id.item_list_title);
            content= (TextView) itemView.findViewById(R.id.item_list_content);
            work_title_name= (TextView) itemView.findViewById(R.id.work_title_name);
            work_email_fristname= (TextView) itemView.findViewById(R.id.work_email_fristname);

        }
    }
}
