package com.seaboxdata.portal.module.sentiment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.linewell.core.view.AutoFixViewGroup;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.module.home.DoubleLineChatView;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;
import com.seaboxdata.portal.utils.chart.PieChartManager;

import java.util.ArrayList;
import java.util.List;

public class SenTimentActivity extends CommonActivity implements View.OnClickListener, PopupWindow.OnDismissListener {

    RelativeLayout choose_time, choose_hot, choose_kind;
    ImageView time_icon, hot_icon, kind_icon, infomation_back;
    boolean timeFlag, hotFlag, kindFlag;
    TextView info_title_common, info_title_common_commit,sendtimend_more;
    PopupWindow timePopupWindow = null;
    PopupWindow hotPopupWindow = null;
    PopupWindow kindPopupWindow = null;

    private static final int TIMETYPE = 1;
    private static final int HOTTYPE = 2;
    private static final int KINDTYPE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sen_timent);
        setToolBar();

        initView();
    }

    private void initView() {
        choose_time = (RelativeLayout) findViewById(R.id.choose_time);
        choose_hot = (RelativeLayout) findViewById(R.id.choose_hot);
        choose_kind = (RelativeLayout) findViewById(R.id.choose_kind);
        sendtimend_more= (TextView) findViewById(R.id.sendtimend_more);
        sendtimend_more.setOnClickListener(this);
        choose_time.setOnClickListener(this);
        choose_hot.setOnClickListener(this);
        choose_kind.setOnClickListener(this);
        infomation_back = (ImageView) findViewById(R.id.infomation_back);
        infomation_back.setImageResource(R.drawable.home_search);
        infomation_back.setOnClickListener(this);
        info_title_common = (TextView) findViewById(R.id.info_title_common);
        info_title_common.setText("舆情信息");
        info_title_common_commit = (TextView) findViewById(R.id.info_title_common_commit);
        info_title_common_commit.setVisibility(View.VISIBLE);
        info_title_common_commit.setOnClickListener(this);
        info_title_common_commit.setText("制作简报");
        time_icon = (ImageView) findViewById(R.id.time_icon);
        hot_icon = (ImageView) findViewById(R.id.hot_icon);
        kind_icon = (ImageView) findViewById(R.id.kind_icon);

        PieChart pieChart2 = (PieChart) findViewById(R.id.home_chart2);
        PieChartManager pieChartManager = new PieChartManager(pieChart2);
        List<String> name = new ArrayList<>();
        name.add("微博");
        name.add("客户端");
        name.add("报刊");
        List<Float> num = new ArrayList<>();
        num.add(70.0f);
        num.add(20.0f);
        num.add(10.0f);
        List<Integer> color = new ArrayList<>();
        color.add(Color.parseColor("#6785f2"));
        color.add(Color.parseColor("#c363fa"));
        color.add(Color.parseColor("#6f3ded"));

        pieChartManager.setSolidPieChart(name, num, color);


        /**
         * 第二组数据
         */
        int[] mDataLeftTwo = {50, 30, 40, 55, 50, 90};
        int[] mDataRightTwo = {70, 65, 70, 80, 76, 80};
        String[] mDataTextXTwo = {"全部", "微博", "客户端", "微信", "论坛", "网站"};

        final DoubleLineChatView doubleLineChatViewTwo = (DoubleLineChatView) findViewById(R.id.line_chat_one);
        doubleLineChatViewTwo.setData(mDataLeftTwo, mDataRightTwo, mDataTextXTwo);
        doubleLineChatViewTwo.start();


        PieChart pie_chart_with_line = (PieChart) findViewById(R.id.home_chart1);
        PieChartManager pieChartManagers = new PieChartManager(pie_chart_with_line);
        List<String> names = new ArrayList<>();
        names.add("电子信息");
        names.add("汽车零件");
        names.add("生物医药");
        names.add("先进制造");
        names.add("其他");
        List<Float> nums = new ArrayList<>();
        nums.add(30.0f);
        nums.add(40.0f);
        nums.add(50.0f);
        nums.add(60.0f);
        nums.add(70.0f);
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#c363fa"));
        colors.add(Color.parseColor("#f5a658"));
        colors.add(Color.parseColor("#755af2"));
        colors.add(Color.parseColor("#496cef"));
        colors.add(Color.parseColor("#eecc44"));
        pieChartManagers.setPieChart(names, nums, colors);

    }


    private void setToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            linear_bar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            linear_bar.getLayoutParams().height = getStatusBarHeight();

            if (StatusBarUtil.StatusBarLightMode(this) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == choose_time) {
            showTimePopuWindow();
        }
        if (v == choose_hot) {
            showHotPopuWindow();
        }
        if (v == choose_kind) {
            showkindWindow();
            }
        if (v == infomation_back) {

//            finish();
//                startActivity(new Intent(this,SentimetSearcheActivity.class));
                startActivity(new Intent(this,SentimedtAnalysisActivity.class));

        }
        if (v == info_title_common_commit) {
//            Toast.makeText(SenTimentActivity.this, "制作简报", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,BriefMakeActivity.class));
        }
        if(v==sendtimend_more){
            startActivity(new Intent(this,SentimentMoreActivity.class));
        }
    }

    private void showkindWindow() {
        changeIconStatus(KINDTYPE);
        if (kindPopupWindow == null) {
            kindPopupWindow = new PopupWindow(this);
        }
        if (kindPopupWindow.isShowing()) {
            return;
        }
        View contentView = LayoutInflater.from(this).inflate(R.layout.kind_popuwindow, null);
        kindPopupWindow.setContentView(contentView);
        kindPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        kindPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        kindPopupWindow.setOutsideTouchable(true);
        kindPopupWindow.setOnDismissListener(this);
        kindPopupWindow.setBackgroundDrawable(new ColorDrawable());
        kindPopupWindow.showAsDropDown(choose_time);
        setBackgroundAlpha(0.5f);

    }

    private void showHotPopuWindow() {
        changeIconStatus(HOTTYPE);
        if (hotPopupWindow == null) {
            hotPopupWindow = new PopupWindow(this);
        }
        if (hotPopupWindow.isShowing()) {
            return;
        }
        View contentView = LayoutInflater.from(this).inflate(R.layout.hot_popuwindow, null);
        hotPopupWindow.setContentView(contentView);
        hotPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        hotPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        hotPopupWindow.setOutsideTouchable(true);
        hotPopupWindow.setOnDismissListener(this);

        AutoFixViewGroup autoFixViewGroup = (AutoFixViewGroup) contentView.findViewById(R.id.hot_text);
        Button hot_more = (Button) contentView.findViewById(R.id.hot_more);
        hot_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(SenTimentActivity.this, "更多热词搜索", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SenTimentActivity.this,SentimentHotWordActivity.class));
                hotPopupWindow.dismiss();
                setBackgroundAlpha(1);
            }
        });

        List<String> list = new ArrayList<>();
        list.add("宝贝上亿");
        list.add("陈都灵胖了");
        list.add("C罗女友");
        list.add("詹姆斯");
        list.add("科比");
        list.add("世界杯首个乌龙球");
        list.add("偶遇范冰冰范丞丞");
        list.add("葡萄牙3-3西班牙");
        list.add("苹果侵犯高通专利");
        list.add("端午小长假");
        list.add("赵旭日为C罗颁奖");
        list.add("北京高考阅卷过半");

        showHotKeyword(list,autoFixViewGroup);
        //设置popupwindow背景
        hotPopupWindow.setBackgroundDrawable(new ColorDrawable());
        hotPopupWindow.showAsDropDown(choose_time);
        setBackgroundAlpha(0.5f);

    }
    private void showHotKeyword(List<String> list, AutoFixViewGroup autoFixViewGroup) {
        if(list==null||list.size()==0){
            return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        for(final String item:list){
            View view = layoutInflater.inflate(R.layout.item_hot, autoFixViewGroup, false);
            TextView textView = (TextView) view.findViewById(R.id.hot_keyword);
            textView.setText(item);
            autoFixViewGroup.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private void showTimePopuWindow() {
        changeIconStatus(TIMETYPE);
        if (timePopupWindow == null) {
            timePopupWindow = new PopupWindow(this);
        }
        if (timePopupWindow.isShowing()) {
            return;
        }
        View contentView = LayoutInflater.from(this).inflate(R.layout.time_popuwindow, null);
        timePopupWindow.setContentView(contentView);
        timePopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        timePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        timePopupWindow.setOutsideTouchable(true);
        timePopupWindow.setOnDismissListener(this);
        ListView time_listview = (ListView) contentView.findViewById(R.id.time_listview);
        final List<String> allData = new ArrayList<>();
        allData.add("今天");
        allData.add("24小时");
        allData.add("2天");
        allData.add("3天");
        allData.add("7天");
        allData.add("10天");
        allData.add("1月");
        time_listview.setAdapter(new TimeAdapter(allData, this));
        time_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timePopupWindow.dismiss();
                setBackgroundAlpha(1);
                Toast.makeText(SenTimentActivity.this, allData.get(position) + "", Toast.LENGTH_SHORT).show();
            }
        });

        //设置popupwindow背景
        timePopupWindow.setBackgroundDrawable(new ColorDrawable());
        timePopupWindow.showAsDropDown(choose_time);
        setBackgroundAlpha(0.5f);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private void changeIconStatus(int type) {
        if (!timeFlag) {
            if (type == TIMETYPE) {
                time_icon.setImageResource(R.drawable.down);
            }
            if (type == HOTTYPE) {
                hot_icon.setImageResource(R.drawable.down);
            }
            if (type == KINDTYPE) {
                kind_icon.setImageResource(R.drawable.down);
            }


        } else {
            if (type == TIMETYPE) {
                time_icon.setImageResource(R.drawable.up);
            }
            if (type == HOTTYPE) {
                hot_icon.setImageResource(R.drawable.up);
            }
            if (type == KINDTYPE) {
                kind_icon.setImageResource(R.drawable.up);
            }
        }
        timeFlag = !timeFlag;
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    class TimeAdapter extends BaseAdapter {
        List<String> mAlldata;
        Context mContext;

        public TimeAdapter(List<String> allData, Context context) {
            mAlldata = allData;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mAlldata.size();
        }

        @Override
        public Object getItem(int position) {
            return mAlldata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.time_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.time_item_tv = (TextView) convertView.findViewById(R.id.time_item_tvs);
                viewHolder.time_item_image = (ImageView) convertView.findViewById(R.id.time_item_image);
                viewHolder.time_items = convertView.findViewById(R.id.time_items);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }

            viewHolder.time_item_tv.setText(mAlldata.get(position));
            return convertView;
        }

        class ViewHolder {
            private TextView time_item_tv;
            private ImageView time_item_image;
            private View time_items;


        }

    }
}
