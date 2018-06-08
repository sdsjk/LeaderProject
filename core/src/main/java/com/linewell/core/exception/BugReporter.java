package com.linewell.core.exception;

/**
 * bug报告工具
 * @author lyixin
 * @since 2017/5/25.
 */

public class BugReporter {

    /**
     * 报告器
     */
    private IBugReporter iBugReporter;

    /**
     * 单例
     */
    private static final BugReporter instance = new BugReporter();

    private BugReporter(){

    }

    public static BugReporter getInstance(){
        return instance;
    }

    public void postException(Exception e){
        if(iBugReporter!=null){
            iBugReporter.postException(e);
        }
    }

    public static BugReporter with(IBugReporter iBugReporter){
        instance.setIBugReporter(iBugReporter);
        return instance;
    }

    public void setIBugReporter(IBugReporter iBugReporter) {
        this.iBugReporter = iBugReporter;
    }
}
