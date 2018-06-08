package com.linewell.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * volley单例
 * Created by caicai on 2016-07-07.
 */
public class VolleySingleton {

    /**
     * 单例对象
     */
    private static VolleySingleton volleySingleton;

    /**
     * 请求队列
     */
    private RequestQueue mRequestQueue;

    /**
     * 图片加载对象
     */
    private ImageLoader mImageLoader;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 构造函数
     *
     * @param context
     */
    private VolleySingleton(Context context) {

        this.mContext = context.getApplicationContext();

        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,

                new ImageLoader.ImageCache() {

                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }

                });
    }

    /**
     * 获取单实例对象
     * @param context 上下文
     * @return         单实例对象
     */
    public static  VolleySingleton getVolleySingleton(Context context) {

        if (volleySingleton != null) {
            return volleySingleton ;
        }

        synchronized (VolleySingleton.class) {

            if (volleySingleton != null) {
                return volleySingleton ;
            }
             volleySingleton = new VolleySingleton(context);

        }
        return volleySingleton;
    }

    /**
     * 获取请求的队列
     * @return 请求的队列
     */
    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * 添加到请求到请求的队列
     * @param req 请求对象
     * @param <T> 请求对象的泛型
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * 获取图片加载器
     * @return
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 取消队列中的所有的请求
     * @param tag 标签
     */
    public void cancelAll(String tag) {
        getRequestQueue().cancelAll(tag);
    }

    /**
     * 伪造请求队列
     * @param queue 请求队列
     */
    public void fakeRequestQueue(RequestQueue queue){
        this.mRequestQueue = queue;
    }
}
