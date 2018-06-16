package com.seaboxdata.portal.module.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhang on 2018/6/14.
 */

public class HistoryPushFragment extends CommonFragment implements  CalendarView.OnDateSelectedListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnDateLongClickListener, CalendarLayout.ScrollerStatusListener {

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.work_detail_content)
    LinearLayout mContentView;
    @BindView(R.id.history_push_titile_year)
    TextView history_push_titile_year;
    @BindView(R.id.history_push_titile_month)
    TextView history_push_titile_month;
    @BindView(R.id.history_push_show)
    ImageButton history_push_show;
    private  boolean flag=true;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarlaout;

    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View historyPush = inflater.inflate(R.layout.history_push, container, false);
        unbinder = ButterKnife.bind(this, historyPush);

        initView(historyPush);
        initData();
        return historyPush;

    }

    private void initView(View view) {
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnDateLongClickListener(this, false);
        calendarlaout.setScrollerStatusListener(this);
        history_push_titile_year.setText(mCalendarView.getCurYear()+ "年");
        history_push_titile_month.setText(mCalendarView.getCurMonth() + "月");

    }

    @OnClick(R.id.history_push_show)
    public void showOrHideCalendarView(){
        if(calendarlaout.isExpand()) {
            calendarlaout.shrink();
//            history_push_show.setImageResource(R.drawable.history_down);
        }else {
            calendarlaout.expand();
//            history_push_show.setImageResource(R.drawable.history_up);
        }

    }

    private void initData() {
        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();
        mCalendarView.setRange(2018, 5, 2019, 12);
        mCalendarView.scrollToCalendar(year, month, mCalendarView.getCurDay());


        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < 2; i++) {
            View view = layoutInflater.inflate(R.layout.item_work_detail, mContentView, false);
            mContentView.addView(view);
        }
    }


    @Override
    public void onYearChange(int year) {
        history_push_titile_year.setText(year + "年");
    }

    @Override
    public void onMonthChange(int year, int month) {
        history_push_titile_month.setText(month + "月");
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
//        Toast.makeText(getActivity(),calendar.getDay()+"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateLongClick(Calendar calendar) {

    }

    @Override
    public void scrollerTop() {
        Log.e("TAG", "===========scrollerTop");
        history_push_show.setImageResource(R.drawable.history_down);
    }

    @Override
    public void scrollerBottom() {
        Log.e("TAG", "===========scrollerBottom");

        history_push_show.setImageResource(R.drawable.history_up);
    }
}
