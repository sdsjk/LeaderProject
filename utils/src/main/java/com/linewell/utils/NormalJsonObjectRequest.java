package com.linewell.utils;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL, allowing for an
 * optional {@link JSONObject} to be passed in as part of the request body.
 */
public class NormalJsonObjectRequest extends JsonRequest<JsonObject> {

    private static final String charset = "UTF-8";
    private Map<String,String> reqMapHeader =null ;
    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JsonObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public NormalJsonObjectRequest(int method, String url, JsonObject jsonRequest,
                             Response.Listener<JsonObject> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : GsonUtil.getJsonStr(jsonRequest), listener,
                errorListener);
        this.initHeaders(listener);
    }

    private void initHeaders(Response.Listener<JsonObject> listener){
        if(listener instanceof  ListenerHandler){
            this.reqMapHeader = ((ListenerHandler) listener).getReqMapHandler();
        }
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see #NormalJsonObjectRequest(int, String, JsonObject, Response.Listener, Response.ErrorListener)
     */
    public NormalJsonObjectRequest(String url, JsonObject jsonRequest, Response.Listener<JsonObject> listener,
                             Response.ErrorListener errorListener) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
                listener, errorListener);

        this.initHeaders(listener);

    }


    @Override
    protected Response<JsonObject> parseNetworkResponse(NetworkResponse response) {

        String jsonString = "{}";

        try {
            jsonString = new String(response.data, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.success(GsonUtil.getJsonObject(jsonString),
                HttpHeaderParser.parseCacheHeaders(response));
    }

    public void addHeader(String key, String value) {
        this.reqMapHeader.put(key, value);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

//        Map<String, String> defaultMap =  super.getHeaders();
//
//        if (this.reqMapHeader!=null){
//            if(defaultMap == null){
//                defaultMap = new HashMap<String,String>();
//            }
//            defaultMap.putAll(this.reqMapHeader);
//        }
//
//        return defaultMap;

        if(null != this.reqMapHeader) {
            return this.reqMapHeader;
        }
        return super.getHeaders();
    }

}
