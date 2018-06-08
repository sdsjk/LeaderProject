package com.seaboxdata.portal.module.monitoring;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.seaboxdata.portal.R;

import java.util.Collections;
import java.util.List;

/**
 */

public class BusinessMonitoringAdapter extends RecyclerView.Adapter<BusinessMonitoringAdapter.HeadHolder> implements MyItemTouchCallback.ItemTouchAdapter{



    private Activity mContext;
    private List<JsonObject> mlist;
    public BusinessMonitoringAdapter(Activity activity, List<JsonObject> list){
        mContext = activity;
        mlist = list;
    }

    @Override
    public HeadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_business_monitoring,parent,false);
        return new HeadHolder(v);
    }

    @Override
    public void onBindViewHolder(final HeadHolder holder, final int position) {
        JsonObject info = mlist.get(position);
        if(info == null)
            return;
        holder.tvTitle.setText(info.get("name").getAsString());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 判断当前数据是否已经选中

                // 需要对数据做处理
                // 直接对源数据进行处理
//                mlist.get(position)

                holder.rootView.setSelected(true);



            }
        });
    }

    @Override
    public int getItemCount() {
        if(mlist == null)
            return 0;
        return  mlist.size();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition==mlist.size()  || toPosition==mlist.size() ){
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mlist, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mlist, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        mlist.remove(position);
        notifyItemRemoved(position);
    }

    protected class HeadHolder extends  RecyclerView.ViewHolder{

        View rootView;
        TextView    tvTitle;
        public HeadHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.search_service_name);
            rootView = itemView.findViewById(R.id.item_monitoring_root_view);
        }
    }

    public  void  setData(List<JsonObject> list){
        mlist = list;
        notifyDataSetChanged();
    }
}
