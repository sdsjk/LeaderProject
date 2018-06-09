package com.seaboxdata.portal.module.home;

/**
 * Created by zhang on 2018/6/9.
 */

public class HomeFragmentYwBean {
    private int icon;
    private String iconName;

    public HomeFragmentYwBean(int icon, String iconName) {
        this.icon = icon;
        this.iconName = iconName;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getIcon() {
        return icon;
    }

    public String getIconName() {
        return iconName;
    }
}
