package com.linewell.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义Listview，解决ScrollView中嵌套ListView显示不正常的问题（1行半）
 * 
 * @author lzhiwei@linewell.com
 * @since 2014-9-12
 * 
 */
public class BanSlidingListView extends ListView {

	public BanSlidingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BanSlidingListView(Context context) {
		super(context);
	}

	public BanSlidingListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
