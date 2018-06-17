package com.seaboxdata.portal.module.sentiment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linewell.core.view.AutoFixViewGroup;
import com.seaboxdata.portal.R;

import java.util.ArrayList;
import java.util.List;

public class SentimetSearcheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentimet_searche);

        initView();
    }

    private void initView() {
        AutoFixViewGroup autoFixViewGroup = (AutoFixViewGroup)findViewById(R.id.viewgroup_keywor_list_last);
        ListView search_info_list= (ListView) findViewById(R.id.search_info_list);
        List<String> allData=new ArrayList<>();
        allData.add("89岁的妈妈整宿整宿不睡觉 ");
        allData.add("60岁的儿子一夜一夜陪护她");
        allData.add("杜应征画的母亲 杜应征供图");
        allData.add("边画画边陪伴母亲 杜应征供图");
        allData.add("边画画边陪伴母亲 杜应征供图");

        search_info_list.setAdapter(new ArrayAdapter(this,R.layout.search_new,allData));

        List<String> list = new ArrayList<>();
        list.add("北京流动人口");
        list.add("高新企业流动人数");
        showHotKeyword(list,autoFixViewGroup);
    }



    private void showHotKeyword(List<String> list, AutoFixViewGroup autoFixViewGroup) {
        if(list==null||list.size()==0){
            return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        for(final String item:list){
            View view = layoutInflater.inflate(R.layout.item_search_sentimet, autoFixViewGroup, false);
            final TextView textView = (TextView) view.findViewById(R.id.text_keyword);
            textView.setText(item);
            autoFixViewGroup.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SentimetSearcheActivity.this,textView.getText()+"",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
