package com.linewell.http.file;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.duowan.mobile.netroid.NetworkResponse;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.request.ImageRequest;
import com.duowan.mobile.netroid.toolbox.ImageLoader;
import com.linewell.http.Constants;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * 加载图片
 * Created by caicai on 2016-07-12.
 */


public class SelfImageLoader extends ImageLoader {

    public static final String RES_ASSETS = "assets://";
    public static final String RES_SDCARD = "sdcard://";
    public static final String RES_DRAWABLE = "drawable://";
    public static final String RES_HTTP = "http://";
    public static final String RES_HTTPS = "https://";
    
    
    private AssetManager mAssetManager;
    private Resources mResources;

    public SelfImageLoader(RequestQueue queue, ImageCache cache, Resources resources, AssetManager assetManager) {
        super(queue, cache);
        mResources = resources;
        mAssetManager = assetManager;
    }

    @Override
    public ImageRequest buildRequest(String requestUrl, int maxWidth, int maxHeight) {
        ImageRequest request;
        if (requestUrl.startsWith(RES_ASSETS)) {
            request = new ImageRequest(requestUrl.substring(RES_ASSETS.length()), maxWidth, maxHeight) {
                @Override
                public NetworkResponse perform() {
                    try {
                        return new NetworkResponse(toBytes(mAssetManager.open(getUrl())), Constants.DEFAULT_CHARSET);
                    } catch (IOException e) {
                        return new NetworkResponse(new byte[1], Constants.DEFAULT_CHARSET);
                    }
                }
            };
        } else if (requestUrl.startsWith(RES_SDCARD)) {
            request = new ImageRequest(requestUrl.substring(RES_SDCARD.length()), maxWidth, maxHeight) {
                @Override
                public NetworkResponse perform() {
                    try {
                        return new NetworkResponse(toBytes(new FileInputStream(getUrl())), Constants.DEFAULT_CHARSET);
                    } catch (IOException e) {
                        return new NetworkResponse(new byte[1], Constants.DEFAULT_CHARSET);
                    }
                }
            };
        } else if (requestUrl.startsWith(RES_DRAWABLE)) {
            request = new ImageRequest(requestUrl.substring(RES_DRAWABLE.length()), maxWidth, maxHeight) {
                @Override
                public NetworkResponse perform() {
                    try {
                        int resId = Integer.parseInt(getUrl());
                        Bitmap bitmap = BitmapFactory.decodeResource(mResources, resId);
                        return new NetworkResponse(bitmap2Bytes(bitmap), Constants.DEFAULT_CHARSET);
                    } catch (Exception e) {
                        return new NetworkResponse(new byte[1], Constants.DEFAULT_CHARSET);
                    }
                }
            };
        } else if (requestUrl.startsWith(RES_HTTP) ||requestUrl.startsWith(RES_HTTPS) ) {
            request = new ImageRequest(requestUrl, maxWidth, maxHeight);
        } else {
            return null;
        }

        makeRequest(request);
        return request;
    }

    public void makeRequest(ImageRequest request) {
        request.setCacheExpireTime(TimeUnit.MINUTES, 10);
    }

    public static byte[] toBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();

        return buffer.toByteArray();
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
