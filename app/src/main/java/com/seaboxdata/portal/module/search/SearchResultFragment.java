package com.seaboxdata.portal.module.search;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.linewell.innochina.core.entity.params.base.SearchParams;
import com.linewell.pulllistview.ListParams;
import com.linewell.pulllistview.RecyclerViewFragment;
import com.linewell.utils.GsonUtil;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CurRecyclerViewFragment;
import com.seaboxdata.portal.config.ServiceConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果
 */
public class SearchResultFragment extends CurRecyclerViewFragment {


    public static final SearchResultFragment createNew(String keywork){
        SearchResultFragment personalWorksFragment= new SearchResultFragment();
        personalWorksFragment.setArguments(getBundle(keywork));
        return personalWorksFragment;
    }

    public static Bundle getBundle(String keywork){
        //列表
        ListParams params = new ListParams();
        params.setrClass(R.id.class);
        params.setItemLayoutId(R.layout.item_search_result);
        params.setServiceUrl(ServiceConfig.BASE);

        // TODO 测试用
        params.setLoadLocal(true);

        SearchParams defaultVisitorParams = new SearchParams();
        defaultVisitorParams.setKeyword(keywork);
        params.setForbidDown(true);
        params.setDefaultVisitorParams(GsonUtil.getJsonStr(defaultVisitorParams));

        Bundle bundle = new Bundle();
        bundle.putSerializable(RecyclerViewFragment.PARAMS, params);
        return bundle;
    }

    public void setKeyword(String search){
        SearchParams defaultVisitorParams = new SearchParams();
        defaultVisitorParams.setKeyword(search);
        if(mParams!=null){
            mParams.setDefaultVisitorParams(GsonUtil.getJsonStr(defaultVisitorParams));
        }else{
            setArguments(getBundle(search));
        }
    }

    /**
     * 列表的点击事件
     *
     * @param view     列表的对象
     * @param t        JsonObject对象
     * @param position dan
     */
    @Override
    public void onRecyclerViewItemClick(View view, JsonObject t, int position) {
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

        jsonObject = new JsonObject();
        jsonObjectList.add(jsonObject);

        listCallBack.successCallBack(jsonObjectList);
    }
}
