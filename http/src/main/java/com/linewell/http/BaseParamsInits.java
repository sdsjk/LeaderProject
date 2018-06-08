package com.linewell.http;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.linewell.core.utils.AppSessionUtils;
import com.linewell.innochina.core.entity.params.base.AuthParams;
import com.linewell.innochina.core.entity.params.base.BaseParams;
import com.linewell.innochina.core.entity.params.base.ClientParams;
import com.linewell.utils.ApkUtils;
import com.linewell.utils.GsonUtil;
import com.linewell.utils.MetaDataUtils;
import com.linewell.utils.SystemUtils;

/**
 * 初始化请求参数
 * Created by caicai on 2016-07-12.
 */
public class BaseParamsInits {

    /**
     * 客户端对象
     */
    private ClientParams appClientParams = null;

    private AuthParams authParams = null;

    private Context context = null;

    private String appId = null;

    private String siteId = null;

    private String siteAreaCode = null;


    /**
     * 获取单例
     *
     * @return
     */
    public static BaseParamsInits getInstance(Context context) {

        BaseParamsInits ourInstance = new BaseParamsInits(context);

        return ourInstance;

    }

    /**
     * 内部构造函数
     */
    private BaseParamsInits(Context context) {

        this.context = context.getApplicationContext();
        appClientParams = new ClientParams();
        appClientParams.setDeviceId(SystemUtils.getImei(context));
        appClientParams.setOs("Android");
        //增加版本号
        appClientParams.setAppVersion(ApkUtils.getVerName(context));
        appClientParams.setAppPackageName(context.getPackageName());
        appClientParams.setDeviceType(android.os.Build.BRAND+" "+android.os.Build.MODEL);//手机型号
        appClientParams.setSystemVersion(android.os.Build.VERSION.RELEASE);//系统型号（Android 版本）
        appClientParams.setCarrierName(getProvidersName(context));

        authParams = new AuthParams();

        Object appidObj = MetaDataUtils.getAppMetaData(context, "APP_ID");
        if(appidObj!=null){
            appId = String.valueOf(appidObj);
        }
        Object siteIdObj = MetaDataUtils.getAppMetaData(context, "SITE_ID");
        if(siteIdObj!=null){
            siteId = String.valueOf(siteIdObj);
        }
        Object siteAreaCodeObj = MetaDataUtils.getAppMetaData(context, "SITE_AREA_CODE");
        if(siteAreaCodeObj!=null){
            siteAreaCode = String.valueOf(siteAreaCodeObj);
        }
    }

    /**
     * 初始化公用参数
     *
     * @param params
     */
    public void initParms(BaseParams params) {
        appClientParams.setTimeStamp(System.currentTimeMillis());

        String token = AppSessionUtils.getInstance().getToken(context);
        if (!TextUtils.isEmpty(token)) {
            authParams.setToken(token);
        }

        appClientParams.setNetwork(SystemUtils.getNetConnectType(context));

        params.setAuthParams(authParams);
        params.setClientParams(appClientParams);

        params.setAppId(appId);
        params.setSiteId(siteId);
        params.setSiteAreaCode(siteAreaCode);
    }

    /**
     * 初始化公用参数
     *
     * @param params
     */
    public void initParms(JsonObject params) {

        appClientParams.setTimeStamp(System.currentTimeMillis());

        String token = AppSessionUtils.getInstance().getToken(context);
        if (!TextUtils.isEmpty(token)) {
            authParams.setToken(token);
        }

        appClientParams.setNetwork(SystemUtils.getNetConnectType(context));

        params.add("authParams", GsonUtil.getJsonObject(GsonUtil.getJsonStr(authParams)));
        params.add("clientParams", GsonUtil.getJsonObject(GsonUtil.getJsonStr(appClientParams)));


        params.addProperty("appId", appId);
        params.addProperty("siteId", siteId);
        params.addProperty("siteAreaCode", siteAreaCode);
    }

    /**
     * 获取json参数对象
     *
     * @param params
     * @return
     */
    public JsonObject getJsonObject(BaseParams params) {
        initParms(params);
        return GsonUtil.objectToJSON(params);
    }

    /**
     * 返回手机运营商名称
     * @return
     */
    public static String getProvidersName(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperatorName();
    }

}
