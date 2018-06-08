package com.linewell.pulllistview;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * 列表监听接口
 * Created by zjianning on 2016/8/2.
 */
public interface RecyclerViewListener {
    /**
     * 列表渲染完 自定义处理
     * @param baseViewHolder
     * @param obj
     */
    public void customRenderItem(BaseViewHolder baseViewHolder, JsonObject obj);
    /**
     * 列表渲染完 自定义处理
     * @param baseViewHolder
     * @param obj
     */
    public void customRenderItemAfter(BaseViewHolder baseViewHolder, JsonObject obj);

    void setNewData(List<JsonObject> jsonObjectList);

}
