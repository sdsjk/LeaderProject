package com.linewell.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * MD5加密
 * @author Lzuwen@linewell.com
 * @since 2013-9-30
 */
public class MD5 {
	
	// 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

    /**
     * Default Constructor
     */
    public MD5() {
    }

    /**
     * 转换字节数组为形式为数字跟字符串
     * @param bByte 字节数组
     * @return  返回形式为数字跟字符串
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

//    /**
//     * 转换字节数组为数字
//     * @param bByte 字节数组
//     * @return 返回形式只为数字
//     */
//    private static String byteToNum(byte bByte) {
//        int iRet = bByte;
//        System.out.println("iRet1=" + iRet);
//        if (iRet < 0) {
//            iRet += 256;
//        }
//        return String.valueOf(iRet);
//    }

    /**
     * 转换字节数组为16进制字串
     * @param bByte 字节数组
     * @return 16进制字串
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * Md5加密方法
     * @param strObj 需加密的字符串
     * @return 返回Md5加密后的字符串
     */
    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        //返回Md5加密后的字符串
        return resultString.toUpperCase();
    }
}
