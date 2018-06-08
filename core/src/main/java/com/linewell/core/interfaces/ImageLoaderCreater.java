package com.linewell.core.interfaces;

/**
 * @author lyixin
 * @since 2017/8/23.
 */

public class ImageLoaderCreater<T extends IImageloader > {

    private static final ImageLoaderCreater instance = new ImageLoaderCreater();

    public static ImageLoaderCreater getInstance() {
        return instance;
    }

    private IImageloader imageloader;

    public void init(Class<T> iImageloaderClass){
        try {
            imageloader = iImageloaderClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public IImageloader getImageLoader(){
        return imageloader;
    }
}
