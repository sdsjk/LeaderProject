package com.seaboxdata.portal.common.http;

/**
 * 更新的回调
 * Created by zjianning on 2017/8/18.
 */

public abstract class AppVersionResultHandler<T> {
    /**
     * 需要更新
     */
    public void onUpdate(T t) {}


    /**
     * 最新
     */
    public void onNewest(){}
}
