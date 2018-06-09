package com.seaboxdata.portal.module.home;

/**
 * Created by zhang on 2018/6/9.
 */

public class HomeInfoBean {
    private String info;
    private String infotime;

    public HomeInfoBean() {
    }

    public HomeInfoBean(String info, String infotime) {
        this.info = info;
        this.infotime = infotime;
    }

    public String getInfo() {
        return info;
    }

    public String getInfotime() {
        return infotime;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setInfotime(String infotime) {
        this.infotime = infotime;
    }
}
