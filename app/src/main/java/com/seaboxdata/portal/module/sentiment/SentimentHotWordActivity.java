package com.seaboxdata.portal.module.sentiment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import java.util.HashMap;
import java.util.Map;

public class SentimentHotWordActivity extends CommonActivity {

    private Map<Integer,String> hotwordsList=new HashMap<>();
    private ImageView infomation_back;
    private TextView info_title_common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment_hot_word);
        setToolBar();
        initData();
        initView();
    }


    private void initData() {
        hotwordsList.put(0,"退役军官的心里话");
        hotwordsList.put(1,"空中突击旅");
        hotwordsList.put(2,"智慧军营");
        hotwordsList.put(3,"青瓦台回应情缘私刑");
        hotwordsList.put(4,"退役军官的心里话");
        hotwordsList.put(5,"被家人掌掴 轻声割腕");
        hotwordsList.put(6,"学生带框5千，要还两万");
        hotwordsList.put(7,"崔永元再晒合同照");
        hotwordsList.put(8,"火箭球迷喷杜兰特");
        hotwordsList.put(9,"民企境外投资规范");
    }

    private void initView() {
        info_title_common= (TextView) findViewById(R.id.info_title_common);
        info_title_common.setText("热词");
        infomation_back= (ImageView) findViewById(R.id.infomation_back);
        infomation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView word1 = (TextView) findViewById(R.id.hot_key1);
        word1.setText(hotwordsList.get(0));
        word1.startAnimation(getAnimation(0, -((float) (Math.random() * 5) + 2)));

        TextView word2 = (TextView) findViewById(R.id.hot_key2);
        word2.setText(hotwordsList.get(8));
        word2.startAnimation(getAnimation((float) ((Math.random() * 5) + 2),
                -((float) (Math.random() * 5) + 2)));

        TextView word3 = (TextView) findViewById(R.id.hot_key3);
        word3.setText(hotwordsList.get(2));
        word3.startAnimation(getAnimation(-((float) ((Math.random() * 5) + 2)),
                -((float) (Math.random() * 5) + 2)));

        TextView word4 = (TextView) findViewById(R.id.hot_key4);
        word4.setText(hotwordsList.get(1));
        word4.startAnimation(getAnimation((float) ((Math.random() * 5) + 2), 0));

        TextView word5 = (TextView) findViewById(R.id.hot_key5);
        word5.setText(hotwordsList.get(7));
        word5.startAnimation(getAnimation(-((float) ((Math.random() * 5) + 2)),
                0));

        TextView word6 = (TextView) findViewById(R.id.hot_key6);
        word6.setText(hotwordsList.get(6));
        word6.startAnimation(getAnimation((float) ((Math.random() * 5) + 2), 0));

        TextView word7 = (TextView) findViewById(R.id.hot_key7);
        word7.setText(hotwordsList.get(9));
        word7.startAnimation(getAnimation(-((float) ((Math.random() * 5) + 2)),
                0));

        TextView word8 = (TextView) findViewById(R.id.hot_key8);
        word8.setText(hotwordsList.get(3));
        word8.startAnimation(getAnimation(-((float) ((Math.random() * 5) + 2)),
                (float) (Math.random() * 5) + 2));

        TextView word9 = (TextView) findViewById(R.id.hot_key9);
        word9.setText(hotwordsList.get(5));
        word9.startAnimation(getAnimation((float) ((Math.random() * 5) + 2),
                (float) (Math.random() * 5) + 2));

        TextView word10 = (TextView) findViewById(R.id.hot_key10);
        word10.setText(hotwordsList.get(4));
        word10.startAnimation(getAnimation(0, (float) (Math.random() * 5) + 2));
    }

    private Animation getAnimation(float posFromX, float posFromY) {
        Animation anim = null;

        anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, posFromX,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                posFromY, Animation.RELATIVE_TO_SELF, 0.0f);
        anim.setDuration(2000);

        return anim;
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
}
