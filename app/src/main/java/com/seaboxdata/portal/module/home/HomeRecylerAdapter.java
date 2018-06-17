package com.seaboxdata.portal.module.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.PieChart;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.module.info.InfomationActivity;
import com.seaboxdata.portal.module.sentiment.SenTimentActivity;
import com.seaboxdata.portal.module.work.WorkAyActivity;
import com.seaboxdata.portal.utils.chart.PieChartManager;
import com.seaboxdata.portal.utils.view.ScrollTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhang on 2018/6/9.
 */

public class HomeRecylerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final  int TYPECOUNT=7;
    public final  int TYPEVIEWPAGER=0;
    public final  int TYPEYWJC=1;
    public final  int TYPECYYQ=2;
    public final  int TYPETZYU=3;
    public final  int TYPEINFO=4;
    public final  int TYPEWORK=5;
    public final  int TYPEYQXX=6;


    private static  Context context;

    public HomeRecylerAdapter(Context context) {
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPEVIEWPAGER:
                View view= LayoutInflater.from(context).inflate(R.layout.home_fragment_viewpage,parent,false);
                return new MyViewholder1(view);

            case TYPEYWJC:
                View ywjc= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_ywjc,null);
                return new MyViewholder2(ywjc);
            case  TYPECYYQ:
                View cyfb= LayoutInflater.from(context).inflate(R.layout.home_fragment_cyfb,parent,false);
                return new MyViewholder3(cyfb);
            case TYPETZYU:
                View czgc= LayoutInflater.from(context).inflate(R.layout.home_fragment_czgc,parent,false);
                return new MyViewholder4(czgc);
            case TYPEINFO:
                View info= LayoutInflater.from(context).inflate(R.layout.home_fragment_info,parent,false);
                return new MyViewholder5(info);
            case TYPEWORK:
                View work= LayoutInflater.from(context).inflate(R.layout.home_fragment_work,parent,false);
                return new MyViewholder6(work);
            case TYPEYQXX:
                View yqinfo= LayoutInflater.from(context).inflate(R.layout.home_fragment_yqinfo,parent,false);
                return new MyViewholder7(yqinfo);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return TYPECOUNT;
    }


    @Override
    public int getItemViewType(int position) {
        switch (position){
            case TYPEVIEWPAGER:
                return TYPEVIEWPAGER;
            case TYPEYWJC:
                return TYPEYWJC;
            case  TYPECYYQ:
                return TYPECYYQ;
            case TYPETZYU:
                return TYPETZYU;
            case TYPEINFO:
                return TYPEINFO;
            case TYPEWORK:
                return TYPEWORK;
            case TYPEYQXX:
                return TYPEYQXX;
            default:
                return TYPEVIEWPAGER;
        }
    }

    private int[] mImages = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner2};
    List<ImageView> mList=new ArrayList<>();
    //ViewPage
    class MyViewholder1 extends RecyclerView.ViewHolder {

        public MyViewholder1(View itemView) {
            super(itemView);
//            List<HomeViewPageBean> alldata=new ArrayList<>();
//            alldata.add(new HomeViewPageBean(R.drawable.banner1,"远航,扬起 上海精神的时代风帆"));
//            alldata.add(new HomeViewPageBean(R.drawable.banner2,"远航,扬起 上海精神的时代风帆"));
//            alldata.add(new HomeViewPageBean(R.drawable.banner3,"远航,扬起 上海精神的时代风帆"));

            final ViewPager viewPager= (ViewPager) itemView.findViewById(R.id.home_viewpage);
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

                    if(currentPosition == viewPager.getAdapter().getCount() - 1){
                        // 最后一页
                        viewPager.setCurrentItem(0);
                    }else{
                        viewPager.setCurrentItem(currentPosition + 1);
                    }

                    // 一直给自己发消息
                    mHandler.postDelayed(this,3000);
                }
            },3000);
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

    //业务检测
    class MyViewholder2 extends RecyclerView.ViewHolder {

        public MyViewholder2(View itemView) {
            super(itemView);
            GridView home_jclist = (GridView) itemView.findViewById(R.id.home_jclist);
            final List<HomeFragmentYwBean> allData=new ArrayList<>();
            allData.add(new HomeFragmentYwBean(R.drawable.yewu_icon_traslate,"城市交通"));
            allData.add(new HomeFragmentYwBean(R.drawable.yewu_icon_en,"环境气象"));
            allData.add(new HomeFragmentYwBean(R.drawable.yewu_icon_mn,"经济运行"));
            allData.add(new HomeFragmentYwBean(R.drawable.yewu_icon_life,"城市生命线"));
            allData.add(new HomeFragmentYwBean(R.drawable.yewu_icon_safe,"公共安全"));
            allData.add(new HomeFragmentYwBean(R.drawable.yewu_icon_cam,"生态环境"));
            allData.add(new HomeFragmentYwBean(R.drawable.yewu_icon_all,"全部"));
            HomeGridViewAdapter adapter=new HomeGridViewAdapter(context,allData);
            home_jclist.setAdapter(adapter);
            home_jclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0) {
                        context.startActivity(new Intent(context,HomeCityActivity.class));
                    }
//                    Toast.makeText(context,""+allData.get(position).getIconName(),Toast.LENGTH_LONG).show();
                }
            });


            TextView home_big_info= (TextView) itemView.findViewById(R.id.home_big_info);
            TextView home_work_ay= (TextView) itemView.findViewById(R.id.home_work_ay);
           TextView home_sentiment= (TextView) itemView.findViewById(R.id.home_sentiment);
            home_work_ay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,WorkAyActivity.class));
                }
            });
            home_big_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,InfomationActivity.class));
                }
            });
            home_sentiment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SenTimentActivity.class));
                }
            });


        }
    }

    //产业分布
    class MyViewholder3 extends RecyclerView.ViewHolder {

        public MyViewholder3(View itemView) {
            super(itemView);
            //设置数据
            /**
             * 第二组数据
             */
             int[] mDataLeftTwo = {50, 30, 40, 55,50};
             int[] mDataRightTwo = {70, 65, 70, 80,76};
             String[] mDataTextXTwo = {"天竺空港", "义庄经济", "中关村", "新机场","燕郊高新"};

            final DoubleLineChatView doubleLineChatViewTwo = (DoubleLineChatView) itemView.findViewById(R.id.line_chat_two);
            doubleLineChatViewTwo.setData(mDataLeftTwo, mDataRightTwo, mDataTextXTwo);
            doubleLineChatViewTwo.start();
//            //设置x轴的数据
//            ArrayList<Float> xValues = new ArrayList<>();
//            for (int i = 0; i <= 4; i++) {
//                xValues.add((float) i);
//            }
//
//            //设置y轴的数据()
//            List<List<Float>> yValues = new ArrayList<>();
//
//            List<Float> y1Value = new ArrayList<>();
//            List<Float> y2Value = new ArrayList<>();
//
//            List<String> lableNameList = new ArrayList<>();
//
//            for (int j = 0; j <= 4; j++) {
//                float value = (float) (Math.random() * 80);
//                y1Value.add(value);
//                y2Value.add(value-10);
//                lableNameList.add("");
//            }
//
//            Log.e("TAG", "========="+y1Value);
//            yValues.add(y1Value);
//            yValues.add(y2Value);
//
//            List<Integer> colorList = new ArrayList<>();
//            colorList.add(R.color.chanye6785f2);
//            colorList.add(R.color.chanye8c77f5);
//
//            BarChart bar= (BarChart) itemView.findViewById(R.id.home_barChart);
//            BarChartManager barChartManager1 = new BarChartManager(bar);
//            barChartManager1.showBarChart(xValues, yValues, lableNameList, colorList);
//            barChartManager1.setDescription("");

        }
    }
    //通州园区
    class MyViewholder4 extends RecyclerView.ViewHolder {

        public MyViewholder4(View itemView) {
            super(itemView);

            PieChart pie_chart_with_line= (PieChart) itemView.findViewById(R.id.home_chart1);
            PieChartManager pieChartManager=new PieChartManager(pie_chart_with_line);
            List<String> name=new ArrayList<>();
            name.add("电子信息");
            name.add("汽车零件");
            name.add("生物医药");
            name.add("先进制造");
            name.add("其他");
            List<Float> num=new ArrayList<>();
            num.add(30.0f);
            num.add(40.0f);
            num.add(50.0f);
            num.add(60.0f);
            num.add(70.0f);
            List<Integer> color=new ArrayList<>();
           color.add(Color.parseColor("#fbd16d"));
           color.add(Color.parseColor("#ff3f60"));
           color.add(Color.parseColor("#ff990a"));
           color.add(Color.parseColor("#c3dd68"));
           color.add(Color.parseColor("#fb8041"));
            pieChartManager.setPieChart(name,num,color);
        }
    }


    //重要信息
    class MyViewholder5 extends RecyclerView.ViewHolder {

        public MyViewholder5(View itemView) {
            super(itemView);
//            ListView home_info_list= (ListView) itemView.findViewById(R.id.home_info_list);
//            List<HomeInfoBean> allData=new ArrayList<>();
//            allData.add(new HomeInfoBean("民航局禁止航班飞行中驾驶员少于2名机组成员","2018-05-28"));
//            allData.add(new HomeInfoBean("重磅! 世界最大物美基地落户北京城市副中心","2018-05-28"));
//
//            home_info_list.setAdapter(new HomeInfoListAdapter(context,allData));
            List<String> allData=new ArrayList<>();
            allData.add("民航局禁止航班飞行中驾驶员少于2名机组成员");
            allData.add("重磅! 世界最大物美基地落户北京城市副中心");
            ScrollTextView scroll_tv= (ScrollTextView) itemView.findViewById(R.id.scroll_tv);
            scroll_tv.setTipList(allData);
        }
    }

    //办公辅助
    class MyViewholder6 extends RecyclerView.ViewHolder {

        public MyViewholder6(View itemView) {
            super(itemView);
        }
    }
    //舆情信息
    class MyViewholder7 extends RecyclerView.ViewHolder {

        public MyViewholder7(View itemView) {
            super(itemView);

            PieChart pieChart2= (PieChart) itemView.findViewById(R.id.home_chart2);
            PieChartManager pieChartManager=new PieChartManager(pieChart2);
            List<String> name=new ArrayList<>();
            name.add("微博");
            name.add("客户端");
            name.add("报刊");
            List<Float> num=new ArrayList<>();
            num.add(70.0f);
            num.add(20.0f);
            num.add(10.0f);
            List<Integer> color=new ArrayList<>();
            color.add(Color.parseColor("#fbd06a"));
            color.add(Color.parseColor("#f6993f"));
            color.add(Color.parseColor("#e71f19"));

            pieChartManager.setSolidPieChart(name,num,color);

            /**
             * 第二组数据
             */
            int[] mDataLeftTwo = {50, 30, 40, 55,50,90};
            int[] mDataRightTwo = {70, 65, 70, 80,76,80};
            String[] mDataTextXTwo = {"全部", "微博", "客户端", "微信","论坛","网站"};

            final DoubleLineChatView doubleLineChatViewTwo = (DoubleLineChatView) itemView.findViewById(R.id.line_chat_one);
            doubleLineChatViewTwo.setData(mDataLeftTwo, mDataRightTwo, mDataTextXTwo);
            doubleLineChatViewTwo.start();
        }
    }
}
