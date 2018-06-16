package com.seaboxdata.portal.module.setting;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SuggestHandleActivity extends CommonActivity implements View.OnClickListener {

    //    标题
    @BindView(R.id.info_title_common)
    TextView infoTitleCommon;
    private Unbinder unbinder;

    private GridView gridview;
    private TextView info_title_common_commit;
    private ImageView infomation_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_handle);
        setToolBar();
        unbinder = ButterKnife.bind(this);
        infoTitleCommon.setText("意见反馈");
        initView();
        initData();
        initListener();
    }

    private void initData() {
        List<Integer> allData = new ArrayList<>();
        allData.add(R.drawable.my_suggest_add);
        gridview.setAdapter(new SuggestGridViewAdapter(this, allData));
    }

    private void initListener() {
        info_title_common_commit.setOnClickListener(this);
        infomation_back.setOnClickListener(this);
    }

    private void initView() {
        gridview = (GridView) findViewById(R.id.gridview);
        infomation_back= (ImageView) findViewById(R.id.infomation_back);

        info_title_common_commit = (TextView) findViewById(R.id.info_title_common_commit);
        info_title_common_commit.setVisibility(View.VISIBLE);
    }


    private void setToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            linear_bar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            linear_bar.getLayoutParams().height = getStatusBarHeight();

            if (StatusBarUtil.StatusBarLightMode(this) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == infomation_back) {
            finish();
        }
    }
}
