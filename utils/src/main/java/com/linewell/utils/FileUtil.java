package com.linewell.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.zeroturnaround.zip.commons.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

/**
 * @Copyright :(C),2014
 * @CompanyName :南威软件股份有限公司(www.linewell.com)
 * @Version :1.0
 * @Date :2014年8月21日
 * @author : 文件创建者姓名：李竞雄 ljingxiong@linewell.com
 * @Description :
 */
public class FileUtil {

	public static final String HIDDEN_PREFIX = ".";

	/**
	 * 私有构造函数
	 */
	private FileUtil() {
	}

	/**
	 * 将文件挎贝到指定的目录
	 * 
	 * @param srcFilePath
	 *            要挎贝的文件
	 * @param destDirPath
	 *            挎贝到哪个目录
	 */
	public static void copyFileToDirectory(String srcFilePath, String destDirPath) {
		File srcFile = new File(srcFilePath);
		File destDir = new File(destDirPath);
		try {
			FileUtils.copyFileToDirectory(srcFile, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void deleteQuietly(File file) {
		FileUtils.deleteQuietly(file);
	}

	/**
	 * 删除目录
	 * 
	 * @param directory
	 */
	public static void deleteDirectory(File directory) {
		try {
			FileUtils.deleteDirectory(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件（夹）比较器 （按字母升序排列）
	 */
	public static Comparator<File> sComparator = new Comparator<File>() {
		@Override
		public int compare(File f1, File f2) {
			// Sort alphabetically by lower case, which is much cleaner
			return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
		}
	};

	/**
	 * 文件过滤器
	 */
	public static FileFilter sFileFilter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			final String fileName = file.getName();
			// Return files only (not directories) and skip hidden files
			return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
		}
	};

	/**
	 * 文件夹过滤器
	 */
	public static FileFilter sDirFilter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			final String fileName = file.getName();
			// Return directories only and skip hidden directories
			return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
		}
	};

	/**
	 * 调用系统程序打开相应的文件
	 * 
	 * @param mContext
	 *            上下文
	 * @param file
	 *            要打开的文件
	 */
	public static void openFile(Context mContext, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String mimeType = getMimeType(file);
		intent.setDataAndType(Uri.fromFile(file), mimeType);
		mContext.startActivity(intent);
	}

	/**
	 * 根据文件信息获取MIMETYPE
	 * 
	 * @param file
	 *            文件对象
	 * @return
	 */
	public static String getMimeType(File file) {

		String extension = getExtension(file.getName());
		
		extension = extension.toLowerCase();

		if (extension.length() > 0)
			return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

		return "application/octet-stream";
	}

	/**
	 * 获取文件的扩展名
	 * 
	 * @param fileName
	 *            文件名
	 * @return 如果没有扩展名，则返回"";如果fileName 为空，则返回空
	 */
	public static String getExtension(String fileName) {
		if (fileName == null) {
			return null;
		}

		int dot = fileName.lastIndexOf(".");
		if (dot >= 0) {
			return fileName.substring(dot);
		} else {
			return "";
		}
	}

	/**
	 * 获取某个文件夹下的所有文件大小
	 * 
	 * @param path
	 *            文件夹的路径
	 * @return 文件大小
	 */
	public static long getFileSize(String path) {

		long fileSize = 0;
		if (StringUtil.isNull(path)) {
			return fileSize;
		}

		File file = new File(path);
		if (file != null && file.exists()) {
			File[] flist = file.listFiles();
			if (file !=null&&flist !=null && flist.length != 0) {
				for (int i = 0, len = flist.length; i < len; i++) {
					fileSize += flist[i].length();
				} // end for
			}// end if
		}// end if

		return fileSize;

	}

	/**
	 * 根据文件的路径获取文件的大小
	 * 
	 * @param filePath
	 *            文件的路径
	 * @return 文件的大小
	 */
	public static long getSingleFileSize(String filePath) {
		long fileSize = 0;
		if (StringUtil.isNull(filePath)) {
			return fileSize;
		}
		File f = new File(filePath);
		if (f.exists() && f.isFile()) {
			fileSize = f.length();
		}
		return fileSize;
	}

	/**
	 * 根据指定路径，获取其下所有文件的路径
	 * 
	 * @param path
	 *            文件路径
	 * @return List<Stirng> 路径列表
	 */
	public static List<String> getFileAbsolutePath(String path) {

		List<String> pathList = null;

		if (StringUtil.isNull(path)) {
			return pathList;
		}

		File file = new File(path);
		if (file != null && file.exists()) {
			File[] flist = file.listFiles();
			if (flist.length != 0) {
				pathList = new ArrayList<String>();
				for (int i = 0, len = flist.length; i < len; i++) {
					pathList.add(flist[i].getAbsolutePath());
				} // end for
			}// end if
		}// end if

		return pathList;
	}

	/**
	 * 将文件大小转换成友好的方式
	 * 
	 * @param size
	 *            文件的大小
	 * @return
	 */
	public static String getReadableFileSize(long size) {
		final int BYTES_IN_KILOBYTES = 1024;
		final DecimalFormat dec = new DecimalFormat("###.#");
		final String BYTES = "B";
		final String KILOBYTES = "KB";
		final String MEGABYTES = "MB";
		final String GIGABYTES = "GB";
		float fileSize = 0;
		String suffix = KILOBYTES;

		if (size > BYTES_IN_KILOBYTES) {
			fileSize = size / BYTES_IN_KILOBYTES;
			if (fileSize > BYTES_IN_KILOBYTES) {
				fileSize = fileSize / BYTES_IN_KILOBYTES;
				if (fileSize > BYTES_IN_KILOBYTES) {
					fileSize = fileSize / BYTES_IN_KILOBYTES;
					suffix = GIGABYTES;
				} else {
					suffix = MEGABYTES;
				}
			}
		} else {
			fileSize = size;
			suffix = BYTES;
		}
		return String.valueOf(dec.format(fileSize) + suffix);
	}

	/**
	 * 根据绝对路径获取文件名
	 * 
	 * @param absoluteDir
	 *            绝对路径
	 * @return 文件名
	 */
	public static String getFileNameByAbsoluteDir(String absoluteDir) {
		File file = new File(absoluteDir);
		String fileName = file.getName();
		return fileName;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 *            文件的绝对路径
	 * @return 存在返回true，否则返回false
	 */
	public static boolean isFileExists(String filePath) {
		return new File(filePath).exists();
	}

	/**
	 * 判断文件夹是否存在，不存在则创建
	 *
	 * @param strFolder 指定的路径
	 * @return
	 */
	public static boolean isFolderExists(String strFolder) {
		File file = new File(strFolder);
		if (!file.exists()) {
			if (file.mkdirs()) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 计算文件的MD5值
	 *
	 * @param f 文件
	 * @return
	 */
	public static String getFileMd5(File f) {
		if(f.exists()){
			MessageDigest digest = null;
			FileInputStream in = null;
			byte buffer[] = new byte[1024];
			int len;
			try {
				digest = MessageDigest.getInstance("MD5");
				in = new FileInputStream(f);
				while ((len = in.read(buffer, 0, 1024)) != -1) {
					digest.update(buffer, 0, len);
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			BigInteger bigInt = new BigInteger(1, digest.digest());
			return bigInt.toString(16);
		}

		return null;
	}
}
