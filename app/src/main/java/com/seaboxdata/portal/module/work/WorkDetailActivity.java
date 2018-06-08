package com.seaboxdata.portal.module.work;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 日程的详情页
 * Created by zjianning on 2018/5/15.
 */

public class WorkDetailActivity extends CommonActivity implements  CalendarView.OnDateSelectedListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnDateLongClickListener{

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.work_detail_content)
    LinearLayout mContentView;

    @BindView(R.id.work_detail_time)
    TextView mTimeTV;

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, WorkDetailActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_work_detail);


        initView();

        initData();
    }

    private void initData() {

        mCalendarView.setRange(2018, 5, 2019, 12);
        mCalendarView.scrollToCalendar(2018, 5, 15);
        mTimeTV.setText("2018年5月15号");

        final List<Calendar> schemes = new ArrayList<>();
        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();
        schemes.add(getSchemeCalendar(year, month, 3, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 6, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 9, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 13, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 14, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 15, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 18, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 25, 0, ""));
        mCalendarView.setSchemeDate(schemes);

        mContentView.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(mCommonContext);
        for (int i = 0; i < 2; i++) {
            View view = layoutInflater.inflate(R.layout.item_work_detail, mContentView, false);
            mContentView.addView(view);
        }


    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
//        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
//        calendar.setScheme(text);
        return calendar;
    }

    private void initView() {
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnDateLongClickListener(this, false);

        setCenterTitle(mCalendarView.getCurYear() + "年" + mCalendarView.getCurMonth() + "月");

    }

    @Override
    public void onYearChange(int year) {

    }

    @Override
    public void onMonthChange(int year, int month) {

    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        setCenterTitle(calendar.getYear() + "年" + calendar.getMonth() + "月");

        if (isClick && calendar.hasScheme()) {
            mTimeTV.setText(getCalendarText(calendar));
        }
    }

    private static String getCalendarText(Calendar calendar) {
        return calendar.getYear() + "年" + calendar.getMonth() + "月" + calendar.getDay() + "日";
    }

    @Override
    public void onDateLongClick(Calendar calendar) {

    }
}
