package com.linewell.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一的application
 *
 * @author lyixin
 * @since 2016/7/20
 */
public abstract class CommonApplicaton extends Application {

    private boolean clearAllActivities = false;

    /**
     * activity管理队列
     */
    private ArrayList<Activity> activities = new ArrayList<Activity>();

    /**
     * 全局变量
     */
    private Map<String, Object> map = new HashMap<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final Context context = this;

    }

    /**
     * 添加界面信息
     * 2014年8月18日
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        clearAllActivities = false;
        activities.add(activity);
    }

    /**
     * 移除界面信息
     * 2014年8月18日
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 关闭所有界面
     * 2014年8月18日
     */
    public void finishAllActivities() {
        clearAllActivities = true;
        if(activities.size()<=0){
            return;
        }
        for(int i=activities.size()-1;i>=0;i--){
            activities.get(i).finish();
        }
    }

    /**
     * 获取应用的类型
     *
     * @return 应用的类型
     */
    public abstract String getAppSystemType();

    public Activity getCurrentActivity() {
        int size = activities.size();
        if(size>0){
            return activities.get(size - 1);
        }else{
            return null;
        }
    }

    public int getActivitySize(){
        return activities.size();
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public boolean isClearAllActivities() {
        return clearAllActivities;
    }
}
