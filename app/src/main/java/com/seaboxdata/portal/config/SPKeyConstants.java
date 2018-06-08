package com.seaboxdata.portal.config;

/**
 * SharedPreferences中的key常量
 *
 * @author hshande@linewell.com
 * @since 2016/10/9
 */
public class SPKeyConstants {

    /**
     * 用户设置相关
     */
    public static class SETTING_SERVICES {

        /**
         * 消息通知设置
         */
        public static final String KEY_NOTIFY_SETTING = "NOTICE_SETTING";

        /**
         * 声音提示设置
         */
        public static final String KEY_SOUND_SETTING = "VOICE_SETTING";

        /**
         * 震动提示设置
         */
        public static final String KEY_SHOCK_SETTING = "SHOCK_SETTING";

        /**
         * 我的页面显示普通用户还是显示用户相关身份页面 true | false
         * KEY_MY_FRAGMENT_SETTING_ + userId
         */
        public static final String KEY_MY_FRAGMENT_SETTING = "KEY_MY_FRAGMENT_SETTING_";

    }
    public static class UPDATE_SERVICES {
        public static final String KEY_VERSION = "cacheVersion";
    }
}
