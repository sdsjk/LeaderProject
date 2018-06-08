package com.linewell.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 固定比例的图片
 * @author lyixin
 * @since 2016/7/26
 */
public class FixImageView extends ImageView {
    public FixImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixImageView(Context context) {
        super(context);
    }

    public int getHeight(int width){
        int height = width*9/16;
        return height;
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getHeight(childWidthSize);

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec =MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

