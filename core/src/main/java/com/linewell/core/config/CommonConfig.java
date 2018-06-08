package com.linewell.core.config;

import com.linewell.utils.StringUtil;

/**
 * @author lyixin
 * @since 2017/8/23.
 */

public class CommonConfig {

    private static String SERVICE_URL = "";

    public static void setServiceUrl(String serviceUrl) {
        SERVICE_URL = serviceUrl;
    }

    public static String getServiceUrl() {
        return SERVICE_URL;
    }

    public static String getUrl(String path){
        if(StringUtil.isEmpty(SERVICE_URL)||StringUtil.isEmpty(path)){
            return "";
        }else{
            if(SERVICE_URL.endsWith("/")&&path.startsWith("/")){
                return SERVICE_URL+path.replaceFirst("/","");
            }else if(!SERVICE_URL.endsWith("/")&&!path.startsWith("/")){
                return SERVICE_URL+"/"+path;
            }else{
                return SERVICE_URL+path;
            }
        }
    }
}
