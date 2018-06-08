package com.linewell.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linewell.utils.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import static com.nostra13.universalimageloader.core.assist.ImageScaleType.IN_SAMPLE_POWER_OF_2;

/**
 * universalimageloader图片缓存
 *
 * @author lyixin
 * @since 2016/7/8.
 */
public class UniversalImageLoader {

    /**
     * 图片缩放比
     */
    private static final double IMAGE_ZOOM_SCALE = 3;

    /**
     * OSS自动旋转
     */
    private static final String OSS_AUTO_ORIENT = "/resize,m_lfit,limit_1,w_4096,h_4096/format,jpg/quality,Q_100/auto-orient,1";
    /**
     * OSS图片属性前缀
     */
    private static final String OSS_PROCESS = "?x-oss-process=image";

    private UniversalImageLoader() {

    }

    private static DisplayImageOptions options;

    /**
     * 获取配置
     *
     * @return
     */
    public static DisplayImageOptions getDisplayImageOptions(Context context) {
        return getDisplayImageOptions(context, null);
    }

    /**
     * 获取配置
     *
     * @return
     */
    public static DisplayImageOptions getDisplayImageOptions(Context context, Drawable loadingDrawable) {
        if (options == null) {
            options = getOptionsBuilder(context, loadingDrawable).build();
        }
        return options;
    }

