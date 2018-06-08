package com.linewell.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linewell.core.activity.PortraitActivity;
import com.linewell.core.params.UploadFilesParams;
import com.linewell.core.utils.AppSessionUtils;
import com.linewell.core.view.LoadingDialog;
import com.linewell.innochina.core.entity.params.base.BaseParams;
import com.linewell.utils.GsonUtil;
import com.linewell.utils.SystemUtils;
import com.linewell.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 应用的http请求工具类
 * Created by aaron on 2016-07-15.
 */
public class AppHttpUtils {

    /**
     * 提交JSON数据
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param params        提交的参数
     * @param resultHandler 结果回调
     */
    public static <T> void postJson(Context context, final String serviceUrl, BaseParams params, final AppHttpResultHandler<T> resultHandler) {
        AppHttpApi.getInstance().postJson(context, serviceUrl, params, resultHandler, null);
    }


    /**
     * 提交UploadFilesParams
     *
     * @param activity      activity
     * @param serviceUrl    服务地址
     * @param params        提交的参数
     * @param resultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    public static <T> void updateFiles(Activity activity, final String serviceUrl, UploadFilesParams params, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        AppHttpApi.getInstance().updateFiles((Context) activity, serviceUrl, params, resultHandler, loadingMsg, activity.getClass().getName());
    }

    /**
     * 提交UploadFilesParams
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param params        提交的参数
     * @param resultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    private static <T> void updateFiles(Context context, final String serviceUrl, UploadFilesParams params, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        AppHttpApi.getInstance().updateFiles(context, serviceUrl, params, resultHandler, loadingMsg, context.getPackageName());
    }

    /**
     * 提交UploadFilesParams
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param params        提交的参数
     * @param httpResultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    private static <T> void updateFiles(final Context context, final String serviceUrl, UploadFilesParams params, AppHttpResultHandler<T> httpResultHandler, final String loadingMsg, String tag) {
        AppHttpApi.getInstance().updateFiles(context, serviceUrl, params, httpResultHandler, loadingMsg, tag);
    }// end postJson

    /**
     * 提交JSON数据
     *
     * @param activity      activity
     * @param serviceUrl    服务地址
     * @param jsonObject    提交的参数
     * @param resultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    public static <T> void postJson(Activity activity, final String serviceUrl, JsonObject jsonObject, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        AppHttpApi.getInstance().postJson((Context) activity, serviceUrl, jsonObject, resultHandler, loadingMsg, activity.getClass().getName());
    }

    /**
     * 提交JSON数据
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param jsonObject    提交的参数
     * @param resultHandler 结果回调
     */
    public static <T> void postJson(Context context, final String serviceUrl, JsonObject jsonObject, final AppHttpResultHandler<T> resultHandler) {
        AppHttpApi.getInstance().postJson(context, serviceUrl, jsonObject, resultHandler, null);
    }

    /**
     * 提交JSON数据
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param jsonObject    提交的参数
     * @param resultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    public static <T> void postJson(Context context, final String serviceUrl, JsonObject jsonObject, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        AppHttpApi.getInstance().postJson(context, serviceUrl, jsonObject, resultHandler, loadingMsg, context.getPackageName());
    }

    /**
     * 提交JSON数据
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param jsonObject    提交的参数
     * @param httpResultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    public static <T> void postJson(Context context, final String serviceUrl, JsonObject jsonObject, AppHttpResultHandler<T> httpResultHandler, final String loadingMsg, String tag) {
        AppHttpApi.getInstance().postJson(context, serviceUrl, jsonObject, httpResultHandler, loadingMsg, tag);
    }// end postJson


    /**
     * 提交JSON数据
     *
     * @param activity      activity
     * @param serviceUrl    服务地址
     * @param params        提交的参数
     * @param resultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    public static <T> void postJson(Activity activity, final String serviceUrl, BaseParams params, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        AppHttpApi.getInstance().postJson((Context) activity, serviceUrl, params, resultHandler, loadingMsg, activity.getClass().getName());
    }

    /**
     * 提交JSON数据
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param params        提交的参数
     * @param resultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    public static <T> void postJson(Context context, final String serviceUrl, BaseParams params, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        AppHttpApi.getInstance().postJson(context, serviceUrl, params, resultHandler, loadingMsg, context.getPackageName());
    }

    /**
     * 提交JSON数据
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param params        提交的参数
     * @param httpResultHandler 结果回调
     * @param loadingMsg    进度条显示的消息(该参数为null时不显示进度条）
     */
    public static <T> void postJson(Context context, final String serviceUrl, BaseParams params, AppHttpResultHandler<T> httpResultHandler, final String loadingMsg, String tag) {
        AppHttpApi.getInstance().postJson(context, serviceUrl, params, httpResultHandler, loadingMsg, tag);
    }// end postJson


    public static void cancelAll(Context context, String tag){
        AppHttpApi.getInstance().cancelAll(context, tag);
    }
}
