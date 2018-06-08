package com.seaboxdata.portal.utils.badge;

import android.content.Context;

import com.linewell.core.utils.AppSessionUtils;
import com.linewell.http.AppHttpResultHandler;

/**
 * 消息数量缓存工具类
 * @author lyixin
 * @since 2016/10/28.
 */
public class MessageCountUtils {

    public static final String ACTION_REFRESH_MESSAGE_COUNT = "ACTION_REFRESH_MESSAGE_COUNT";


    private static int[] counts = new int[6];//0:系统 1: 通知 2: 私聊

    private static final String SP_KEY_MSG_COUNT = "SP_KEY_MSG_COUNT";

    private static final int INDEX_SYS = 0;
    private static final int INDEX_NOTIFY = 1;

    public static int getSystemMsgCount(){
        return getCount(INDEX_SYS);
    }
    public static int getTradeMsgCount(){
        return getCount(INDEX_NOTIFY);
    }


    public static int getAllCount(){
        int count = 0;
        for(int i:counts){
            count+=i;
        }
        return count;
    }

    public static int getCount(int index){
        if(index<counts.length){
            return counts[index];
        }else{
            return 0;
        }
    }

    public static void setSystemMsgCount(int count){
        setCount(count, INDEX_SYS);
    }
    public static void setTradeMsgCount(int count){
        setCount(count, INDEX_NOTIFY);
    }
    public static void setCount(int count, int index){
        if(index<counts.length){
            counts[index] = count;
        }else{
            return ;
        }
    }

    public static void clearMsgCount(){
        for(int i=0;i<counts.length;i++){
            counts[i] = 0;
        }
    }

    public static void refresh(final Context context, final AppHttpResultHandler<Object> appHttpResultHandler){
        if(context==null){
            return;
        }

        if(!AppSessionUtils.getInstance().isLogin(context)){
            return;
        }

//        AppHttpUtils.postJson(context, InnochinaServiceConfig.MSG_SERVICES.GET_NOTIFY_UNREAD_COUNT, new BaseParams(), new AppHttpResultHandler<Object>() {
//            @Override
//            public void onSuccess(Object result, JsonObject allResult) {
//                super.onSuccess(result, allResult);
//                NotifyCountDTO notifyCountDTO = GsonUtil.jsonToBean(result.toString(), NotifyCountDTO.class);
//
//                if(notifyCountDTO!=null){
//                    setSystemMsgCount((int) notifyCountDTO.getSysNotifyCount());
//
//                    setTradeMsgCount((int) notifyCountDTO.getTradeNotifyCount());
//                }
//
//                if(context!=null){
//                    // 其他机型角标设置方式
//                    BadgeUtil.setBadgeCount(context, MessageCountUtils.getAllCount());
//                }
//
//                if(appHttpResultHandler!=null){
//                    appHttpResultHandler.onSuccess(result, allResult);
//                }
//            }
//        });


    }
}
