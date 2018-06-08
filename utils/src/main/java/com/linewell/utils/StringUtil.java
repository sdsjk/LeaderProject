package com.linewell.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 编码格式默认为utf-8
	 */
	public static final String UTF_8 = "utf-8";

	/**
	 * 字符串分隔符 ,
	 */
	public static final String STRINGSPLIT = ",";

	/**
	 * URL分隔符
	 */
	public static final String URL_SPLIT = "/";

	/**
	 * 中横线
	 */
	public static final String HYPHEN = "-";
	
	/**
	 * 把字符串转化为相应的boolean值，根据平台定义规定，当值为“1”时，返回true， 否则返回false
	 * 
	 * @param val
	 *            待解析的字符串 String
	 * 
	 * @return 解析后的结果 boolean
	 */
	public static boolean parseBoolean(String val) {
		boolean result = false;

		if (null != val
				&& ("1".equals(val.trim()) || "true".equals(val.trim()))) {
			result = true;
		}

		return result;
	}

	/**
	 * 把字符串转化成相应的整数
	 * 
	 * @param val
	 *            待转换的字符串 String
	 * 
	 * @return 转换后的结果 int
	 */
	public static int parseInt(String val) {
		int result = 0;

		if (null == val || val.trim().equals(""))
			return result;

		String tmpVal = val;
		if (val.startsWith("-")) {
			tmpVal = val.substring(1);
		}

		if (!tmpVal.trim().equals("")) {// 如果是数字型就做相应的转换
			result = Integer.parseInt(val);
		}

		return result;
	}

	/**
	 * 把boolean值转化成相应的字符串
	 * 
	 * @param val
	 *            待转换的boolean值 boolean
	 * 
	 * @return 转换后的结果 true:1;false:0
	 */
	public static String valueOf(boolean val) {
		return val ? "1" : "0";
	}

	/**
	 * 转换为字符串
	 * 
	 * @param obj
	 *            数组对象 Object[]
	 * @param containInvertedComma
	 *            是否包含单引号 boolean
	 * @return containInvertedComma为false:返回“信息1，信息2，信息3，...”形式的字符串
	 *         containInvertedComma为true:返回“'信息1'，'信息2'，'信息3'，...”形式的字符串
	 *         若输入参数不合法，则返回NULL
	 */
	public static String toString(Object[] obj, boolean containInvertedComma) {
		return StringUtil.toString(Arrays.asList(obj), containInvertedComma);
	}

	/**
	 * 转换为字符串
	 * 
	 * @param list
	 *            List对象 List<?>
	 * 
	 * @param containInvertedComma
	 *            是否包含单引号 boolean
	 * 
	 * @return containInvertedComma为false:返回“信息1，信息2，信息3，...”形式的字符串
	 *         containInvertedComma为true:返回“'信息1'，'信息2'，'信息3'，...”形式的字符串
	 *         若输入参数不合法，则返回NULL String
	 */
	public static String toString(List<?> list, boolean containInvertedComma) {
		// 检查输入参数合法性
		if (list == null || list.size() == 0)
			return null;

		StringBuffer returnStr = new StringBuffer();

		// 添加信息项
		int listMaxIndex = list.size() - 1;
		for (int index = 0; index < listMaxIndex; index++) {
			Object value = list.get(index);
			if (containInvertedComma) {
				returnStr.append("'" + value + "'");
			} else {
				returnStr.append(value);
			}
			returnStr.append(",");
		}

		// 添加最后一条信息项
		Object lastIndexValue = list.get(listMaxIndex);
		if (containInvertedComma) {
			returnStr.append("'" + lastIndexValue + "'");
		} else {
			returnStr.append(lastIndexValue);
		}

		return returnStr.toString();
	}

	/**
	 * 把两个字符串列表组合起来
	 * 
	 * @param list1
	 *            字符串列表 List<String>
	 * 
	 * @param list2
	 *            字符串列表 List<String>
	 * 
	 * @return 组合后的字符串列表 List<String>
	 */
	public static List<String> composeList(List<String> list1,
			List<String> list2) {
		List<String> resultList = list1;

		if (null == resultList)
			return list2;

		if (null != list2 && !list2.isEmpty()) {

			for (int i = 0; i < list2.size(); i++) {
				if (resultList.contains(list2.get(i)))
					continue;
				resultList.add(list2.get(i));
			}

		}

		return resultList;
	}

	/**
	 * 把一个object对象转化成字符串
	 * 
	 * @param obj
	 *            object对象 Object
	 * 
	 * @return 返回结果值 String
	 */
	public static String convertObjectToString(Object obj) {
		if (null == obj) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * 将字符串转化成 InputStream 对象
	 * 
	 * @param str
	 *            待转换的字符串 String
	 * 
	 * @return InputStream 对象 InputStream
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * 判断一个字符串是否为32位的UNID
	 * 
	 * @param unid
	 *            待判断字符串 String
	 * 
	 * @return 是UNID格式的，返回true，否则返回false
	 */
	public static boolean validateUnid(String unid) {
		boolean result = false;

		if (null != unid) {
			Pattern pattern = Pattern.compile("[0-9A-F]{32}");
			Matcher matcher = pattern.matcher(unid);
			result = matcher.matches();
		}

		return result;
	}

	/**
	 * 进行对url字符串进行编码
	 * 
	 * @param url
	 *            url地址
	 * @param encode
	 *            编码格式
	 * @return 编码后的url
	 */
	public static String encodeUrl(String url, String encode) {
		if ("".equals(url) || null == url) {
			return "";
		}
		try {
			url = java.net.URLEncoder.encode(url, encode);
		} catch (Exception ex) {
			System.out.println("url：" + url + " URL编码失败！");
		}
		return url;
	}

	/**
	 * url编码（编码格式默认为utf-8）
	 * 
	 * @param url
	 * @return
	 */
	public static String encodeUrl(String url) {
		return encodeUrl(url, UTF_8);
	}

	/**
	 * 进行对url字符串进行解码操作
	 * 
	 * @param url
	 *            url地址
	 * @param encode
	 *            编码格式
	 * @return
	 */
	public static String decodeUrl(String url, String encode) {
		if ("".equals(url) || null == url) {
			return "";
		}
		try {
			url = java.net.URLDecoder.decode(url, encode);
		} catch (Exception ex) {
			System.out.println("url：" + url + " URL解码失败！");
		}
		return url;
	}

	/**
	 * 进行对url字符串进行解码操作(默认格式为utf-8)
	 * 
	 * @param url
	 * @return
	 */
	public static String decodeUrl(String url) {
		return decodeUrl(url, UTF_8);
	}

	/**
	 * 对字符串根据分隔符转化为List
	 * 
	 * @param str
	 * @param delim
	 * @return a list of Strings
	 */
	public static List split(String str, String delim) {
		List splitList = null;
		StringTokenizer st = null;

		if (str == null)
			return splitList;

		if (delim != null)
			st = new StringTokenizer(str, delim);
		else
			st = new StringTokenizer(str);

		if (st != null && st.hasMoreTokens()) {
			splitList = new ArrayList();

			while (st.hasMoreTokens())
				splitList.add(st.nextToken());
		}
		return splitList;
	}

	/**
	 * 通过字符串参数替换字符串，如：测试{0}，替换成:测试替换的参数1
	 * 
	 * @param sourceStr
	 *            源字符串
	 * @param replaceStrings
	 *            替换字符串的不定参数
	 * @return 替换的结果
	 */
	public static String replaceStr(String sourceStr, String... replaceStrings) {
		if (replaceStrings.length == 0) {
			return sourceStr;
		}
		Object[] repalceObjectArry = replaceStrings;
		return MessageFormat.format(sourceStr, repalceObjectArry);
	}

	/**
	 * 邮件正则表达式
	 */
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	/**
	 * 判断给定字符串是否空白串。 <br>
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 功能：格式化空字符串
	 * 
	 * @param str
	 * @return String
	 */
	public static String formatNull(String str) {
		return null == str || "null".equals(str) ? "" : str;
	}

	/**
	 * 功能：格式化空字符串,为空时返回默认字符串
	 * 
	 * @param str
	 *            原字符串
	 * @param defaultStr
	 *            默认字符串
	 * @return String
	 */
	public static String formatNull(String str, String defaultStr) {
		return isNull(str) ? defaultStr : str;
	}

	/**
	 * 功能：判断字符串是否为空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNull(String str) {
		return null == str || "".equals(str) || "null".equals(str);
	}

	/**
	 * 判断字符数组是否为空,如果有一个为空，则返回true
	 * 
	 * @param strs
	 *            字符串数组
	 * @return
	 */
	public static boolean isEmpty(String... strs) {
		boolean status = false;
		for (String str : strs) {
			if (isNull(str)) {
				status = true;
			}
		}
		return status;
	}

	/**
	 * 判断文件或路径是否为空，有一个为空，则返回true
	 */
	public static boolean isEmpty(File... files) {
		boolean status = false;
		for (File file : files) {
			if (!file.exists()) {
				status = true;
			}
		}
		return status;
	}

	/**
	 * 判断一个字符串是否是整形
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if(isEmpty(str)){
			return false;
		}
		
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	public static <T> String join(T... elements) {
		return join((Object[])elements, (String)null);
	}

	public static String join(Object[] array, char separator) {
		return array == null?null:join((Object[])array, separator, 0, array.length);
	}

	public static String join(long[] array, char separator) {
		return array == null?null:join((long[])array, separator, 0, array.length);
	}

	public static String join(int[] array, char separator) {
		return array == null?null:join((int[])array, separator, 0, array.length);
	}

	public static String join(short[] array, char separator) {
		return array == null?null:join((short[])array, separator, 0, array.length);
	}

	public static String join(byte[] array, char separator) {
		return array == null?null:join((byte[])array, separator, 0, array.length);
	}

	public static String join(char[] array, char separator) {
		return array == null?null:join((char[])array, separator, 0, array.length);
	}

	public static String join(float[] array, char separator) {
		return array == null?null:join((float[])array, separator, 0, array.length);
	}

	public static String join(double[] array, char separator) {
		return array == null?null:join((double[])array, separator, 0, array.length);
	}

	public static String join(Object[] array, char separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					if(array[i] != null) {
						buf.append(array[i]);
					}
				}

				return buf.toString();
			}
		}
	}

	public static String join(long[] array, char separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					buf.append(array[i]);
				}

				return buf.toString();
			}
		}
	}

	public static String join(int[] array, char separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					buf.append(array[i]);
				}

				return buf.toString();
			}
		}
	}

	public static String join(byte[] array, char separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					buf.append(array[i]);
				}

				return buf.toString();
			}
		}
	}

	public static String join(short[] array, char separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					buf.append(array[i]);
				}

				return buf.toString();
			}
		}
	}

	public static String join(char[] array, char separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					buf.append(array[i]);
				}

				return buf.toString();
			}
		}
	}

	public static String join(double[] array, char separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					buf.append(array[i]);
				}

				return buf.toString();
			}
		}
	}

	public static String join(float[] array, char separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					buf.append(array[i]);
				}

				return buf.toString();
			}
		}
	}

	public static String join(Object[] array, String separator) {
		return array == null?null:join(array, separator, 0, array.length);
	}

	public static String join(Object[] array, String separator, int startIndex, int endIndex) {
		if(array == null) {
			return null;
		} else {
			if(separator == null) {
				separator = "";
			}

			int noOfItems = endIndex - startIndex;
			if(noOfItems <= 0) {
				return "";
			} else {
				StringBuilder buf = new StringBuilder(noOfItems * 16);

				for(int i = startIndex; i < endIndex; ++i) {
					if(i > startIndex) {
						buf.append(separator);
					}

					if(array[i] != null) {
						buf.append(array[i]);
					}
				}

				return buf.toString();
			}
		}
	}

	private static String getString(Object obj) {
		return obj == null?"":obj.toString();
	}

	public static String join(Iterator<?> iterator, char separator) {
		if(iterator == null) {
			return null;
		} else if(!iterator.hasNext()) {
			return "";
		} else {
			Object first = iterator.next();
			if(!iterator.hasNext()) {
				String buf1 = getString(first);
				return buf1;
			} else {
				StringBuilder buf = new StringBuilder(256);
				if(first != null) {
					buf.append(first);
				}

				while(iterator.hasNext()) {
					buf.append(separator);
					Object obj = iterator.next();
					if(obj != null) {
						buf.append(obj);
					}
				}

				return buf.toString();
			}
		}
	}

	public static String join(Iterator<?> iterator, String separator) {
		if(iterator == null) {
			return null;
		} else if(!iterator.hasNext()) {
			return "";
		} else {
			Object first = iterator.next();
			if(!iterator.hasNext()) {
				String buf1 = getString(first);
				return buf1;
			} else {
				StringBuilder buf = new StringBuilder(256);
				if(first != null) {
					buf.append(first);
				}

				while(iterator.hasNext()) {
					if(separator != null) {
						buf.append(separator);
					}

					Object obj = iterator.next();
					if(obj != null) {
						buf.append(obj);
					}
				}

				return buf.toString();
			}
		}
	}

	public static String join(Iterable<?> iterable, char separator) {
		return iterable == null?null:join(iterable.iterator(), separator);
	}

	public static String join(Iterable<?> iterable, String separator) {
		return iterable == null?null:join(iterable.iterator(), separator);
	}

}
