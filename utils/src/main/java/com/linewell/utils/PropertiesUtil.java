package com.linewell.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources.NotFoundException;

/**
 * @Copyright :(C),2014
 * @CompanyName :南威软件股份有限公司(www.linewell.com)
 * @Version :1.0
 * @Date :2014年8月7日
 * @author : 文件创建者姓名：李竞雄 ljingxiong@linewell.com
 * @Description : properties配置文件工具类
 */
public class PropertiesUtil {
	private Properties p;

	/**
	 * 私有化无参构造函数
	 */
	private PropertiesUtil() {
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param rawPropertiesFile
	 *            要读取raw下的xxx.properties文件在R.java中的值
	 */
	public PropertiesUtil(Context context, int rawPropertiesFile) {
		p = new Properties();
		try {
			p.load(context.getResources().openRawResource(rawPropertiesFile));
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param context上下文
	 * @param assertPropertiesFile
	 *            要读取assert下的xxx.properties文件的路径
	 */
	public PropertiesUtil(Context context, String assertPropertiesFile) {
		p = new Properties();
		try {
			p.load(context.getAssets().open(assertPropertiesFile));
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public PropertiesUtil(String filePath) {
		p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			p.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param file
	 *            文件
	 */
	public PropertiesUtil(File file) {
		p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(file);
			p.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据Key获取Value值
	 * 
	 * @param key
	 * @return 返回value值
	 */
	public String getValue(String key) {
		return p.getProperty(key);
	}

	/**
	 * 保存属性文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param properties
	 *            属性对象
	 */
	public static void saveProperties(String filePath, Properties properties) {
		try {
			File f = new File(filePath);
			if (f.exists() == true) {
				FileInputStream fileInputStream_temp = new FileInputStream(filePath);
				Properties properties_temp = new Properties();
				properties_temp.load(fileInputStream_temp);
				if (properties != null && properties.size() > 0) {
					properties_temp.putAll(properties);
				}
				FileOutputStream s = new FileOutputStream(filePath, false);
				properties_temp.store(s, "");
			} else {
				FileOutputStream s = new FileOutputStream(filePath, false);
				properties.store(s, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
