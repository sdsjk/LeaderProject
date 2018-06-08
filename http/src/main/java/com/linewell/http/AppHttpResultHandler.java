package com.linewell.http;

import com.google.gson.JsonObject;

/**
 * 应用的Http请求的结果
 * Created by aaron on 2016-07-20.
 */
public abstract class AppHttpResultHandler<T> {

    /**
     * 成功后的事件
     * @param result     成功后的结果集
     * @param allResult 所有成功后的事件
     */
    public void onSuccess(T result, JsonObject allResult){

    }

    /**
     * 失败的事件
     * @param allResult 所有的结果
     * @return           返回是否需要执行系统默认
     */
    public boolean onFail(JsonObject allResult){
        return false;
    }

    /**
     * 系统级的错误（如网络未连接等）
     * @param allResult 所有的结果
     * @return           返回是否需要执行系统默认
     */
    public boolean onSysFail(JsonObject allResult){
        return false;
    }

    /**
     * 失败的事件
     * @param message 需要提示的消息
     */
    public void onFail(String message) {

    }

}
