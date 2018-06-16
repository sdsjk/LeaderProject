package com.seaboxdata.portal.bean;

/**
 * Created by zhang on 2018/6/14.
 */

public class RemindBean {
    private int type;
    private String data;
    private String titleName;
    private String time;
    private String content;
    private String address;

    public RemindBean() {
    }

    public RemindBean(int type, String data, String titleName, String time, String content, String address) {
        this.type = type;
        this.data = data;
        this.titleName = titleName;
        this.time = time;
        this.content = content;
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
