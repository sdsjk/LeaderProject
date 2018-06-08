package com.linewell.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caicai on 2016-07-12.
 */
public class NormalStringRequest extends Request<String> {
    private Response.Listener<String> mListener;
    private static final String charset = "UTF-8";
    private Map<String,String> reqMapHeader =null ;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public NormalStringRequest(int method, String url, Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        if(listener instanceof ListenerHandler){
            this.reqMapHeader = ((ListenerHandler) listener).getReqMapHandler();
        }
    }

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public NormalStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }



    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, charset);
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> map =  super.getHeaders();

        if (reqMapHeader != null){
            if(map==null){
                map = new HashMap<String,String>();
            }
            map.putAll(reqMapHeader);
        }

        return map;
    }

    @Override
    protected void onFinish() {
        reqMapHeader = null;
        mListener = null;
        super.onFinish();
    }
}
