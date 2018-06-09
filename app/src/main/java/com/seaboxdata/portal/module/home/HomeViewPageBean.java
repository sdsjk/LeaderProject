package com.seaboxdata.portal.module.home;

/**
 * Created by zhang on 2018/6/9.
 */

public class HomeViewPageBean {
    private int iconResouse;
    private String messageInfo;

    public HomeViewPageBean(int iconResouse, String messageInfo) {
        this.iconResouse = iconResouse;
        this.messageInfo = messageInfo;
    }

    public int getIconResouse() {
        return iconResouse;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setIconResouse(int iconResouse) {
        this.iconResouse = iconResouse;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }
}
