package com.linewell.core.interfaces;

import android.widget.ImageView;

/**
 * @author lyixin
 * @since 2017/8/23.
 */

public interface IImageloader {

    public void displayImage(String imagePath, ImageView imageView);

    public void displayImage(String imagePath, ImageView imageView, OnLoadListener onLoadListener);

    public boolean isCached(String imagePath);

    public void preloadImage(String imagePath, OnLoadListener onLoadListener);
}
