package com.seaboxdata.portal.module.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.linewell.core.view.AutoFixViewGroup;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.bean.DateInfo;
import com.seaboxdata.portal.bean.OrderInfo;
import com.seaboxdata.portal.bean.RoomInfo;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.PopWindowUtil;
import com.seaboxdata.portal.utils.chart.LineChartManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * 应用详情页
 * Created by zjianning on 2018/5/15.
 */

public class DetailActivity extends CommonActivity {

    @BindView(R.id.centerTitle_iv)
    ImageView imageView;

    @BindView(R.id.centerTitle)
    View view;

    private boolean isExpand = false;

    private PopupWindow mPopupWindow;

    private PopWindowUtil popWindowUtil;

    @BindView(R.id.backgroud_view)
    View backgroudView;

    private TextView curSelectCatoryTV;


    public static void startAction(Activity activity, String title) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(KEY_TITLE,title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);

        setCenterTitle(getIntent().getStringExtra(KEY_TITLE));
        initView();

        initData();

        bindView();

    }

    private void bindView() {

        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.down);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isExpand) {
                    // 收缩
                    isExpand = false;
                    imageView.setImageResource(R.drawable.down);
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    backgroudView.setVisibility(View.GONE);
                } else {
                    // 展开
                    isExpand = true;
                    imageView.setImageResource(R.drawable.up);

                    if (mPopupWindow == null) {

                        LayoutInflater inflater = LayoutInflater.from(mCommonContext);

                        // 引入窗口配置文件
                        View view = inflater.inflate(R.layout.pop_category, null);

                        AutoFixViewGroup autoFixViewGroup = (AutoFixViewGroup)view.findViewById(R.id.pop_category_root_view);
                        autoFixViewGroup.removeAllViews();
                        // TODO
                        // 循环获取数据
                        // 需要设置当前选中的样式
                        for (int i=0; i<5; i++) {
                            View popCategoryView = inflater.inflate(R.layout.item_pop_category,
                                    autoFixViewGroup, false);
                            final TextView nameTV = (TextView) popCategoryView.findViewById(R.id.item_pop_category_name);
                            nameTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (curSelectCatoryTV != null) {
                                        curSelectCatoryTV.setSelected(false);
                                    }

                                    nameTV.setSelected(true);
                                    setCenterTitle(nameTV.getText().toString());
                                    curSelectCatoryTV = nameTV;

                                    mPopupWindow.dismiss();
                                }
                            });
                            autoFixViewGroup.addView(popCategoryView);

                        }

                        popWindowUtil = PopWindowUtil.getInstance().makePopupWindow(mCommonActivity,  view);
                        popWindowUtil.setOnDissmissListener(new PopWindowUtil.OnDissmissListener() {
                            @Override
                            public void dissmiss() {
                                isExpand = false;
                                imageView.setImageResource(R.drawable.down);
                                backgroudView.setVisibility(View.GONE);
                            }
                        });
                    }

                    mPopupWindow = popWindowUtil.showLocationWithAnimation(mCommonActivity,
                            findViewById(R.id.toolbar), 0,0);

                    backgroudView.setVisibility(View.VISIBLE);

                }


            }
        });

    }

    private void initData() {



    }

    private void initView() {

        LineChart mLineChart = (LineChart) findViewById(R.id.lineChart);

        //设置数据

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();

        List<Float> y1Value = new ArrayList<>();
        List<Float> y2Value = new ArrayList<>();

        List<String> lableNameList = new ArrayList<>();
        lableNameList.add("交通运行");
        lableNameList.add("拥挤指数");
        for (int j = 0; j <= 4; j++) {
            float value = (float) (Math.random() * 80);
            y1Value.add(value);
            y2Value.add(value-10);

        }
        yValues.add(y1Value);
        yValues.add(y2Value);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.BLUE);
        colorList.add(Color.DKGRAY);

        // 曲线
        LineChartManager lineChartManager = new LineChartManager(mLineChart);
        lineChartManager.showLineChart(xValues, yValues, lableNameList, colorList);

        lineChartManager.setDescription("");


        // 表格
        DataScrollablePanel scrollablePanel = (DataScrollablePanel) findViewById(R.id.scrollable_panel);

        scrollablePanel.getContentRecyclerView().setHasFixedSize(true);
        scrollablePanel.getContentRecyclerView().setNestedScrollingEnabled(false);

        ScrollablePanelAdapter scrollablePanelAdapter = new ScrollablePanelAdapter();
        generateTestData(scrollablePanelAdapter);
        scrollablePanel.setPanelAdapter(scrollablePanelAdapter);

    }

    public static final SimpleDateFormat DAY_UI_MONTH_DAY_FORMAT = new SimpleDateFormat("MM-dd");
    public static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("EEE", Locale.SIMPLIFIED_CHINESE);

    private void generateTestData(ScrollablePanelAdapter scrollablePanelAdapter) {
        List<RoomInfo> roomInfoList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomType("SUPK");
            roomInfo.setRoomId(i);
            roomInfo.setRoomName("20" + i);
            roomInfoList.add(roomInfo);
        }
        scrollablePanelAdapter.setRoomInfoList(roomInfoList);

        List<DateInfo> dateInfoList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 10; i++) {
            DateInfo dateInfo = new DateInfo();
            String date = DAY_UI_MONTH_DAY_FORMAT.format(calendar.getTime());
            String week = WEEK_FORMAT.format(calendar.getTime());
            dateInfo.setDate(date);
            dateInfo.setWeek(week);
            dateInfoList.add(dateInfo);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        scrollablePanelAdapter.setDateInfoList(dateInfoList);

        List<List<OrderInfo>> ordersList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            List<OrderInfo> orderInfoList = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setGuestName("NO." + i + j);
                orderInfo.setBegin(true);
                orderInfo.setStatus(OrderInfo.Status.randomStatus());
//                if (orderInfoList.size() > 0) {
//                    OrderInfo lastOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
//                    if (orderInfo.getStatus().ordinal() == lastOrderInfo.getStatus().ordinal()) {
//                        orderInfo.setId(lastOrderInfo.getId());
//                        orderInfo.setBegin(false);
//                        orderInfo.setGuestName("");
//                    } else {
//                        if (new Random().nextBoolean()) {
//                            orderInfo.setStatus(OrderInfo.Status.BLANK);
//                        }
//                    }
//                }
                orderInfoList.add(orderInfo);
            }
            ordersList.add(orderInfoList);
        }
        scrollablePanelAdapter.setOrdersList(ordersList);
    }

}
