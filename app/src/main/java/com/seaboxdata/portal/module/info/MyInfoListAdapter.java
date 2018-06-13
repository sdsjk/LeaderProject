package com.seaboxdata.portal.module.info;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seaboxdata.portal.R;

/**
 * Created by zhang on 2018/6/12.
 */

    public class MyInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private final Context context;

        public MyInfoListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    View info_top = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_top, parent, false);
                    return new MyViewholder1(info_top);
                case 1:
                    View info_top_image = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_top_image, parent, false);
                    return new MyViewholder2(info_top_image);
                case 2:
                    View info_top_image_nohot = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_top_image_nohot, parent, false);
                    return new MyViewholder3(info_top_image_nohot);
                case 3:
                    View info_nomal = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_nomal, parent, false);
                    return new MyViewholder4(info_nomal);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 8;
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                    return 0;
                case 1:
                    return 0;
                case 2:
                    return 1;
                case 3:
                    return 2;
                case 4:
                    return 3;
                case 5:
                    return 3;
                case 6:
                    return 1;
                case 7:
                    return 1;
                default:
                    return 5;
            }
        }


        class MyViewholder1 extends RecyclerView.ViewHolder {

            public MyViewholder1(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,InfoDetailsActivity.class));
                    }
                });

            }
        }



        class MyViewholder2 extends RecyclerView.ViewHolder {

            public MyViewholder2(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,InfoDetailsActivity.class));
                    }
                });
            }
        }

        class MyViewholder3 extends RecyclerView.ViewHolder {

            public MyViewholder3(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,InfoDetailsActivity.class));
                    }
                });
            }
        }
        class MyViewholder4 extends RecyclerView.ViewHolder {

            public MyViewholder4(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,InfoDetailsActivity.class));
                    }
                });
            }
        }


    }