package com.seaboxdata.portal.module.work;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linewell.core.view.FontIconText;
import com.linewell.pulllistview.ListParams;
import com.linewell.pulllistview.RecyclerViewFragment;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;
import com.seaboxdata.portal.config.ServiceConfig;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import butterknife.ButterKnife;

/**
 * Created by zjianning on 2018/5/11.
 */

public class WorkFragment extends CommonFragment {
    private View view;

    private WorkListFragment workListFragment;

    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_work, container, false);

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

        initView();

        return view;
    }

    private void initView() {
        FontIconText fontIconText = (FontIconText) view. findViewById(R.id.backBtn);
        fontIconText.setVisibility(View.GONE);
        TextView textView = (TextView) view.findViewById(R.id.centerTitle);
        textView.setText("办公");


        FragmentManager fragmentManage = getChildFragmentManager();
        if (fragmentManage.getFragments() == null || fragmentManage.getFragments().size() == 0) {
            FragmentTransaction fTransaction = getChildFragmentManager().beginTransaction();
            workListFragment = new WorkListFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable(RecyclerViewFragment.PARAMS,
                    getListParams(ServiceConfig.BASE, R.layout.item_work));
            workListFragment.setArguments(bundle);

            fTransaction.add(R.id.work_list_fl, workListFragment);
            fTransaction.commit();
        }


    }



    /**
     *
     * @param serviceUrl
     * @param layoutId
     * @return
     */
    public ListParams getListParams(String serviceUrl, int layoutId){
        ListParams params = new ListParams();
        params.setrClass(R.id.class);
        params.setItemLayoutId(layoutId);
        params.setServiceUrl(serviceUrl);

        // TODO
        params.setLoadLocal(true);

        return params;
    }

}
