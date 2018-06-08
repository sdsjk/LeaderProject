package com.seaboxdata.portal.common.http;

import android.app.Dialog;
import android.content.Context;

import com.linewell.core.activity.PortraitActivity;
import com.linewell.http.AppHttpApi;

/**
 * Created by linyixin on 2017/11/29.
 */

public class CommonAppHttpApi extends AppHttpApi {

    @Override
    public Dialog showProgressDialog(Context context, String loadingMsg) {
        final PortraitActivity activity = (PortraitActivity) context;
        HttpLoadingDialog dialog = new HttpLoadingDialog(context, loadingMsg) {
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
}
