package com.linewell.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * volley图片缓存
 * @author lyixin
 * @since 2016/7/8.
 */
public class VolleyImageLoader {

    private VolleyImageLoader(){

    }

    /**
     * 获取ImageLoader对象
     * @param context
     * @return
     */
    public static final ImageLoader getImageLoader(Context context){
        RequestQueue mQueue = Volley.newRequestQueue(context);
//        return new ImageLoader(mQueue, BitmapCache.getInstance());
        return new ImageLoader(mQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
    }

    /**
     * 示例
     */
    private void test(){
        ImageView imageView = null;
        int defaultImage = 0;
        int failedImage = 0;
        Context context = null;

        //新建listener
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                defaultImage, failedImage);
        //获取加载对象
        ImageLoader imageLoader = VolleyImageLoader.getImageLoader(context);
        //获取图像
        imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", listener);
    }
}
