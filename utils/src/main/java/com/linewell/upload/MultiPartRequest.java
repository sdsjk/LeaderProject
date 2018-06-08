package com.linewell.upload;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.linewell.constants.Constants;
import com.linewell.utils.GsonUtil;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caicai on 2016-07-25.
 */
public class MultiPartRequest extends Request<JsonObject> {

    // 上传的对象
    private MultipartEntity mMultiPartEntity = new MultipartEntity();

    // 头部信息
    private Map<String, String> mHeaders = new HashMap<String, String>();

    // 监听
    private final Response.Listener<JsonObject> mListener;

    /**
     * Creates a new request with the given url.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     */
    public MultiPartRequest(String url, Response.Listener<JsonObject> listener) {
        this(url, listener, null);
    }

    /**
     * Creates a new POST request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MultiPartRequest(String url, Response.Listener<JsonObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
    }

    /**
     * 获取文件上传的对象
     * @return
     */
    public MultipartEntity getMultiPartEntity() {
        return this.mMultiPartEntity;
    }

    @Override
    public String getBodyContentType() {
        return mMultiPartEntity.getContentType().getValue();
    }

    public void addHeader(String key, String value) {
        this.mHeaders.put(key, value);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.mHeaders;
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // multipart body
            this.mMultiPartEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<JsonObject> parseNetworkResponse(NetworkResponse response) {

        String jsonString = "{}";

        try {
            jsonString = new String(response.data, Constants.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.success(GsonUtil.getJsonObject(jsonString),
                HttpHeaderParser.parseCacheHeaders(response));

    }

    @Override
    protected void deliverResponse(JsonObject response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }
}
