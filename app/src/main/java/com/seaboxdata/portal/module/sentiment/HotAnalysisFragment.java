package com.seaboxdata.portal.module.sentiment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;

/**
 * Created by zhang on 2018/6/17.
 */

public class HotAnalysisFragment extends CommonFragment{
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.analysis_hot,null);
        return view;
    }
}