    private static DisplayImageOptions.Builder getOptionsBuilder(Context context, Drawable loadingDrawable) {
        //显示图片的配置
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        if (context != null) {
            Drawable loadedFailDrawable = context.getResources().getDrawable(R.drawable.default_bg2);
            builder.showImageOnFail(loadedFailDrawable);
            if (loadingDrawable != null) {
                builder.showImageOnLoading(loadingDrawable);
            }
        } else {
            builder.showImageOnFail(R.drawable.default_bg2);
//                        .showImageOnLoading(R.drawable.default_bg1);
            if (loadingDrawable != null) {
                builder.showImageOnLoading(loadingDrawable);
            }
        }
        builder.cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565);
        return builder;
    }

    /**
     * 显示图片
     *
     * @param imagePath 图片路径
     * @param imageView 图片控件
     */
    public static void displayImage(String imagePath, ImageView imageView) {
        if (TextUtils.isEmpty(imagePath)) {
            return;
        }
        displayImage(imagePath, imageView, null);
    }

    /**
     * 显示图片
     *
     * @param imagePath 图片路径
     * @param imageView 图片控件
     */
    public static void displayImage(String imagePath, Drawable thumb, ImageView imageView) {
        if (TextUtils.isEmpty(imagePath) || imagePath == null) {
            return;
        }
        displayImage(imagePath, imageView, getDisplayImageOptions(imageView.getContext(), thumb), null);

        //options要置空,否则thumb会串掉
        options = null;
    }


    /**
     * 显示图片
     *
     * @param imagePath 图片路径
     * @param imageView 图片控件
     */
    public static void displayImage(String imagePath, String thumb, ImageView imageView) {
        if (TextUtils.isEmpty(imagePath) || imagePath == null) {
            return;
        }
        if (TextUtils.isEmpty(thumb)) {
            displayImage(imagePath, imageView, null);
        } else {
            displayImageWithThumb(imagePath, thumb, imageView);
        }
    }

    /**
     * 对imagePath进行处理
     *
     * @param imagePath
     * @return
     */
    public static String handImagepath(String imagePath) {
        if (imagePath.indexOf("file://") >= 0 || imagePath.indexOf("drawable://") >= 0 || imagePath.indexOf("?x-oss-process=") >= 0) {
            //本地图片或者头像不做处理
        } else {
            imagePath = imagePath + OSS_PROCESS + OSS_AUTO_ORIENT;
        }
        return imagePath;
    }

    /**
     * 显示图片
     *
     * @param imagePath 图片路径
     * @param imageView 图片控件
     */
    public static void displayChatImage(String imagePath, ImageView imageView) {
//        imagePath = imagePath + OSS_PROCESS + "/resize,w_200"+OSS_AUTO_ORIENT;
        displayImage(imagePath, imageView, null);
    }

    /**
     * 通用的加载回调
     */
    public static class CommonImageLoadingListener implements ImageLoadingListener {

        private boolean loaded = false;

        public void setLoaded(boolean loaded) {
            this.loaded = loaded;
        }

        public boolean getLoaded() {
            return loaded;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            setLoaded(true);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }

    public static void displayImageWithThumb(String imagePath, String thumb, final ImageView imageView) {
        final CommonImageLoadingListener originalListener = new CommonImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                imageView.setImageBitmap(loadedImage);
            }
        };
        CommonImageLoadingListener thumbListener = new CommonImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                if (!originalListener.getLoaded()) {
                    imageView.setImageBitmap(loadedImage);
                }
            }
        };

        ImageLoader.getInstance().loadImage(thumb, getDisplayImageOptions(imageView.getContext()), thumbListener);
        ImageLoader.getInstance().loadImage(imagePath, getOptionsBuilder(imageView.getContext(), null).imageScaleType(ImageScaleType.NONE).build(), originalListener);
    }

    /**
     * 显示图片
     *
     * @param imagePath            图片路径
     * @param imageView            图片控件
     * @param imageLoadingListener 加载回调
     */
    public static void displayImage(String imagePath, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        displayImage(imagePath, imageView, getDisplayImageOptions(imageView.getContext())
                , imageLoadingListener);
    }

    /**
     * 显示缩放图片
     *
     * @param context
     * @param imagePath
     * @param imageView
     * @param defaultDrawable
     */
    public static void displayZoomImage(Context context, String imagePath, ImageView imageView, int defaultDrawable, ImageLoadingListener imageLoadingListener) {
        if (TextUtils.isEmpty(imagePath) && defaultDrawable > 0) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, defaultDrawable));
            return;
        }
        if (options == null) {
            //显示图片的配置
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
            if (context != null) {
                Drawable loadedFailDrawable = context.getResources().getDrawable(R.drawable.default_bg2);
                builder.showImageOnFail(loadedFailDrawable);
            } else {
                builder.showImageOnFail(R.drawable.default_bg2);
            }
            builder.cacheInMemory(false)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .build();
            options = builder.build();
        }
        displayImage(imagePath, imageView, options, imageLoadingListener);
    }

    /**
     * 显示图片
     *
     * @param imagePath            图片路径
     * @param imageView            图片控件
     * @param options              配置
     * @param imageLoadingListener 加载回调
     */
    public static void displayImage(String imagePath, ImageView imageView, DisplayImageOptions options, ImageLoadingListener imageLoadingListener) {
        ImageLoader.getInstance().displayImage(imagePath, imageView, options
                , imageLoadingListener);
    }

    /**
     * 展示本地图片
     *
     * @param imagePath 图片路径
     * @param imageView 图片控件
     */
    public static void displayLocalImage(String imagePath, ImageView imageView) {
        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bmp);
    }

    /**
     * 显示图片
     *
     * @param imagePath       图片地址
     * @param imageView       图片View
     * @param defaultDrawable 加载时图片&默认图片
     */
    public static void displayImageCustomOption(String imagePath, ImageView imageView, int defaultDrawable) {
        DisplayImageOptions options;
        try {
            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(defaultDrawable)
                    .showImageOnFail(defaultDrawable)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        } catch (Exception ex) {
            options = getDisplayImageOptions(imageView.getContext());
        }
        ImageLoader.getInstance().displayImage(imagePath, imageView, options);
    }


    /**
     * 显示图片
     *
     * @param imagePath       图片地址
     * @param imageView       图片View
     * @param defaultDrawable 图片地址为空时默认图片
     */
    public static void displayImage(Context context, String imagePath, ImageView imageView, int defaultDrawable) {
        displayImage(context, imagePath, imageView, defaultDrawable, null);
    }

    /**
     * 显示图片
     *
     * @param imagePath       图片地址
     * @param imageView       图片View
     * @param defaultDrawable 图片地址为空时默认图片
     */
    public static void displayImage(Context context, String imagePath, ImageView imageView, int defaultDrawable, ImageLoadingListener imageLoadingListener) {
        if (TextUtils.isEmpty(imagePath)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, defaultDrawable));
            return;
        }
        displayImage(imagePath, imageView, imageLoadingListener);
    }


    /**
     * 展示本地图片
     *
     * @param imagePath
     * @param imageView
     */
    public static void displaySDCardImage(String imagePath, ImageView imageView) {
        String imageUrl = ImageDownloader.Scheme.FILE.wrap(imagePath);
        displayImage(imageUrl, imageView);

        //旧 ↓
//        String path = imagePath;
//        InputStream is = null;
//        try {
//            //1.加载位图
//            is = new FileInputStream(path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        //2.为位图设置100K的缓存
//        opts.inTempStorage = new byte[100 * 1024];
//        //3.设置位图颜色显示优化方式
//        opts.inPreferredConfig = Bitmap.Config.RGB_565;
//        //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
//        opts.inPurgeable = true;
//        //5.设置位图缩放比例
//        opts.inSampleSize = 2;
//        //6.设置解码位图的尺寸信息
//        opts.inInputShareable = true;
//        //7.解码位图
//        Bitmap bmp = BitmapFactory.decodeStream(is, null, opts);
//        //设置缩放
//        int width = bmp.getWidth();
//        int height = bmp.getHeight();
//        if (width / height > IMAGE_ZOOM_SCALE) {
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        } else {
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        }
//        //8.显示位图
//        imageView.setImageBitmap(bmp);
    }

    /**
     * @param imageView
     * @param width     (ViewGroup.LayoutParams.WRAP_CONTENT|ViewGroup.LayoutParams.MATCH_PARENT)
     * @param height    (ViewGroup.LayoutParams.WRAP_CONTENT|ViewGroup.LayoutParams.MATCH_PARENT)
     */
    public static void changeImageLayout(ImageView imageView, int screenWidth, int width, int height) {
        if (width < 98) {
            width = 98;
        }
        double imageScale = width / 656.0;
        int showWidth;
        if (imageScale == 1) {
            showWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            showWidth = (int) (imageScale * screenWidth);
        }
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = showWidth;
        para.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        if (width < 656) {
//            para.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            para.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        } else {
//            para.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            para.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        }
        imageView.setLayoutParams(para);
        imageView.setLayoutParams(para);
    }

    /**
     * @param picturePath 图片完整路径
     * @param width       宽度
     * @param height      高度
     * @param quality     质量
     * @return
     */
    public static String zoom(String picturePath, int width, int height, int quality) {

        if (TextUtils.isEmpty(picturePath)) {
            return picturePath;
        }

        if (!picturePath.contains("x-oss-process")) {
            picturePath = picturePath + "?x-oss-process=image/resize";
        } else {
            picturePath = picturePath + "/resize";
        }


        picturePath = picturePath + ",m_lfit,limit_1,w_" + width + ",h_" + height + "/quality,q_" + quality;

        return picturePath;
    }


}
