package com.seaboxdata.portal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.linewell.core.exception.BugReporter;
import com.linewell.core.html.CommonHtmlActivity;
import com.linewell.utils.StringUtil;
import com.seaboxdata.portal.common.H5Activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 链接处理类
 *
 * @author lyixin
 * @since 2016/9/28.
 */
public class LinkUtils {

    /**
     * 处理链接
     *
     * @param activity activity
     * @param linkUrl  链接
     */
    public static void handleAdLinks(Activity activity, String linkUrl) {
        handleAdLinks(activity, linkUrl, null);
    }

    /**
     * 处理链接
     *
     * @param activity activity
     * @param linkUrl  链接
     * @param title    标题
     */
    public static boolean handleAdLinks(Activity activity, String linkUrl, String title) {
        Intent intent = getOutLinkIntent(activity, linkUrl, "");
        if (intent == null) {
            return false;
        } else {
            activity.startActivityForResult(intent, 1);
            return true;
        }
    }

    /**
     * 处理链接
     *
     * @param activity activity
     * @param linkUrl  链接
     * @param title    标题
     */
    public static boolean handleAdLinks(Activity activity, String linkUrl, String title, String... shareData) {
        Intent intent = getOutLinkIntent(activity, linkUrl, title);
        if (intent == null) {
            return false;
        } else {
            intent.putExtra(CommonHtmlActivity.KEY_SHARE_DATA, shareData);
            activity.startActivityForResult(intent, 1);
            return true;
        }
    }

    /**
     * 根据链接类型(无链接, 内链, 外链)获取Intent
     *
     * @param context
     * @param openType
     * @param data
     * @return
     */
    public static Intent getLinkIntent(Context context, String openType, JsonObject data) {
        String openParamsStr = JsonObjectGetValueUtils.getString(data, "openParams","");
        return getLinkIntent(context, openType, openParamsStr);
    }

    /**
     * 根据链接类型(无链接, 内链, 外链)获取Intent
     *
     * @param context
     * @param openType
     * @return
     */
    public static Intent getLinkIntent(Context context, String openType, String openParamsStr) {
        Intent intent = null;
        if ("0".equals(openType)) {
            // 消息详情
            try{
                JsonObject openParam = urlToJsonObject(openParamsStr);
                String id = JsonObjectGetValueUtils.getString(openParam, "id","");
//                intent = NotifyMessageDetailActivity.getIntent(context, id);
            }catch (Exception e){
            }
        } else if ("1".equals(openType)) {
            // 内部链接(个人主页, 服务详情页, 活动页, 专题页面)
            try {
                JsonObject openParam = urlToJsonObject(openParamsStr);
                intent = LinkUtils.getInnerLinkIntent(context, openParam);
            } catch (Exception e) {//因为运营后台可能瞎鸡巴传，所以你这里要瞎鸡巴处理
                // 消息详情
                try{
                    JsonObject openParam = urlToJsonObject(openParamsStr);
                    String id = JsonObjectGetValueUtils.getString(openParam, "id","");
//                    intent = NotifyMessageDetailActivity.getIntent(context, id);
                }catch (Exception e1){
                    // 外部链接
                    String url = openParamsStr;
                    intent = LinkUtils.getOutLinkIntent(context, url, null);
                }
            }
        } else if ("2".equals(openType)) {
            // 外部链接
            String url = openParamsStr;
            intent = LinkUtils.getOutLinkIntent(context, url, null);
        }

        return intent;
    }

    /**
     * 获取外链的intent
     *
     * @param context context
     * @param linkUrl 链接
     * @param title   标题
     */
    public static Intent getOutLinkIntent(Context context, String linkUrl, String title) {
        if(TextUtils.isEmpty(linkUrl)){
            return null;
        }
        Intent intent = null;
        //如果点击的链接为空或者为#就不做跳转
        linkUrl = linkUrl.trim();//空格处理
        if (StringUtil.isEmpty(linkUrl) || linkUrl.equals("#")) {
        } else if (linkUrl.indexOf("http://") == 0 || linkUrl.indexOf("https://") == 0) {
            if(!linkUrl.contains("&deviceType")){
                linkUrl = linkUrl+"&deviceType=Android";
            }

            if (!linkUrl.contains("&T=")) {
                linkUrl = linkUrl+"&T=CITY";
            }

            intent = new Intent();
            //打开远程环境
            Map<String, List<String>> linkMap = getQueryParams(linkUrl);
            intent.setClass(context, H5Activity.class);
            intent.putExtra(CommonHtmlActivity.KEY_LINK_TITLE, title);

            if (linkMap.size() == 0) {
                linkUrl = linkUrl.replaceFirst("\\&", "?");
            }

            intent.putExtra(CommonHtmlActivity.KEY_LINK_URL, linkUrl);

            //是否隐藏头部  传值就是隐藏
            if (linkMap.size() > 0 && linkMap.get("ifs") != null && linkMap.get("ifs").size() > 0) {
                intent.putExtra(CommonHtmlActivity.KEY_IFS, linkMap.get("ifs").get(0));
            }
            if (linkMap.size() > 0 && linkMap.get("share") != null && linkMap.get("share").size() > 0) {
                intent.putExtra(CommonHtmlActivity.KEY_SHARE, linkMap.get("share").get(0));
            }
            if (linkMap == null || linkMap.get("activityId") == null || linkMap.get("activityId").size() == 0) {
                return intent;
            }
            //noticeId传值
            if (linkMap.size() > 0 && linkMap.get("activityId").size() > 0) {
                intent.putExtra(CommonHtmlActivity.KEY_NOTICE_ID, linkMap.get("activityId").get(0));
            }
            return intent;
        } else {
            //打开本地环境
            JsonObject bannerObj = urlToJsonObject(linkUrl);
            intent = getInnerLinkIntent(context, bannerObj);
        }
        return intent;
    }

