package com.seaboxdata.portal.module.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.utils.chart.BarChartManager;
import com.seaboxdata.portal.utils.chart.LineChartManager;
import com.seaboxdata.portal.utils.chart.PieChartManager;
import com.seaboxdata.portal.utils.chart.RadarChartManager;
import com.seaboxdata.portal.utils.view.segmentedbar.Segment;
import com.seaboxdata.portal.utils.view.segmentedbar.SegmentedBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zjianning on 2018/5/11.
 */

public class HomeContentFragment extends CommonFragment {

    private View view;

    @BindView(R.id.content_home_view)
    LinearLayout mContentHomeView;

    private int mIndex = 1;

    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content_home, container, false);

        unbinder = ButterKnife.bind(this, view);

        mIndex = getArguments().getInt(KEY_DATA);

        initView(view);

        initData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initData() {

    }

    private void initView(View view) {

        mContentHomeView.removeAllViews();

        // 重要指标
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        int layoutId = R.layout.item_city_traffic;
        switch (mIndex) {
            case 1:
                layoutId = R.layout.item_city_traffic;
                break;
            case 2:
                layoutId = R.layout.item_economics_running;
                break;
            case 3:
                layoutId = R.layout.item_livelihood;
                break;
            case 4:
                layoutId = R.layout.item_environment_weather;
                break;
        }


        View cityTrafficView = layoutInflater.inflate(layoutId, mContentHomeView, false);
        cityTrafficView.findViewById(R.id.content_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.startAction(mActivity, "交通");
            }
        });

        mContentHomeView.addView(cityTrafficView);

        /********************图表******************************/
        //设置数据
        if(mIndex==4){
            // 北京热力地图
            View lineChartView = layoutInflater.inflate(R.layout.item_content_map, mContentHomeView, false);
            mContentHomeView.addView(lineChartView);

            //空气KOI
            View airKoi = layoutInflater.inflate(R.layout.item_content_koi, mContentHomeView, false);
            LineChart mLineChart = (LineChart) airKoi.findViewById(R.id.lineChart);
            LineChartManager lineChartManager = new LineChartManager(mLineChart);
            List<Float> xValues=new ArrayList<>();
            List<Float> y1Value=new ArrayList<>();
            for (int i=0;i<16;i++){
                xValues.add((float) i);
                y1Value.add((float) (Math.random()*80));
            }
            lineChartManager.showAirLineChart(xValues, y1Value, "", Color.parseColor("#da6268"));
            lineChartManager.setDescription("");
            mContentHomeView.addView(airKoi);

            //实时空气质量指数
            showAqi(mContentHomeView,layoutInflater);

            //温度和湿度指数
            showDoubleLine(mContentHomeView,layoutInflater);

        }


        if (mIndex == 1||mIndex==2||mIndex==3) {
            ArrayList<Float> xValues = new ArrayList<>();
            xValues.add(0.0f);
            xValues.add(1.0f);
            xValues.add(2.0f);
            xValues.add(3.0f);
            xValues.add(4.0f);
            xValues.add(5.0f);

            //设置y轴的数据()
            List<List<Float>> yValues = new ArrayList<>();

            List<Float> y1Value = new ArrayList<>();
            List<Float> y2Value = new ArrayList<>();

            List<String> lableNameList = new ArrayList<>();

            for (int j = 0; j <= 6; j++) {
                float value = (float) (Math.random() * 10);
                y1Value.add(value);
                y2Value.add(value - 10);
                lableNameList.add("" + j);
            }
            yValues.add(y1Value);
            yValues.add(y2Value);

            List<Integer> colorList = new ArrayList<>();
            colorList.add(Color.parseColor("#5ad2b3"));
            colorList.add(R.color.backgroundGrey);


            // 曲线
            View lineChartView = layoutInflater.inflate(R.layout.item_content_linechart, mContentHomeView, false);
            LineChart mLineChart = (LineChart) lineChartView.findViewById(R.id.lineChart);
            LineChartManager lineChartManager = new LineChartManager(mLineChart);
            lineChartManager.showLineChart(xValues, y1Value, "", Color.parseColor("#da6268"));
            lineChartManager.setDescription("");
            mContentHomeView.addView(lineChartView);

            ArrayList<Float> xValuesbarChart = new ArrayList<>();
            List<Float> y3Value = new ArrayList<>();
            for (int j = 0; j <= 6; j++) {
                xValuesbarChart.add(Float.parseFloat("" + j));
                float value = (float) (Math.random() * 10);
                y3Value.add(value);
                lableNameList.add("");
            }


            // 柱状
            View barChartView = layoutInflater.inflate(R.layout.item_content_barchart, mContentHomeView, false);
            BarChart barChart1 = (BarChart) barChartView.findViewById(R.id.barChart);

            BarChartManager barChartManager1 = new BarChartManager(barChart1);
            barChartManager1.showBarChart(xValuesbarChart, y3Value, "", Color.parseColor("#fbd06a"));
            barChartManager1.setDescription("");
            mContentHomeView.addView(barChartView);
            // 饼图


            // 饼图
            View pieChartView = layoutInflater.inflate(R.layout.item_content_piechart, mContentHomeView, false);
            PieChart pieChart2 = (PieChart) pieChartView.findViewById(R.id.home_chart2);
            PieChartManager pieChartManager = new PieChartManager(pieChart2);
            List<String> name = new ArrayList<>();
            name.add("通讯网");
            name.add("供电线路");
            name.add("公路");
            name.add("供水线路");
            List<Float> num = new ArrayList<>();
            num.add(70.0f);
            num.add(20.0f);
            num.add(10.0f);
            num.add(10.0f);
            List<Integer> color = new ArrayList<>();
            color.add(Color.parseColor("#fbd06a"));
            color.add(Color.parseColor("#f6993f"));
            color.add(Color.parseColor("#e71f19"));
            color.add(Color.parseColor("#ff5d52"));

            pieChartManager.setTrafficSolidPieChart(name, num, color);

            mContentHomeView.addView(pieChartView);


            //雷达图
            View scatterChartView = layoutInflater.inflate(R.layout.item_content_scatter, mContentHomeView, false);
            RadarChart radarChart = (RadarChart) scatterChartView.findViewById(R.id.readerChart);
            RadarChartManager radarChartManager = new RadarChartManager(getActivity(), radarChart);
            List<String> xData = new ArrayList<>();
            List<List<Float>> yDatas = new ArrayList<>();
            List<String> names = new ArrayList<>();
            names.add("东城区");
            names.add("朝阳区");
            names.add("西城");
            names.add("丰台区");
            names.add("石景山");
            names.add("房山区");

            xData.add("总体");
            xData.add("交通设施");
            xData.add("评价");
            xData.add("平均");
            xData.add("交通治理");
            xData.add("交通需求");
            xData.add("交通拥堵指数");
            List<Integer> colors = new ArrayList<>();

            for (int i = 0; i < 6; i++) {
                List<Float> nData = new ArrayList<>();
                for (int j = 0; j < 7; j++) {
                    nData.add((float) (Math.random() * 20));
                }
                yDatas.add(nData);
            }

            colors.add(Color.parseColor("#fbd06a"));
            colors.add(Color.parseColor("#f69a40"));
            colors.add(Color.parseColor("#ff5d52"));
            colors.add(Color.parseColor("#e71f19"));
            colors.add(Color.parseColor("#ff9b43"));
            colors.add(Color.parseColor("#8eb9fb"));
            radarChartManager.showRadarChart(xData, yDatas, names, colors);

            mContentHomeView.addView(scatterChartView);
        }
