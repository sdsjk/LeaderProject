package com.seaboxdata.portal;


import android.content.Context;

import com.linewell.core.CommonApplicaton;
import com.linewell.http.AppHttpApi;
import com.seaboxdata.portal.common.http.CommonAppHttpApi;

public class PortalApplication extends CommonApplicaton {

    private Context mContext = PortalApplication.this;

    @Override
    public void onCreate() {
        super.onCreate();

        //替换新的http交互
        AppHttpApi.getInstance(new CommonAppHttpApi());

//        FreelineCore.init(this);
    }

    @Override
    public String getAppSystemType() {
        return "";
    }


}
