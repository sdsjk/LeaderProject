package com.linewell.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义gridview，解决ListView中嵌套gridview显示不正常的问题（1行半）
 * @author sxiangyu@linewell.com
 * @since 2014-8-21
 */
public class BanSlidingGridView extends GridView {

	public BanSlidingGridView(Context context, AttributeSet attrs) {   
        super(context, attrs);   
    }   
   
    public BanSlidingGridView(Context context) {   
        super(context);   
    }   
   
    public BanSlidingGridView(Context context, AttributeSet attrs, int defStyle) {   
        super(context, attrs, defStyle);   
    }   
   
    @Override   
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
   
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,   
                MeasureSpec.AT_MOST);   
        super.onMeasure(widthMeasureSpec, expandSpec);   
    }   
}
