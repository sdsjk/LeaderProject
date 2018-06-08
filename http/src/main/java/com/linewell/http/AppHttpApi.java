package com.linewell.http;

import android.app.Activity;
import android.app.Dialog;
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
 * 把原来的appHttpUtils改造成单例模式,开放方法的继承,提高扩展性
 * @author lyixin 2017-11-29
 */
public class AppHttpApi {

    // 是否为调试
    public static boolean IS_DEBUG = false;

    // 协议
    private static final String PROTOCOL = "https";

    private static AppHttpApi instance;

    public synchronized static AppHttpApi getInstance(AppHttpApi outInstance){
        if(outInstance==null){
            return getInstance();
        }
        instance = outInstance;
        return instance;
    }

    public synchronized static AppHttpApi getInstance(){
        if(instance==null){
            instance = new AppHttpApi();
        }
        return instance;
    }

    public AppHttpApi(){

    }

    /**
     * 提交JSON数据
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param params        提交的参数
     * @param resultHandler 结果回调
     */
    public <T> void postJson(Context context, final String serviceUrl, BaseParams params, final AppHttpResultHandler<T> resultHandler) {
        postJson(context, serviceUrl, params, resultHandler, null);
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
    public <T> void updateFiles(Activity activity, final String serviceUrl, UploadFilesParams params, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        if(activity==null){
            return;
        }
        updateFiles((Context) activity, serviceUrl, params, resultHandler, loadingMsg, activity.getClass().getName());
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
    private <T> void updateFiles(Context context, final String serviceUrl, UploadFilesParams params, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        if(context==null){
            return;
        }
        updateFiles(context, serviceUrl, params, resultHandler, loadingMsg, context.getPackageName());
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
    public <T> void updateFiles(final Context context, final String serviceUrl, UploadFilesParams params, AppHttpResultHandler<T> httpResultHandler, final String loadingMsg, String tag) {
        if(context==null){
            return;
        }
        boolean flag = SystemUtils.isNetConnected(context);

        final AppHttpResultHandler<T> resultHandler = httpResultHandler;

        if (!flag) {
            try {
                if (!resultHandler.onFail(new JsonObject())) {
                    postException(serviceUrl, context.getString(R.string.session_expired), context);
                    //清空session
                    AppSessionUtils.getInstance().invalidate(context);
//                Toast.makeText(context, R.string.session_expired, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                postException(serviceUrl, context.getString(R.string.system_error) + "-" + e.getMessage(), context);
            }
            return;
        }


        // 如果是HTTPS协议，则伪造协议的内容
        if (serviceUrl.toLowerCase().indexOf(PROTOCOL) == 0) {
            FakeX509TrustManager.allowAllSSL();
        }

        CountDownLatch signal = null;
        if (IS_DEBUG) {
            signal = new CountDownLatch(1);
        }

        //启动登录进度
//        ProgressDialog progressDialog = null;
        Dialog progressDialog = null;

        // 如果消息不为空，则直接显示进度条
        if (null != loadingMsg) {
            progressDialog = showProgressDialog(context, loadingMsg);
            final Activity activity = (Activity) context;
            if (activity != null && !activity.isFinishing())
                progressDialog.show();
        }

        // 构建公共提交的参数


        //请求验证码
//        final ProgressDialog finalProgressDialog = progressDialog;
        final Dialog finalProgressDialog = progressDialog;
        final CountDownLatch finalSignal = signal;


        ListenerHandler<JsonObject> listenerHandler = new ListenerHandler<JsonObject>() {

            @Override
            public void onResponse(JsonObject response) {
                if (null != finalProgressDialog) {
                    // 关闭进度条
                    finalProgressDialog.cancel();
                }

                switch (response.get("status").getAsInt()) {
                    case 0:
                        try {
                            // 回调的方法返回为true，则不需要再执行公共的逻辑，否则执行公共的逻辑
                            if (!resultHandler.onFail(response)) {
                                postException(serviceUrl, response.get("message").getAsString(), context);
                                ToastUtils.show(context, response.get("message").getAsString(), Toast.LENGTH_SHORT);
                            }
                        } catch (Exception e) {
                            postException(serviceUrl, context.getString(R.string.system_error) + "-" + e.getMessage(), context);
                        }
                        break;
                    case 1:// 成功执行
                        try {
                            JsonElement jsonElement = response.get("content");
                            if (null == jsonElement || jsonElement.toString().equals("null")) {
                                resultHandler.onSuccess(null, response);
                            } else {
                                resultHandler.onSuccess((T) response.get("content"), response);
                            }
                        } catch (Exception e) {
                            postException(serviceUrl, context.getString(R.string.system_error) + "-" + e.getMessage(), context);
                        }

                        break;
                    case 2:// 会话过期
                        try {
                            if (!resultHandler.onFail(response)) {
                                postException(serviceUrl, context.getString(R.string.session_expired), context);
//                            Toast.makeText(context, R.string.session_expired, Toast.LENGTH_SHORT).show();
                                //清空session
                                AppSessionUtils.getInstance().invalidate(context);
                            }
                        } catch (Exception e) {
                            postException(serviceUrl, context.getString(R.string.system_error) + "-" + e.getMessage(), context);
                        }
                        break;
                    default:// 默认的处理
                        try {
                            if (!resultHandler.onFail(response)) {
                                postException(serviceUrl, response.get("message").getAsString(), context);
                                ToastUtils.show(context, response.get("message").getAsString(), Toast.LENGTH_SHORT);
                            }
                        } catch (Exception e) {
                            postException(serviceUrl, context.getString(R.string.system_error) + "-" + e.getMessage(), context);
                        }
                        break;
                }
                if (IS_DEBUG) {
                    finalSignal.countDown();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                if (null != finalProgressDialog) {
                    // 关闭进度条
                    finalProgressDialog.cancel();
                }

                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("status", 3);
                resultJson.addProperty("message", error.getMessage());
                if (!resultHandler.onSysFail(resultJson)) {
                    postException(serviceUrl, context.getString(R.string.network_error), context);
//                    Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
                }

                if (IS_DEBUG) {
                    finalSignal.countDown();
                }
            }
        };

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("deviceId", SystemUtils.getImei(context));
        listenerHandler.setReqMapHandler(headerMap);
        listenerHandler.setTag(tag);

        HttpUtils.uploadFiles(context, serviceUrl, params.getFormMap(), params.getFileMap(), params.getHeaderMap(), listenerHandler);
        // HttpUtils.postJson(context, serviceUrl, GsonUtil.objectToJSON(params), listenerHandler);// end postJson

        // 是否为调试
        if (IS_DEBUG) {
            try {
                signal.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
    public <T> void postJson(Activity activity, final String serviceUrl, JsonObject jsonObject, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        if(activity==null){
            return;
        }
        postJson((Context) activity, serviceUrl, jsonObject, resultHandler, loadingMsg, activity.getClass().getName());
    }

    /**
     * 提交JSON数据
     *
     * @param context       上下文对象
     * @param serviceUrl    服务地址
     * @param jsonObject    提交的参数
     * @param resultHandler 结果回调
     */
    public <T> void postJson(Context context, final String serviceUrl, JsonObject jsonObject, final AppHttpResultHandler<T> resultHandler) {
        postJson(context, serviceUrl, jsonObject, resultHandler, null);
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
    public <T> void postJson(Context context, final String serviceUrl, JsonObject jsonObject, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        if(context==null){
            return;
        }
        postJson(context, serviceUrl, jsonObject, resultHandler, loadingMsg, context.getPackageName());
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
    public <T> void postJson(Context context, final String serviceUrl, JsonObject jsonObject, AppHttpResultHandler<T> httpResultHandler, final String loadingMsg, String tag) {
        if (context == null) {
            return;
        }

        final AppHttpResultHandler<T> resultHandler = httpResultHandler;

        boolean flag = SystemUtils.isNetConnected(context);

        if (!flag) {
            if (!resultHandler.onSysFail(new JsonObject())) {
                postException(serviceUrl, context.getString(R.string.network_error), context);
                Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // 如果是HTTPS协议，则伪造协议的内容
        if (serviceUrl.toLowerCase().indexOf(PROTOCOL) == 0) {
            FakeX509TrustManager.allowAllSSL();
        }

        CountDownLatch signal = null;
        if (IS_DEBUG) {
            signal = new CountDownLatch(1);
        }

        //启动登录进度
//        ProgressDialog progressDialog = null;
        Dialog progressDialog = null;

        // 如果消息不为空，则直接显示进度条
        if (null != loadingMsg) {
            progressDialog = showProgressDialog(context, loadingMsg);
            Activity activity = (Activity) context;
            if (activity != null && !activity.isFinishing())
                progressDialog.show();
        }

        // 构建公共提交的参数
         BaseParamsInits.getInstance(context).initParms(jsonObject);

        //请求验证码
//        final ProgressDialog finalProgressDialog = progressDialog;
        final Dialog finalProgressDialog = progressDialog;
        final CountDownLatch finalSignal = signal;

        final Context finalContext = context.getApplicationContext();

        ListenerHandler<JsonObject> listenerHandler = new ListenerHandler<JsonObject>() {

            @Override
            public void onResponse(JsonObject response) {
                if (null != finalProgressDialog) {
                    // 关闭进度条
                    finalProgressDialog.cancel();
                }

                switch (response.get("status").getAsInt()) {
                    case 0:

                        // 回调的方法返回为true，则不需要再执行公共的逻辑，否则执行公共的逻辑
                        try {
                            if (!resultHandler.onFail(response)) {
                                String message;
                                if (response.get("message").isJsonNull()) {
                                    postException(serviceUrl, "", finalContext);
                                } else {
                                    message = response.get("message").toString();
                                    postException(serviceUrl, message, finalContext);
                                    ToastUtils.show(finalContext, message, Toast.LENGTH_SHORT);
                                }
                            }
                        } catch (Exception e) {
                            postException(serviceUrl, finalContext.getString(R.string.system_error) + "-" + e.getMessage(), finalContext);
                        }
                        break;
                    case 1:// 成功执行
                        try {
                            JsonElement jsonElement = response.get("content");
                            if (null == jsonElement || jsonElement.toString().equals("null")) {
                                resultHandler.onSuccess(null, response);
                            } else {
                                resultHandler.onSuccess((T) response.get("content"), response);
                            }
                        } catch (Exception ex) {
                            if (ex != null) {
                                postException(serviceUrl, finalContext.getString(R.string.system_error) + "-" + ex.getMessage(), finalContext);
                            }
//                            Toast.makeText(context, R.string.system_error, Toast.LENGTH_SHORT).show();
                            showTipDialog(finalContext, serviceUrl, R.string.system_error);
                        }

                        break;
                    case 2:// 会话过期
                        try {
                            if (!resultHandler.onFail(response)) {
                                postException(serviceUrl, finalContext.getString(R.string.session_expired), finalContext);
//                            Toast.makeText(context, R.string.session_expired, Toast.LENGTH_SHORT).show();
                                //清空session
//                                AppSessionUtils.getInstance().invalidate(finalContext);
                            }
                        } catch (Exception e) {
                            postException(serviceUrl, finalContext.getString(R.string.system_error) + "-" + e.getMessage(), finalContext);
                        }
                        break;
                    default:// 默认的处理
                        try {
                            if (!resultHandler.onFail(response)) {
                                String message;
                                if (response.get("message").isJsonNull()) {
                                    postException(serviceUrl, "", finalContext);
                                } else {
                                    message = response.get("message").toString();
                                    postException(serviceUrl, message, finalContext);
                                    ToastUtils.show(finalContext, message, Toast.LENGTH_SHORT);
                                }

                            }
                        } catch (Exception e) {
                            postException(serviceUrl, finalContext.getString(R.string.system_error) + "-" + e.getMessage(), finalContext);
                        }
                        break;
                }
                if (IS_DEBUG) {
                    finalSignal.countDown();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                if (null != finalProgressDialog) {
                    // 关闭进度条
                    finalProgressDialog.cancel();
                }

                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("status", 3);
                resultJson.addProperty("message", error.getMessage());
                if (!resultHandler.onSysFail(resultJson)) {
                    postException(serviceUrl, error.getMessage(), finalContext);
//                    Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
                }

                if (IS_DEBUG) {
                    finalSignal.countDown();
                }
            }
        };

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("deviceId", SystemUtils.getImei(context));
        listenerHandler.setReqMapHandler(headerMap);
        listenerHandler.setTag(tag);

        HttpUtils.postJson(context, serviceUrl, jsonObject, listenerHandler);// end postJson

        // 是否为调试
        if (IS_DEBUG) {
            try {
                signal.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
    public <T> void postJson(Activity activity, final String serviceUrl, BaseParams params, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        if(activity==null){
            return;
        }
        postJson((Context) activity, serviceUrl, params, resultHandler, loadingMsg, activity.getClass().getName());
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
    public <T> void postJson(Context context, final String serviceUrl, BaseParams params, final AppHttpResultHandler<T> resultHandler, final String loadingMsg) {
        if(context==null){
            return;
        }
        postJson(context, serviceUrl, params, resultHandler, loadingMsg, context.getPackageName());
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
    public <T> void postJson(Context context, final String serviceUrl, BaseParams params, AppHttpResultHandler<T> httpResultHandler, final String loadingMsg, String tag) {
        if(context==null){
            return;
        }
        boolean flag = SystemUtils.isNetConnected(context);

        final AppHttpResultHandler<T> resultHandler = httpResultHandler;

        if (!flag) {
            if (!resultHandler.onSysFail(new JsonObject())) {
                postException(serviceUrl, context.getString(R.string.network_error), context);
                Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // 如果是HTTPS协议，则伪造协议的内容
        if (serviceUrl.toLowerCase().indexOf(PROTOCOL) == 0) {
            FakeX509TrustManager.allowAllSSL();
        }

        CountDownLatch signal = null;
        if (IS_DEBUG) {
            signal = new CountDownLatch(1);
        }

        //启动登录进度
//        ProgressDialog progressDialog = null;
        Dialog progressDialog = null;

        // 如果消息不为空，则直接显示进度条
        if (null != loadingMsg) {
            if (context instanceof Activity) {
                progressDialog = showProgressDialog(context, loadingMsg);
                Activity activity = (Activity) context;
                if (activity != null && !activity.isFinishing())
                    progressDialog.show();
            }
        }

        final Context finalContext = context.getApplicationContext();

        // 构建公共提交的参数
        BaseParamsInits.getInstance(finalContext).initParms(params);

        //请求验证码
//        final ProgressDialog finalProgressDialog = progressDialog;
        final Dialog finalProgressDialog = progressDialog;
        final CountDownLatch finalSignal = signal;

        ListenerHandler<JsonObject> listenerHandler = new ListenerHandler<JsonObject>() {

            @Override
            public void onResponse(JsonObject response) {
                if (null != finalProgressDialog) {
                    // 关闭进度条

                    finalProgressDialog.cancel();
                }

                switch (response.get("status").getAsInt()) {
                    case 0:
                        try {
                            // 回调的方法返回为true，则不需要再执行公共的逻辑，否则执行公共的逻辑
                            if (!resultHandler.onFail(response)) {
                                // 防止报错
                                if (!response.get("message").isJsonNull()) {
                                    postException(serviceUrl, response.get("message").getAsString(), finalContext);
                                    ToastUtils.show(finalContext, response.get("message").getAsString(), Toast.LENGTH_SHORT);
                                    resultHandler.onFail(response.get("message").getAsString());
                                }

                            }
                        } catch (Exception e) {
                            postException(serviceUrl, finalContext.getString(R.string.system_error) + "-" + e.getMessage(), finalContext);
                        }
                        break;
                    case 1:// 成功执行
                        try {
                            JsonElement jsonElement = response.get("content");
                            if (null == jsonElement || jsonElement.isJsonNull()) {
                                resultHandler.onSuccess(null, response);
                            } else {
                                if (response.get("content") != null && response.get("content").isJsonPrimitive()) {
                                    if (response.getAsJsonPrimitive("content").isString()) {
                                        resultHandler.onSuccess((T) response.get("content").getAsString(), response);
                                        break;
                                    }
//                                    if (response.getAsJsonPrimitive("content").isBoolean()) {
//                                        resultHandler.onSuccess((T) new Boolean(response.get("content").getAsBoolean()), response);
//                                        break;
//                                    }
//                                    if (response.getAsJsonPrimitive("content").isNumber()) {
//                                        resultHandler.onSuccess((T) new Integer(response.get("content").getAsInt()), response);
//                                        break;
//                                    }
                                }
                                resultHandler.onSuccess((T) response.get("content"), response);

                            }
                        } catch (Exception ex) {
                            if (ex != null) {
                                postException(serviceUrl, ex.getMessage(), finalContext);
                            }
//                            Toast.makeText(context, R.string.system_error, Toast.LENGTH_SHORT).show();
                            showTipDialog(finalContext, serviceUrl, R.string.system_error);
                            try {
                                resultHandler.onFail(response);
                            } catch (Exception e) {
                                postException(serviceUrl, finalContext.getString(R.string.system_error) + "-" + e.getMessage(), finalContext);
                            }
                        }

                        break;
                    case 2:// 会话过期
                        try {
                            if (!resultHandler.onFail(response)) {
                                postException(serviceUrl, finalContext.getString(R.string.session_expired), finalContext);
                            Toast.makeText(finalContext, R.string.session_expired, Toast.LENGTH_SHORT).show();
                                //清空session
//                                AppSessionUtils.getInstance().invalidate(finalContext);
                            }
                        } catch (Exception e) {
                            postException(serviceUrl, finalContext.getString(R.string.system_error) + "-" + e.getMessage(), finalContext);
                        }
                        break;
                    default:// 默认的处理
                        try {
                            if (!resultHandler.onFail(response)) {
                                postException(serviceUrl, response.get("message").getAsString(), finalContext);
                                ToastUtils.show(finalContext, response.get("message").getAsString(), Toast.LENGTH_SHORT);
                            }
                        } catch (Exception e) {
                            postException(serviceUrl, finalContext.getString(R.string.system_error) + "-" + e.getMessage(), finalContext);
                        }
                        break;
                }
                if (IS_DEBUG) {
                    finalSignal.countDown();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != finalProgressDialog) {
                    // 关闭进度条
                    finalProgressDialog.cancel();
                }

                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("status", 3);
                resultJson.addProperty("message", error.getMessage());
                boolean showTips = false;
                try{
                    showTips = !resultHandler.onSysFail(resultJson);
                }catch (Exception e){
                    showTips = true;
                }
                if(showTips){
                    postException(serviceUrl, error.getMessage(), finalContext);
                    Toast.makeText(finalContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                    showTipDialog(finalContext, serviceUrl, R.string.network_error);
                }

                if (IS_DEBUG) {
                    finalSignal.countDown();
                }
            }
        };

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("deviceId", params.getClientParams().getDeviceId());
        listenerHandler.setReqMapHandler(headerMap);
        listenerHandler.setTag(tag);

        HttpUtils.postJson(context, serviceUrl, GsonUtil.objectToJSON(params), listenerHandler);// end postJson

        // 是否为调试
        if (IS_DEBUG) {
            try {
                signal.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }// end postJson


    //展示进度框
    public Dialog showProgressDialog(Context context, String loadingMsg) {
        final PortraitActivity activity = (PortraitActivity) context;
        LoadingDialog dialog = new LoadingDialog(context, loadingMsg) {
            @Override
            public void cancel() {
                if(!isShowing()){
                    return;
                }
                if (activity != null && !activity.isFinishing()) {
                    super.cancel();
                }
            }
        };
        activity.addDialog(dialog);
        return dialog;
    }

    private void showTipDialog(Context context, String content, int type) {
//        CommonApplicaton commonApplicaton = (CommonApplicaton) (context.getApplicationContext());
//        Activity mActivity = commonApplicaton.getCurrentActivity();
//        if (mActivity == null) {
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(
//                mActivity);
//        builder.setMessage(content + "" + mActivity.getString(type));
//        builder.setCancelable(true);
//        builder.setPositiveButton("知道了！", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.create().show();
    }

    /**
     * 上传异常
     *
     * @param exceptionMsg 异常信息
     * @param requestUrl   请求路径
     */
    public void postException(String requestUrl, String exceptionMsg, Context context) {
        StringBuffer postMsg = new StringBuffer("");
        if (!TextUtils.isEmpty(requestUrl)) {
            postMsg.append("请求地址：" + requestUrl + "\n");
        }
        if (!TextUtils.isEmpty(exceptionMsg)) {
            postMsg.append("异常信息：" + exceptionMsg);
        }
        return;
    }

    public void cancelAll(Context context, String tag){
        VolleySingleton.getVolleySingleton(context).cancelAll(tag);
    }
}
