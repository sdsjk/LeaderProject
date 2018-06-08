package com.seaboxdata.portal.module.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.kelin.scrollablepanel.library.PanelAdapter;
import com.kelin.scrollablepanel.library.ScrollablePanel;

/**
 * Created by zjianning on 2018/5/18.
 */

public class DataScrollablePanel extends ScrollablePanel {
    public DataScrollablePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public DataScrollablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DataScrollablePanel(Context context, PanelAdapter panelAdapter) {
        super(context, panelAdapter);
    }

    public RecyclerView getContentRecyclerView() {
        return recyclerView;
    }

    public RecyclerView getHeaderRecyclerView() {
        return headerRecyclerView;
    }


}
