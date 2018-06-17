package com.seaboxdata.portal.bean;

/**
 * Created by zhang on 2018/6/16.
 */

public class MoreInfoBean {
    private  int index;
    private String title;
    private String content;
    private int  status;

    public MoreInfoBean(int index, String title, String content, int status) {
        this.index = index;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