    public static JsonObject urlToJsonObject(String linkUrl){
        if(linkUrl.indexOf("?")>=0){
            linkUrl = linkUrl.substring(linkUrl.indexOf("?")+1, linkUrl.length());
        }
        if(linkUrl.startsWith("&")){
            linkUrl = linkUrl.replaceFirst("&","");
        }

        // 字符串转JSON pageId=project-detail-sub&unid=CH1000001247&userId=425bc0754e744742bd919005c9fc06e8
        linkUrl = linkUrl.replace("&", "','");
        linkUrl = "{'" + linkUrl.replace("=", "':'") + "'}";
        // 转换后 '{pageId: "project-detail-sub", unid: "CH1000001247", userId: "425bc0754e744742bd919005c9fc06e8"}'
        JsonObject bannerObj = CommonGsonUtils.jsonToBean(linkUrl, JsonObject.class);
        return bannerObj;
    }

    /**
     * 打开内部链接
     *
     * @param bannerObj
     */
    public static void openActivity(Activity activity, JsonObject bannerObj) {
        try {
            Intent intent = getInnerLinkIntent(activity, bannerObj);
            if (intent != null) {
                activity.startActivityForResult(intent, 0);
            }
        } catch (Exception e) {
            BugReporter.getInstance().postException(e);
        }
    }

    /**
     * 获取内部链接的Intent
     *
     * @param bannerObj
     */
    public static Intent getInnerLinkIntent(Context context, JsonObject bannerObj) {
        try {
            //跳转的类型
            String pageId = JsonObjectGetValueUtils.getString(bannerObj, "pageId", null);
            //如果取出的值为空 不做响应
            if (StringUtil.isEmpty(pageId)) {
                return null;
            }
            //实例一个class
            Class<?> activityClass = null;
            Intent intent = new Intent();
//                PageType pageType = PageType.getTypeByCode(pageId);
//                if(pageType==null){
//                    activityClass = Class.forName(pageId);
//                }else{
//                    //根据别名配置
//
//                    activityClass = Class.forName(pageType.getValue());
//                    Map<String, Object> extra = pageType.getExtra();
//
//                    //额外参数
//                    if(extra!=null){
//                        for (Map.Entry<String, Object> entry : extra.entrySet()) {
//                            Object value = entry.getValue();
//                            if(value instanceof Integer){
//                                intent.putExtra(entry.getKey(), (Integer) value);
//                            }else if(value instanceof String){
//                                intent.putExtra(entry.getKey(), (String) value);
//                            }else if(value instanceof Boolean){
//                                intent.putExtra(entry.getKey(), (Boolean) value);
//                            }
//                        }
//                    }
//                }
//                //跳转到项目详情页
//                intent.setClass(context, activityClass);
//
//                /**
//                 * 遍历获取键值对
//                 */
//                Set<Map.Entry<String, JsonElement>> entries = bannerObj.entrySet();
//                Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry<String, JsonElement> next = iterator.next();
//                    String key = next.getKey();
//                    if (!TextUtils.isEmpty(key) && !"pageId".equals(key)) {
//                        //判断传值是否是boolean型
//                        if (bannerObj.getAsJsonPrimitive(key) != null && bannerObj.getAsJsonPrimitive(key).isBoolean()) {
//                            boolean booleanValue = bannerObj.get(key).getAsBoolean();
//                            intent.putExtra(key, booleanValue);
//                        } else if (bannerObj.getAsJsonPrimitive(key) != null && bannerObj.getAsJsonPrimitive(key).isNumber()) {
//                            //判断传值是否是int型
//                            int numValue = bannerObj.get(key).getAsInt();
//                            intent.putExtra(key, numValue);
//                        } else {
//                            String value = bannerObj.get(key).getAsString();
//                            intent.putExtra(key, value);
//                        }
//                    }
//                }
                return intent;
            // TODO 处理详情页
        } catch (Exception e) {
            return null;
        }
    }


    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }
}
