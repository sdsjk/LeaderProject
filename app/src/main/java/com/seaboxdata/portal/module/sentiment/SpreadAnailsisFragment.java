package com.seaboxdata.portal.module.sentiment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.utils.chart.BarChartManager;
import com.seaboxdata.portal.utils.chart.PieChartManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2018/6/17.
 */

public class SpreadAnailsisFragment extends CommonFragment {
    View view;
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.analysis_apread,null);
        initView();
        return view;
    }

    private void initView() {

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
            y2Value.add(value-10);
            lableNameList.add(""+j);
        }
        yValues.add(y1Value);
        yValues.add(y2Value);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#5ad2b3"));
        colorList.add(R.color.backgroundGrey);

        ArrayList<Float> xValuesbarChart = new ArrayList<>();
        List<Float> y3Value = new ArrayList<>();
        for (int j = 0; j <= 6; j++) {
            xValuesbarChart.add(Float.parseFloat(""+j));
            float value = (float) (Math.random() * 10);
            y3Value.add(value);
            lableNameList.add("");
        }
        // 柱状
        BarChart barChart1 = (BarChart) view.findViewById(R.id.barChart);
        BarChartManager barChartManager1 = new BarChartManager(barChart1);
        barChartManager1.showBarChart(xValuesbarChart, y3Value, "", Color.parseColor("#6785f2"));
        barChartManager1.setDescription("");








        PieChart pie_chart_with_line= (PieChart) view.findViewById(R.id.home_chart1);
        PieChartManager pieChartManager=new PieChartManager(pie_chart_with_line);
        List<String> name=new ArrayList<>();
        name.add("新浪微博");
        name.add("微信");
        name.add("搜狐网");
        name.add("百家号");
        name.add("微信棒");
        name.add("凤凰娱乐");
        name.add("突袭新闻");
        name.add("天天快报");
        List<Float> num=new ArrayList<>();
        num.add(30.0f);
        num.add(10.0f);
        num.add(30.0f);
        num.add(30.0f);
        num.add(40.0f);
        num.add(50.0f);
        num.add(60.0f);
        num.add(70.0f);
        List<Integer> color=new ArrayList<>();
        color.add(Color.parseColor("#a1a1a1"));
        color.add(Color.parseColor("#f3aa5b"));
        color.add(Color.parseColor("#bc4b23"));
        color.add(Color.parseColor("#b47c4b"));
        color.add(Color.parseColor("#8a522d"));

        color.add(Color.parseColor("#6e727d"));
        color.add(Color.parseColor("#4ca258"));
        color.add(Color.parseColor("#35623a"));
        pieChartManager.setPieChart(name,num,color);


    }
}
