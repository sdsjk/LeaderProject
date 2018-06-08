package com.linewell.core.activity;

import android.view.View;

/**
 * 错误页面的接口
 * @author lyixin
 * @since 2017/6/13.
 */

public interface IErrorView {

    public View initErrorView();

    public void showErrorView();

    public void hideErrorView();

    public void setOnErrorClickListener(View.OnClickListener onClickListener);
}
