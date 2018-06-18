package com.seaboxdata.portal.utils.chart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 饼图
 * Created by zjianning on 2018/5/15.
 */

public class PieChartManager {

    PieChart pieChart;
    Legend legend;
    public PieChartManager(PieChart mPieChart) {
        this.pieChart = mPieChart;
        initChart();
    }

    private void initChart() {
        pieChart.setHoleRadius(80f);//半径
        pieChart.setTransparentCircleRadius(60f);// 半透明圈
        pieChart.setDrawCenterText(true);//饼状图中间可以添加文字
        //  是否显示中间的洞
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90);// 初始旋转角度
        pieChart.setRotationEnabled(false);// 可以手动旋转
        pieChart.setUsePercentValues(true);//显示成百分比
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setExtraOffsets(8, 8, 40, 8);

        legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(1f);
        legend.setYOffset(0f);
        legend.setXOffset(4f);
        legend.setTextColor(Color.parseColor("#a1a1a1"));
        legend.setTextSize(12);


        pieChart.animateXY(1000, 1000);//设置动画
    }

    /**
     * 设置饼状图
     *
     * @param name   饼状图分类的名字
     * @param date   数值
     * @param colors 颜色集合
     */
    public void setPieChart(List<String> name, List<Float> date, List<Integer> colors) {
        List<PieEntry> yValue = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            PieEntry entry = new PieEntry(date.get(i), name.get(i));
            yValue.add(entry);
        }
        PieDataSet set = new PieDataSet(yValue, "");
        set.setDrawValues(true);
        set.setValueTextSize(6);
        set.setColors(colors);
        set.setValueTextColor(Color.WHITE);
        set.setValueLinePart1Length(0.43f);
        set.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        set.setValueLinePart1Length(0.3f);
        set.setValueLinePart2Length(0.4f);
        set.setValueLineColor(Color.parseColor("#a1a1a1"));//设置连接线的颜色
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(set);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);


//        PieData data = new PieData(set);
        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh
    }

    /**
     * 设实心饼状图
     *
     * @param name   饼状图分类的名字
     * @param date   数值
     * @param colors 颜色集合
     */
    public void setSolidPieChart(List<String> name, List<Float> date, List<Integer> colors) {

        pieChart.setHoleRadius(0);//实心圆
        pieChart.setTransparentCircleRadius(0);// 半透明圈
        pieChart.setDrawCenterText(false);//饼状图中间不可以添加文字

        List<PieEntry> yValue = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            PieEntry entry = new PieEntry(date.get(i), name.get(i));
            yValue.add(entry);
        }
        PieDataSet set = new PieDataSet(yValue, "");
        set.setDrawValues(true);
        set.setValueTextSize(12);
        set.setColors(colors);

        set.setValueTextColor(Color.WHITE);
        set.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        set.setValueLinePart1Length(0.3f);
        set.setValueLinePart2Length(0.4f);
        set.setValueLineColor(Color.parseColor("#a1a1a1"));//设置连接线的颜色
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(set);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);
        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh
    }

    /**
     * 设置饼状图中间的描述内容
     *
     * @param str
     */
    public void setCenterDescription(String str, int color) {
        pieChart.setCenterText(str);
        pieChart.setCenterTextColor(color);
        pieChart.setCenterTextSize(12);
        pieChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        pieChart.setDescription(description);
        pieChart.invalidate();
    }

}
