package com.linewell.core.activity;

import android.view.View;

/**
 * 加载页面的接口
 * @author lyixin
 * @since 2017/6/13.
 */

public interface ILoadingView {

    public View initLoadingView();

    public void showLoadingView();

    public void hideLoadingView();
}
