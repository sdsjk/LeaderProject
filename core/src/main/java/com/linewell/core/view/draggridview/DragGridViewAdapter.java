package com.linewell.core.view.draggridview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 拖拽adapter
 *
 * @author lyixin
 * @since 2016/8/24
 */
public abstract class DragGridViewAdapter<T> extends BaseAdapter {

    private List<T> dataList;

    /**
     * 隐藏的项的索引
     */
    private int inVisibleIndex = -1;



    private boolean showGuide = false;

    public DragGridViewAdapter(List<T> dataList) {
        this.dataList = dataList;
    }


    public void setInVisibleIndex(int inVisibleIndex) {
        this.inVisibleIndex = inVisibleIndex;
    }

    public int getInVisibleIndex() {
        return inVisibleIndex;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public List<T> getDataList() {
        return dataList;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View view = getCustomView(position, convertView, parent);
        view.setVisibility(position == inVisibleIndex ? View.INVISIBLE : View.VISIBLE);
        return view;
    }

    public abstract View getCustomView(int position, View convertView, ViewGroup parent);

    /**
     * 设置是否显示可拖拽提示
     * @param showGuide
     */
    public void setShowGuide(boolean showGuide) {
        this.showGuide = showGuide;
    }
    /**
     * 是否显示可拖拽提示
     */
    public boolean isShowGuide() {
        return showGuide;
    }
}
