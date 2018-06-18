package com.seaboxdata.portal.module.info;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.linewell.core.view.FontIconText;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zjianning on 2018/5/11.
 */

public class InfoFragment extends CommonFragment implements  CalendarView.OnDateSelectedListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnDateLongClickListener{

    private View view;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.work_detail_content)
    LinearLayout mContentView;

    @BindView(R.id.work_detail_time)
    TextView mTimeTV;
    @BindView(R.id.backBtn)
    FontIconText backBtn;
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_work_detail, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) view.findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            linear_bar.getLayoutParams().height = getStatusBarHeight(mActivity);

            if (StatusBarUtil.StatusBarLightMode(mActivity) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }


        unbinder = ButterKnife.bind(this, view);

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        backBtn.setVisibility(View.GONE);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnDateLongClickListener(this, false);

        setCenterTitle(mCalendarView.getCurYear() + "年" + mCalendarView.getCurMonth() + "月",view);

    }

    public void setCenterTitle(String stringTitle, View view) {
        Log.e("TAG", "================="+stringTitle);
        TextView textView = (TextView) view.findViewById(R.id.centerTitle);
        if (null != textView) {
            textView.setText(stringTitle);
        }
    }

    private void initData() {

        mCalendarView.setRange(2018, 5, 2019, 12);
        mCalendarView.scrollToCalendar(2018, 6, 14);
        mTimeTV.setText("2018年5月15号");

        final List<Calendar> schemes = new ArrayList<>();
        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();
        schemes.add(getSchemeCalendar(year, month, 3, 0, "112"));
        schemes.add(getSchemeCalendar(year, month, 6, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 9, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 13, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 14, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 15, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 18, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 25, 0, ""));
        mCalendarView.setSchemeDate(schemes);
        mContentView.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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
        calendar.setSchemeColor(Color.BLUE);
//        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onYearChange(int year) {

    }

    @Override
    public void onMonthChange(int year, int month) {

    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        setCenterTitle(calendar.getYear() + "年" + calendar.getMonth() + "月",view);

        if (isClick && calendar.hasScheme()) {
            mTimeTV.setText(getCalendarText(calendar));
        }
    }

    @Override
    public void onDateLongClick(Calendar calendar) {

    }

    private static String getCalendarText(Calendar calendar) {
        return calendar.getYear() + "年" + calendar.getMonth() + "月" + calendar.getDay() + "日";
    }

}
