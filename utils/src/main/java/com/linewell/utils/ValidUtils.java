package com.linewell.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 * Created by caicai on 2016-07-12.
 */
public class ValidUtils {

    /**
     * 只允许字母、数字和汉字
     *
     * @param str 字符串
     * @return
     */
    public static boolean stringFilter(String str) {
        // 只允许字母、数字和汉字
        String regEx = "[a-zA-Z0-9\\u4e00-\\u9fa5]+";
//        String regEx = "[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z0-9\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证手机号
     *
     * @param phone 电话号码
     * @return
     */
    public static boolean isPhoneValid(String phone) {
//        Pattern p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        Pattern p = Pattern.compile("^(1)[3456789][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(phone);

        return m.matches();
    }

    /**
     * 验证固话或手机号码
     *
     * @param phone 电话号码
     * @return
     */
    public static boolean isTelOrMobileValid(String phone) {
        if (ValidUtils.isPhoneValid(phone)) {
            return ValidUtils.isPhoneValid(phone);
        }

        if (ValidUtils.isTelValid(phone)) {
            return ValidUtils.isTelValid(phone);
        }
        return false;
    }

    /**
     * 验证固话或手机号码
     *
     * @param phone 电话号码
     * @return
     */
    public static boolean isTelValid(String phone) {
        Pattern pTel = Pattern.compile("^(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$"); // 验证固话
        Matcher mTel = pTel.matcher(phone);
        return mTel.matches();
    }

    /**
     * 验证邮箱
     *
     * @param mail 邮箱
     * @return
     */
    public static boolean isMailValid(String mail) {
//        Pattern p = Pattern.compile("^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$"); // 验证邮箱
        Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"); // 验证邮箱
        Matcher m = p.matcher(mail);
        return m.matches();
    }

    /**
     * 验证密码规则(必须是数字与字母组合)
     *
     * @param psw
     * @return
     */
    public static boolean verifyPsw(String psw) {

        Pattern p = Pattern.compile("^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{6,18}$");
        //Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$"); // 验证新密码
        Matcher m = p.matcher(psw);
        return m.matches();

    }


    /**
     * 快递编号验证
     *
     * @param num
     * @return
     */
    public static boolean validExpressNumber(String num) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    /**
     * 验证中文字符
     *
     * @param str
     * @return
     */
    public static boolean validChinese(String str) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
        Matcher m = p.matcher(str.trim());
        return m.matches();
    }

    /**
     * 验证汉字或字母
     *
     * @param str
     * @return
     */
    public static boolean validChineseOrWord(String str) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证数字或字母
     *
     * @param str
     * @return
     */
    public static boolean validNumOrWord(String str) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证证件编号
     *
     * @param str
     * @return
     */
    public static boolean validCerCode(String str) {
        Pattern p = Pattern.compile("^[A-Za-z0-9~！!@#￥%……&（）\\(\\)——\\+\\{\\}\\|：”《》？`·【】、；‘’，。、<>,\\.\\/\\\\\\[\\];\\\"\\\"]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证汉字、字母、数字
     *
     * @param str
     * @return
     */
    public static boolean validChineseNumWord(String str) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证密码规则
     *
     * @param str
     * @return
     */
    public static boolean validPassword(String str) {
        Pattern p = Pattern.compile("^[A-Za-z0-9~！!@#￥%……&（）\\(\\)——\\+\\{\\}\\|：”《》？`·【】、；‘’，。、<>,\\.\\/\\\\\\[\\];\\\"\\\"]{6,18}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * *；，三种符号的验证
     *
     * @param str
     * @return
     */
    public static boolean validLabel(String str) {
        Pattern p = Pattern.compile(".*[\\*,，;；]+.*");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 匹配中国邮政编码
     * @param postCode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPostCode(String postCode){
        String reg = "[1-9]\\d{5}";
        return Pattern.matches(reg, postCode);
    }
}
