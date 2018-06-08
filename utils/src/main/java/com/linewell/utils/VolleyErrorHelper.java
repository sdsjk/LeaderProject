package com.linewell.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误统一处理
 * Created by caicai on 2016-07-14.
 */
public class VolleyErrorHelper {

    private static String generic_server_down ="服务请求超时";
    private static String no_internet = "网络问题";
    private static  String generic_error = "请求时出现错误";

    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return generic_server_down;
        }
        else if (isServerProblem(error)) {
            return handleServerError(error, context);
        }
        else if (isNetworkProblem(error)) {
            return no_internet;
        }
        return generic_error;
    }
    /**
     * Determines whether the error is related to network
     * @param error
     * @return
     */
    public static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }
    /**
     * Determines whether the error is related to server
     * @param error
     * @return
     */
    public static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    public static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error": "Some error occured" }
                        // Use "Gson" to parse the result
                        HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());
                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return error.getMessage();
                default:
                    return generic_server_down;
            }
        }
        return generic_error;
    }

}
