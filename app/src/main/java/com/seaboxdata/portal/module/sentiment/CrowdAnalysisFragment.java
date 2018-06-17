package com.seaboxdata.portal.module.sentiment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.utils.chart.PieChartManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2018/6/17.
 */

public class CrowdAnalysisFragment extends CommonFragment {
    View view;
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.analysis_crowd,null);

        intiView();
        return view;
    }

    private void intiView() {
        PieChart pie_chart_with_line= (PieChart) view.findViewById(R.id.home_chart1);
        PieChartManager pieChartManager=new PieChartManager(pie_chart_with_line);
        List<String> name=new ArrayList<>();
        name.add("本科");
        name.add("硕士");
        name.add("博士");
        name.add("大专");
        name.add("其他");
        List<Float> num=new ArrayList<>();
        num.add(30.0f);
        num.add(40.0f);
        num.add(50.0f);
        num.add(60.0f);
        num.add(70.0f);
        List<Integer> color=new ArrayList<>();
        color.add(Color.parseColor("#aa63fa"));
        color.add(Color.parseColor("#f5a658"));
        color.add(Color.parseColor("#675cf2"));
        color.add(Color.parseColor("#496cef"));
        color.add(Color.parseColor("#f3aa5b"));
        pieChartManager.setPieChart(name,num,color);




        PieChart pieChart2= (PieChart) view.findViewById(R.id.home_chart2);
        PieChartManager pieChartManagers=new PieChartManager(pieChart2);
        List<String> names=new ArrayList<>();
        names.add("汉族");
        names.add("回族");
        names.add("壮族");
        names.add("维吾尔族");
        names.add("土家族");
        List<Float> nums=new ArrayList<>();
        nums.add(70.0f);
        nums.add(20.0f);
        nums.add(10.0f);
        nums.add(10.0f);
        nums.add(10.0f);
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.parseColor("#58a9f5"));
        colors.add(Color.parseColor("#aa63fa"));
        colors.add(Color.parseColor("#496cef"));
        colors.add(Color.parseColor("#6785f2"));
        colors.add(Color.parseColor("#675cf2"));

        pieChartManagers.setSolidPieChart(names,nums,colors);
    }
}
