package com.seaboxdata.portal.module.info;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

public class InfomationActivity extends CommonActivity implements View.OnClickListener {
    private ImageView infomation_back;

    private View home_title;
    private EditText home_search_ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

//            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
//            linear_bar.setVisibility(View.VISIBLE);
//            linear_bar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//            linear_bar.getLayoutParams().height = getStatusBarHeight();

            if (StatusBarUtil.StatusBarLightMode(this) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }
        initView();
    }

    private void initView() {
        RecyclerView infomation_recyclerview = (RecyclerView) findViewById(R.id.infomation_recyclerview);
        infomation_recyclerview.setAdapter(new InfoationAdapter(this));
        infomation_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        infomation_back = (ImageView) findViewById(R.id.info_back);
        infomation_back.setOnClickListener(this);
        home_title = findViewById(R.id.home_title);
        home_search_ed = (EditText) findViewById(R.id.home_search_ed);
        infomation_recyclerview.addOnScrollListener(mOnScrollListener);
    }


    int mDistance = 0;
    int maxDistance = 255;//当距离在[0,255]变化时，透明度在[0,255之间变化]
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        //dy:每一次竖直滑动增量 向下为正 向上为负
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            mDistance += dy;
            float percent = mDistance * 1f / maxDistance;//百分比
            int alpha = (int) (percent * 255);
            int argb = Color.argb(alpha, 255, 255, 255);
            setSystemBarAlpha(alpha);
        }
    };

    /**
     * 设置标题栏背景透明度
     *
     * @param alpha 透明度
     */
    private void setSystemBarAlpha(int alpha) {

        if (alpha >= 255) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            home_title.setBackgroundColor(Color.argb(255, 48, 63, 159));
            home_title.setBackgroundColor(Color.argb(255, 255, 255, 255));
            home_search_ed.setBackground(getResources().getDrawable(R.drawable.edit_raduis_up));
            infomation_back.setBackgroundResource(R.drawable.infomation_back);
        } else if (alpha == 0) {
            home_title.setBackgroundColor(Color.argb(0, 255, 255, 255));
            home_search_ed.setBackground(getResources().getDrawable(R.drawable.edit_raduis));
            infomation_back.setBackgroundResource(R.drawable.info_back);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            //标题栏渐变。a:alpha透明度 r:红 g：绿 b蓝
//        titlebar.setBackgroundColor(Color.rgb(57, 174, 255));//没有透明效果
            home_title.setBackgroundColor(Color.argb(alpha, 255, 255, 255));//透明效果是由参数1决定的，透明范围[0,255]
//            home_title.getBackground().setAlpha(alpha);
        }
//        home_title.setBackgroundColor(Color.argb(alpha*2, 255, 255, 255));//透明效果是由参数1决定的，透明范围[0,255]

    }

    @Override
    public void onClick(View v) {
        if (v == infomation_back) {
            finish();
        }
    }
}
