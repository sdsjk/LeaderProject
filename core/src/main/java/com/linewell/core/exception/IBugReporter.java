package com.linewell.core.exception;

/**
 * @author lyixin
 * @since 2017/5/25.
 */

public interface IBugReporter {

    /**
     * 报告异常
     * @param e
     */
    public void postException(Exception e);
}
