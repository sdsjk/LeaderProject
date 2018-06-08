package com.linewell.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageRequest;
import com.google.gson.JsonObject;
import com.linewell.utils.SystemUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 网络请求工具类
 * Created by caicai on 2016-07-07.
 */
public class HttpUtils {

    private static final int initialTimeout = 20*1000;// 默认超时时间



    /**
     * 同步请求，通过json ，json为空为GET，请求，否则为POST请求
     * @param context                 上下文
     * @param url                     请求url
     * @param jsonObject             请求参数
     * @return                        返回json对象
     */
    public static JsonObject syncPostJson(Context context,String url,JsonObject jsonObject){
        NormalRequestFuture<JsonObject> future = NormalRequestFuture.newFuture();
        NormalJsonObjectRequest request = new NormalJsonObjectRequest(url, jsonObject, future, future);

        request.setRetryPolicy(new DefaultRetryPolicy(initialTimeout,0,1.0f));

        VolleySingleton.getVolleySingleton(context).addToRequestQueue(request);
        try {
            JsonObject response = future.get();
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Http获取字符串
     * @param context       上下文
     * @param url            请求地址
     * @param handler       监听
     */
    public static void getString(Context context, String url, ListenerHandler<String> handler){

        if(!SystemUtils.isNetConnected(context)){
            Toast.makeText(context, "请查看网络是否正常", Toast.LENGTH_LONG).show();
            return;
        }

        //字符串请求
        NormalStringRequest stringRequest = new NormalStringRequest( Method.GET,url,handler,handler );
        if (handler.getTag()!=null && !"".equals(handler.getTag())){
            stringRequest.setTag(handler.getTag());
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeout, 0, 1.0f));
        //将StringRequest对象添加进RequestQueue请求队列中
        VolleySingleton.getVolleySingleton(context).addToRequestQueue(stringRequest);
    }

    /**
     * 请求获取字符串
     * @param context           上下文
     * @param url               请求地址
     * @param map               参数
     * @param handler          监听器
     */
    public static void postString(Context context, String url, final Map<String,String> map, ListenerHandler<String> handler){

        if(!SystemUtils.isNetConnected(context)){
            Toast.makeText(context, "请查看网络是否正常", Toast.LENGTH_LONG).show();
            return;
        }

        //字符串请求
        NormalStringRequest stringRequest = new NormalStringRequest(Method.POST, url, handler, handler ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeout, 0, 1.0f));
        if (handler.getTag()!=null && !"".equals(handler.getTag())){
            stringRequest.setTag(handler.getTag());
        }
        //将StringRequest对象添加进RequestQueue请求队列中
        VolleySingleton.getVolleySingleton(context).addToRequestQueue(stringRequest);
    }


    /**
     * 请求返回json格式数据
     * @param context           上下文
     * @param url               请求地址
     * @param handler          监听器
     */
    public static void getJson(Context context,String url,ListenerHandler<JsonObject> handler){

        if(!SystemUtils.isNetConnected(context)){
            Toast.makeText(context, "请查看网络是否正常", Toast.LENGTH_LONG).show();
            return;
        }
        //字符串请求
        NormalJsonObjectRequest  jsonObjectRequest = new NormalJsonObjectRequest(Method.GET, url, null, handler, handler);
        if (handler.getTag()!=null && !"".equals(handler.getTag())){
            jsonObjectRequest.setTag(handler.getTag());
        }
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeout, 0, 1.0f));

        //将StringRequest对象添加进RequestQueue请求队列中
        VolleySingleton.getVolleySingleton(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * 请求图片
     * @param context           上下文
     * @param url               请求地址
     * @param jsonObject       参数
     * @param handler          监听器
     */
    public static void postJson(Context context,String url,JsonObject jsonObject,ListenerHandler<JsonObject> handler){

       if(!SystemUtils.isNetConnected(context)){
           Toast.makeText(context, "请查看网络是否正常", Toast.LENGTH_LONG).show();
           return;
       }

        //字符串请求
        NormalJsonObjectRequest jsonObjectRequest = new NormalJsonObjectRequest(Method.POST,url,jsonObject,handler,handler ) ;

        if (handler.getTag()!=null && !"".equals(handler.getTag())){
            jsonObjectRequest.setTag(handler.getTag());
        }
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeout,0, 1.0f));

        //将StringRequest对象添加进RequestQueue请求队列中
        VolleySingleton.getVolleySingleton(context).addToRequestQueue(jsonObjectRequest);
    }


    /**
     * 加载图片
     * @param context           上下文
     * @param url               图片地址
     * @param maxWidth          最大宽度
     * @param maxHeight         最大高度
     * @param decodeConfig      编码配置
     * @param handler               处理事件
     */
    public static void getImageRequest(Context context,String url,int maxWidth, int maxHeight, Bitmap.Config decodeConfig,ListenerHandler<Bitmap> handler){

        if(!SystemUtils.isNetConnected(context)){
            Toast.makeText(context,"请查看网络是否正常", Toast.LENGTH_LONG).show();
            return;
        }


        //字符串请求
        ImageRequest imageRequest = new ImageRequest(url,handler,maxWidth, maxHeight, decodeConfig,handler ) ;

        if (!TextUtils.isEmpty(handler.getTag())){
            imageRequest.setTag(handler.getTag());
        }

        imageRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeout, 1, 0.0f));

        //将StringRequest对象添加进RequestQueue请求队列中
        VolleySingleton.getVolleySingleton(context).addToRequestQueue(imageRequest);
    }

    /**
     * 上传文件（包含上传字节列表）
     * @param context       上下文
     * @param url           服务地址
     * @param params        表单参数
     * @param tMap          文件参数
     * @param headMap       头部参数
     * @param handler       处理返回结果
     * @param <T>           byte[] 或者 File
     */
    public static <T> void uploadFiles(Context context,String url,Map<String,String> params,Map<String,T> tMap,Map<String,String> headMap,ListenerHandler<JsonObject> handler){

        if(!SystemUtils.isNetConnected(context)){
            Toast.makeText(context, "请查看网络是否正常", Toast.LENGTH_LONG).show();
            return;
        }

        MultiPartRequest multipartRequest = new MultiPartRequest(
                url, handler,handler);
        // 添加header
        if(headMap!=null && headMap.size()>0){
            for(Map.Entry<String,String> entry:headMap.entrySet() ){
                multipartRequest.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 通过MultipartEntity来设置参数
        MultipartEntity multi = multipartRequest.getMultiPartEntity();

        // 添加文本参数
        if (params!=null&& params.size()>0){
            for(Map.Entry<String,String> entry:params.entrySet() ){
                multi.addStringPart(entry.getKey(), entry.getValue());
            }
        }

        int length = 1;
        //bitmapMap参数
        if (tMap!=null&& tMap.size()>0){
            length = tMap.size();
            for(Map.Entry<String,T> entry:tMap.entrySet() ){

                if(entry.getValue() instanceof File){
                    multi.addBinaryPart(entry.getKey(),getBytes((File) entry.getValue()));
                   // multi.addFilePart();
                }

                if(entry.getValue() instanceof byte[]){
                    multi.addBinaryPart(entry.getKey(),(byte[]) entry.getValue());
                }

            }

        }

//        multipartRequest.addHeader( "Content-Type", multi.build().getContentType().getValue());
//        multipartRequest.addHeader( "Content-Disposition", "attachment");

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeout*length,0, 1.0f));

        //将StringRequest对象添加进RequestQueue请求队列中
        VolleySingleton.getVolleySingleton(context).addToRequestQueue(multipartRequest);

    }


    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes( File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }




}
