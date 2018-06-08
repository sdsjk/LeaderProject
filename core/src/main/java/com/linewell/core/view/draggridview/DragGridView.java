package com.linewell.core.view.draggridview;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Collections;
import java.util.List;

/**
 * 支持拖拽的九宫格组件
 * 配合DragGridViewAdapter使用
 * @author lyixin
 * @since 2016/8/24.
 */
public class DragGridView<T> extends DragGridBaseView {
    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragGridView(Context context) {
        super(context);
    }

    /**
     * 设置拖拽adapter
     * @param adapter
     */
    public void setDragAdapter(final DragGridViewAdapter<T> adapter){
        setAdapter(adapter);

        setOnChangeListener(new OnChanageListener() {
            @Override
            public void onChange(int from, int to) {
                // 数据的重新排序

                List<T> dataSourceList = adapter.getDataList();
                T temp = dataSourceList.get(from);
                // 这里的处理需要注意下
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(dataSourceList, i, i + 1);
                    }
                } else if (from > to) {
                    for (int i = from; i > to; i--) {
                        Collections.swap(dataSourceList, i, i - 1);
                    }
                }

                dataSourceList.set(to, temp);
                adapter.setDataList(dataSourceList);

                //隐藏当前项
                adapter.setInVisibleIndex(to);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish(int inVisibleIndex) {
                adapter.setInVisibleIndex(-1);
                adapter.notifyDataSetChanged();
                adapter.setShowGuide(false);

            }

            @Override
            public void onNothingClick() {

            }

            @Override
            public void onLongClick(int index) {
                adapter.setInVisibleIndex(index);
            }

            @Override
            public void onDragToLast(int dragIndex) {

            }
        });
    }
}
