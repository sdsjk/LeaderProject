package com.linewell.http;

import com.android.volley.Response;

import java.util.Map;


/**
 * 监听处理事件
 * Created by caicai on 2016-07-07.
 */
public abstract class ListenerHandler<T> implements Response.Listener<T>,Response.ErrorListener {

    private String tag = null;//标签


    private Map<String,String> mapHandler ;//头部消息用于权限验证处理

    /**
     * 获取标签
     * @return
     */
    public String getTag() {
        return tag;
    }

    /**
     * 设置标签
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 请求头部消息
     * @return
     */
    public Map<String, String> getReqMapHandler() {
        return mapHandler;
    }

    /**
     * 设置请求头部消息
     * @param mapHandler
     */
    public void setReqMapHandler(Map<String, String> mapHandler) {
        this.mapHandler = mapHandler;
    }
}
