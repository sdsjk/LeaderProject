package com.seaboxdata.portal.module.info;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.module.home.HomeCityActivity;
import com.seaboxdata.portal.module.home.HomeFragmentYwBean;
import com.seaboxdata.portal.module.home.HomeGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2018/6/12.
 */

public class InfoationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;

    public InfoationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(context).inflate(R.layout.home_fragment_viewpage, parent, false);
                return new MyViewholder1(view);
            case 1:
//                View ywjc = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_ywjc, parent, false);
                View ywjc = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_gride_view, parent, false);
                return new MyViewholder2(ywjc);
            case 2:
                View info_top = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_top, parent, false);
                return new MyViewholder3(info_top);
            case 3:
                View info_top_image = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_top_image, parent, false);
                return new MyViewholder4(info_top_image);
            case 4:
                View info_top_image_nohot = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_top_image_nohot, parent, false);
                return new MyViewholder5(info_top_image_nohot);
            case 5:
                View info_nomal = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_nomal, parent, false);
                return new MyViewholder6(info_nomal);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            default:
                return 5;
        }
    }

    private int[] mImages = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner2};
    List<ImageView> mList = new ArrayList<>();

    class MyViewholder1 extends RecyclerView.ViewHolder {

        public MyViewholder1(View itemView) {
            super(itemView);

            final ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.home_viewpage);
            final LinearLayout pointGroup = (LinearLayout) itemView.findViewById(R.id.pointgroup);
            // 准备显示的图片集合
            mList = new ArrayList<>();
            for (int i = 0; i < mImages.length; i++) {
                ImageView imageView = new ImageView(context);
                // 将图片设置到ImageView控件上
                imageView.setImageResource(mImages[i]);
                // 将ImageView控件添加到集合
                mList.add(imageView);
                // 制作底部小圆点
                ImageView pointImage = new ImageView(context);
                pointImage.setImageResource(R.drawable.shape_point_selector);
                // 设置小圆点的布局参数
                int PointSize = context.getResources().getDimensionPixelSize(R.dimen.point_size);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PointSize, PointSize);

                if (i > 0) {
                    params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.point_margin);
                    pointImage.setSelected(false);
                } else {
                    pointImage.setSelected(true);
                }
                pointImage.setLayoutParams(params);

                // 添加到容器里
                pointGroup.addView(pointImage);
            }
            viewPager.setAdapter(new MyAdapter());
            // 对ViewPager设置滑动监听
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                int lastPosition;

                @Override
                public void onPageSelected(int position) {
                    // 页面被选中
                    // 修改position
                    position = position % mList.size();

                    // 设置当前页面选中
                    pointGroup.getChildAt(position).setSelected(true);
                    // 设置前一页不选中
                    pointGroup.getChildAt(lastPosition).setSelected(false);

                    // 替换位置
                    lastPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int currentPosition = viewPager.getCurrentItem();

                    if (currentPosition == viewPager.getAdapter().getCount() - 1) {
                        // 最后一页
                        viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(currentPosition + 1);
                    }

                    // 一直给自己发消息
                    mHandler.postDelayed(this, 3000);
                }
            }, 3000);
        }
    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // 返回整数的最大值
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // return super.instantiateItem(container, position);
            // 修改position
            position = position % mList.size();
            // 将图片控件添加到容器
            container.addView(mList.get(position));

            // 返回
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    class MyViewholder2 extends RecyclerView.ViewHolder {

        public MyViewholder2(View itemView) {
            super(itemView);
            GridView home_jclist = (GridView) itemView.findViewById(R.id.home_jclist);
            final List<HomeFragmentYwBean> allData = new ArrayList<>();
            allData.add(new HomeFragmentYwBean(R.drawable.info_safe, "公共安全"));
            allData.add(new HomeFragmentYwBean(R.drawable.info_hot, "热点问题"));
            allData.add(new HomeFragmentYwBean(R.drawable.info_zhengwu, "政务信息"));
            allData.add(new HomeFragmentYwBean(R.drawable.info_yearstaday, "昨日市情"));
            allData.add(new HomeFragmentYwBean(R.drawable.info_geijing, "北京信息"));
            HomeGridViewAdapter adapter = new HomeGridViewAdapter(context, allData);
            home_jclist.setAdapter(adapter);
            home_jclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(context,""+allData.get(position).getIconName(),Toast.LENGTH_LONG).show();
                      context.startActivity(new Intent(context,InfoListActivity.class));
                }
            });
        }
    }

    class MyViewholder3 extends RecyclerView.ViewHolder {

        public MyViewholder3(View itemView) {
            super(itemView);

        }
    }
    class MyViewholder4 extends RecyclerView.ViewHolder {

        public MyViewholder4(View itemView) {
            super(itemView);

        }
    }
    class MyViewholder5 extends RecyclerView.ViewHolder {

        public MyViewholder5(View itemView) {
            super(itemView);

        }
    }
    class MyViewholder6 extends RecyclerView.ViewHolder {

        public MyViewholder6(View itemView) {
            super(itemView);

        }
    }

}
