package com.seaboxdata.portal.module.work;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.seaboxdata.portal.common.CurRecyclerViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 办公
 */
public class WorkListFragment extends CurRecyclerViewFragment {

    /**
     * 列表的点击事件
     *
     * @param view     列表的对象
     * @param t        JsonObject对象
     * @param position dan
     */
    @Override
    public void onRecyclerViewItemClick(View view, JsonObject t, int position) {

        WorkDetailActivity.startAction(mActivity);


    }

    @Override
    public void customRenderItem(BaseViewHolder baseViewHolder, JsonObject obj) {
    }

    // TODO
    @Override
    protected void getAndHandleListData(String type, Long date, Object sortFieldValue, ListCallBack listCallBack) {
        List<JsonObject> jsonObjectList = new ArrayList<JsonObject>();

        JsonObject jsonObject = new JsonObject();
        jsonObjectList.add(jsonObject);

        listCallBack.successCallBack(jsonObjectList);
    }

}
