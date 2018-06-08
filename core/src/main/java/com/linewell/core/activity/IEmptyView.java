package com.linewell.core.activity;

import android.view.View;

/**
 * 空页面的接口
 * @author lyixin
 * @since 2017/6/13.
 */

public interface IEmptyView {

    public View initEmptyView();

    public void showEmptyView();

    public void hideEmptyView();

    public void setOnEmptyClickListener(View.OnClickListener onClickListener);
}
