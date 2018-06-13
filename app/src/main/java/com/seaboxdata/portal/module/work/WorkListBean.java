package com.seaboxdata.portal.module.work;

/**
 * Created by zhang on 2018/6/13.
 */

public class WorkListBean {
    private String fristName;
    private String title;
    private String content;
    private String time;

    public WorkListBean(String fristName, String title, String content, String time) {
        this.fristName = fristName;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public WorkListBean() {
    }

    public void setFristName(String fristName) {
        this.fristName = fristName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFristName() {
        return fristName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
