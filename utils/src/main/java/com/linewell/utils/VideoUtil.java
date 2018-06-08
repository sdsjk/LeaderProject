package com.linewell.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.util.Log;

/**
 * @Copyright :(C),2014
 * @CompanyName :南威软件股份有限公司(www.linewell.com)
 * @Version :1.0
 * @Date :2014年8月5日
 * @author : 文件创建者姓名：李竞雄 ljingxiong@linewell.com
 * @Description :
 */
public class VideoUtil {

	private static final String TAG = "VideoUtil";

	public static String videoToString(String filePath) {
		BufferedInputStream in = null;
		ByteArrayOutputStream baos = null;
		try {
			File tmpFile = new File(filePath);
			in = new BufferedInputStream(new FileInputStream(tmpFile));
			baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[in.available()];
			int length;
			while ((length = in.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
			byte[] b = baos.toByteArray();
			return Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			Log.e(TAG, "读取视频出现异常", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e(TAG, "关闭视频出现异常", e);
				}
			}
		}
		return null;
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

}
