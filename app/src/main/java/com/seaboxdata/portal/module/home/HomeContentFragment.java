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
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.utils.chart.BarChartManager;
import com.seaboxdata.portal.utils.chart.LineChartManager;
import com.seaboxdata.portal.utils.chart.PieChartManager;

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

        for (int j = 0; j <= 4; j++) {
            float value = (float) (Math.random() * 80);
            y1Value.add(value);
            y2Value.add(value-10);
            lableNameList.add("");
        }
        yValues.add(y1Value);
        yValues.add(y2Value);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.BLUE);
        colorList.add(R.color.backgroundGrey);


        // 曲线
        View lineChartView = layoutInflater.inflate(R.layout.item_content_linechart, mContentHomeView, false);
        LineChart mLineChart = (LineChart) lineChartView.findViewById(R.id.lineChart);
        LineChartManager lineChartManager = new LineChartManager(mLineChart);
        lineChartManager.showLineCharDouble(xValues, yValues, lableNameList, colorList);
        lineChartManager.setDescription("");
        mContentHomeView.addView(lineChartView);

        // 柱状
        View barChartView = layoutInflater.inflate(R.layout.item_content_barchart, mContentHomeView, false);
        BarChart barChart1 = (BarChart) barChartView.findViewById(R.id.barChart);

        BarChartManager barChartManager1 = new BarChartManager(barChart1);
        barChartManager1.showBarChart(xValues, yValues.get(0), lableNameList.get(1), colorList.get(1));
        barChartManager1.setDescription("");
        mContentHomeView.addView(barChartView);
        // 饼图

        // 饼图
        View pieChartView = layoutInflater.inflate(R.layout.item_content_piechart, mContentHomeView, false);
        PieChart mPieChart = (PieChart) pieChartView.findViewById(R.id.pieChart);
        List<String> names = new ArrayList<>(); //每个模块的内容描述
        names.add("模块一");
        names.add("模块二");
        names.add("模块三");
        names.add("模块四");
        List<Float> date = new ArrayList<>(); //每个模块的值（占比率）
        date.add(10.3f);
        date.add(20.2f);
        date.add(30.4f);
        date.add(39.1f);
        List<Integer> colors = new ArrayList<>(); //每个模块的颜色
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.DKGRAY);
        //饼状图管理类
        PieChartManager pieChartManager1 = new PieChartManager(mPieChart);
        pieChartManager1.setPieChart(names, date, colors);
        pieChartManager1.setDescription("");
        mContentHomeView.addView(pieChartView);
        /********************图表******************************/

    }

}
