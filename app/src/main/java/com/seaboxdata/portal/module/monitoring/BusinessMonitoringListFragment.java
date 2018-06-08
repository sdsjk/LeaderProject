package com.seaboxdata.portal.module.monitoring;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.linewell.utils.ToastUtils;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 办公
 */
public class BusinessMonitoringListFragment extends CommonFragment implements MyItemTouchCallback.OnDragListener {

    private View view;

    @BindView(R.id.recycler_select)
    RecyclerView mRecyclerView;

    @BindView(R.id.save_bt)
    Button mSaveBT;

    // TODO
    protected List<JsonObject> getAndHandleListData() {
        List<JsonObject> jsonObjectList = new ArrayList<JsonObject>();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "城市");
        jsonObjectList.add(jsonObject);

        jsonObject = new JsonObject();
        jsonObject.addProperty("name", "教育");
        jsonObjectList.add(jsonObject);

        jsonObject = new JsonObject();
        jsonObject.addProperty("name", "交通");
        jsonObjectList.add(jsonObject);

        return jsonObjectList;
    }

    @Override
    public View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_business_monitoring, container, false);

        unbinder = ButterKnife.bind(this, view);

        final List<JsonObject> list = getAndHandleListData();

        BusinessMonitoringAdapter businessMonitoringAdapter = new BusinessMonitoringAdapter(mActivity, getAndHandleListData());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(businessMonitoringAdapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(businessMonitoringAdapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != list.size()) {
                    itemTouchHelper.startDrag(vh);
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                setButtonEnable(true);
            }
        });

        mSaveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        return view;
    }

    private void save() {

        setButtonEnable(false);
        ToastUtils.show(mContext, "保存成功");

    }

    @Override
    public void onFinishDrag() {
        setButtonEnable(true);
    }

    public void setButtonEnable(boolean enable) {
        this.mSaveBT.setEnabled(enable);
    }

}
