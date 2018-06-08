package com.linewell.core.utils;

import android.content.Context;
import android.text.TextUtils;

import com.linewell.innochina.entity.dto.user.LoginResultDTO;
import com.linewell.utils.GsonUtil;
import com.linewell.utils.SharedPreferencesUtil;

/**
 * 应用Session的工具类
 * Created by aaron on 2016-07-27.
 */
public class AppSessionUtils {

    public static final String KEY_SESSETION_KEY = "KEY_SESSETION_KEY";

    private LoginResultDTO loginInfo = null;

    //  单实例对象
    private final static AppSessionUtils APP_SESSION_UTILS = new AppSessionUtils();

    private AppSessionUtils(){

    }

    /**
     * 获取实例的对象
     * @return
     */
    public static AppSessionUtils getInstance(){

        return APP_SESSION_UTILS;
    }

    /**
     * 获取TOKEN
     * @param context
     * @return token
     */
    public String getToken(Context context){
        LoginResultDTO loginInfo = this.getLoginInfo(context);
        if(null != loginInfo){
            return loginInfo.getUserTokenId();
        }
        return null;
    }

    /**
     * 获取登录信息
     * @param context 上下文关系
     * @return         登录信息
     */
    public LoginResultDTO getLoginInfo(Context context){
        context = context.getApplicationContext();
        if(null != loginInfo){
            return loginInfo;
        }
        String loginInfoStr = (String) SharedPreferencesUtil.get(context, KEY_SESSETION_KEY, "{}");
        if(TextUtils.isEmpty(loginInfoStr) || "{}".equals(loginInfoStr)){
            return null;
        }
        return GsonUtil.jsonToBean(loginInfoStr, LoginResultDTO.class);
    }

    /**
     * 清除Session的设置
     * @param context      上下文关系
     */
    public void invalidate(Context context){
        context = context.getApplicationContext();
        loginInfo=null;
        SharedPreferencesUtil.remove(context, KEY_SESSETION_KEY);
    }

    /**
     * 判断是否登录
     * @param context 上下文对象
     * @return         是否登录
     */
    public boolean isLogin(Context context){
        LoginResultDTO appLoginInfo = getLoginInfo(context);
        if(null == appLoginInfo){
            return false;
        }
        //&& appLoginInfo.getPhoneAuth() == 1
        if(null != appLoginInfo && !TextUtils.isEmpty(appLoginInfo.getUserTokenId())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 初始化Session对象
     * @param context      上下文关系
     * @param loginInfo   登录信息
     */
    public void initSession(Context context, LoginResultDTO loginInfo){
        context = context.getApplicationContext();
        SharedPreferencesUtil.save(context, KEY_SESSETION_KEY, "{}");
        this.loginInfo = null;
        if(null != loginInfo){
            SharedPreferencesUtil.save(context, KEY_SESSETION_KEY, GsonUtil.getJsonStr(loginInfo));
            this.loginInfo = loginInfo;
        }
    }

    /**
     * 安装时间的key
     */
    private static final String KEY_INSTALL_TIME = "KEY_INSTALL_TIME";

    /**
     * 初始化安装时间
     * @param context
     */
    public void initInstallTime(Context context){
        context = context.getApplicationContext();
        SharedPreferencesUtil.save(context, KEY_INSTALL_TIME, System.currentTimeMillis());
    }

    /**
     * 已安装的时间
     * @param context
     * @return
     */
    public long getInstalledTime(Context context){
        context = context.getApplicationContext();
        Long installTime = (Long) SharedPreferencesUtil.get(context, KEY_INSTALL_TIME, Long.valueOf(0));
        if(installTime==0){
            installTime = System.currentTimeMillis();
            SharedPreferencesUtil.save(context, KEY_INSTALL_TIME, installTime);
        }
        return System.currentTimeMillis()-installTime;
    }

}
