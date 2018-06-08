package com.linewell.core.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 自定义Listview，解决ScrollView中嵌套ListView显示不正常的问题（1行半）
 * @author lyixin
 * @since 2016/10/27
 */
public class BanSlidingRecyclerView extends RecyclerView {

	public BanSlidingRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BanSlidingRecyclerView(Context context) {
		super(context);
	}

	public BanSlidingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