//        PieChart mPieChart = (PieChart) pieChartView.findViewById(R.id.pieChart);
//        List<String> names = new ArrayList<>(); //每个模块的内容描述
//        names.add("模块一");
//        names.add("模块二");
//        names.add("模块三");
//        names.add("模块四");
//        List<Float> date = new ArrayList<>(); //每个模块的值（占比率）
//        date.add(10.3f);
//        date.add(20.2f);
//        date.add(30.4f);
//        date.add(39.1f);
//        List<Integer> colors = new ArrayList<>(); //每个模块的颜色
//        colors.add(Color.RED);
//        colors.add(Color.GREEN);
//        colors.add(Color.BLUE);
//        colors.add(Color.DKGRAY);
//        //饼状图管理类
//        PieChartManager pieChartManager1 = new PieChartManager(mPieChart);
//        pieChartManager1.setPieChart(names, date, colors);
//        pieChartManager1.setDescription("");

        /********************图表******************************/
    }

    //空气质量指数
    private void showAqi(LinearLayout mContentHomeView, LayoutInflater layoutInflater) {
        View apiView = layoutInflater.inflate(R.layout.item_content_aqi, mContentHomeView, false);
        final SegmentedBarView barView = (SegmentedBarView) apiView.findViewById(R.id.slider_type_bar_view_oil3);
        int colors[] = new int[]{Color.parseColor("#6bce05"), Color.parseColor("#fbd128"), Color.parseColor("#fe8801"), Color.parseColor("#fe0200"),Color.parseColor("#980445"), Color.parseColor("#62001d")};
        ArrayList<Segment> segments = new ArrayList<>();
        Segment segment1 = new Segment(0.0f, 10.0f, "", colors[0]).setDescriptionText("0").setTopDescriptionText("缺油");
        segments.add(segment1);
        //中间显示用40%&60%  “&” 分割
        Segment segment2 = new Segment(10.0f, 20.0f, "", colors[1]).setDescriptionText("50&100").setTopDescriptionText("正常");
        segments.add(segment2);
        Segment segment3 = new Segment(20.0f, 30.0f, "", colors[2]).setDescriptionText("&150").setTopDescriptionText("偏油");
        segments.add(segment3);
        Segment segment4 = new Segment(30.0f, 40.0f, "", colors[3]).setDescriptionText("&200").setTopDescriptionText("严重");
        segments.add(segment4);
        Segment segment5 = new Segment(40.0f, 60.0f, "", colors[4]).setDescriptionText("&300").setTopDescriptionText("超严重");
        segments.add(segment5);
        Segment segment6 = new Segment(60.0f, 100.0f, "", colors[5]).setDescriptionText("500").setTopDescriptionText("糟糕");
        segments.add(segment6);
        barView.setShowDescriptionText(true);
        barView.setValue(10.0f);
        barView.setSegments(segments);
        mContentHomeView.addView(apiView);
    }


    //温度和湿度指数
    private void showDoubleLine(LinearLayout mContentHomeView, LayoutInflater layoutInflater) {


        View view1 = layoutInflater.inflate(R.layout.item_content_temperature, mContentHomeView, false);
        LineChart doubleLineChart = (LineChart) view1.findViewById(R.id.lineChartdouble);
        LineChartManager lineChartManagers = new LineChartManager(doubleLineChart);

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
        lableNameList.add("温度");
        lableNameList.add("湿度");

        for (int j = 0; j <= 4; j++) {
            float value = (float) (Math.random() * 80);
            y1Value.add(value);
            y2Value.add(value-5);
        }
        yValues.add(y1Value);
        yValues.add(y2Value);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#eecc44"));
        colorList.add(Color.parseColor("#78c726"));

        // 曲线
        lineChartManagers.showLineCharDouble(xValues, yValues, lableNameList, colorList);

        lineChartManagers.setDescription("");
        mContentHomeView.addView(view1);
    }

}
