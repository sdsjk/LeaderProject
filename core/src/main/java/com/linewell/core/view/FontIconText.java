package com.linewell.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.linewell.core.R;
import com.linewell.core.exception.BugReporter;

import java.util.HashMap;
import java.util.Map;

/**
 * 字体图标的textView
 */
public class FontIconText extends TextView {

    public static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();

    public FontIconText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public FontIconText(Context context) {
        this(context, null);
    }

    /**
     * 自定义属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public FontIconText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setTypeface(context, attrs, this);
    }

    public void setTypeface(Context context, AttributeSet attrs, TextView textView) {
        if (textView.isInEditMode()) return;

        context = context.getApplicationContext();

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.FontIconText);
        String typefaceName = values.getString(R.styleable.FontIconText_typefacename);

//        if (TextUtils.isEmpty(typefaceName)) {
//            // 默认的图标库 TODO
//            typefaceName = "";
//        }

        if (typefaceCache.containsKey(typefaceName)) {
            textView.setTypeface(typefaceCache.get(typefaceName));
        } else {
            Typeface typeface;
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.assets_fonts_folder) + typefaceName);
            } catch (Exception e) {
                BugReporter.getInstance().postException(e);
                return;
            }
            typefaceCache.put(typefaceName, typeface);
            textView.setTypeface(typeface);
        }
        values.recycle();
    }
}
