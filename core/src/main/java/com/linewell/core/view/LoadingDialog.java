package com.linewell.core.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.linewell.core.R;



/**
 * 加载框
 *
 * @author hshande@linewell.com
 * @since 2017/2/27
 */
public class LoadingDialog extends Dialog {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 消息
     */
    private String mMsg;

    /**
     * 加载圈
     */
    private ImageView imageView;

    /**
     * 加载时的文字
     */
    private TextView textView;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, R.style.curDialog);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param msg     为空时 = 正在加载
     */
    public LoadingDialog(Context context, String msg) {
        this(context, 0);
        this.mContext = context;
        this.mMsg = msg;
        init();

    }

    private void init() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.pop_loading, null);
        setContentView(view);

        //init view
        imageView = (ImageView) view.findViewById(R.id.loading_iv);
        textView = (TextView) view.findViewById(R.id.loading_tv);

        //提示信息
        if (!TextUtils.isEmpty(mMsg)) {
            textView.setText(mMsg);
        }

        //加载圈动画
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotation);
        imageView.setAnimation(animation);

        //关闭时关闭动画
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (imageView != null) {
                    imageView.clearAnimation();
                }
            }
        });
        //关闭时关闭动画
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (imageView != null) {
                    imageView.clearAnimation();
                }
            }
        });

        //不可关闭
        setCancelable(false);

        setCanceledOnTouchOutside(false);

        // 设置相关位置，一定要在 show()之后
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = RecyclerView.LayoutParams.MATCH_PARENT;
        params.height = RecyclerView.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    public void onDetachedFromWindow() {
        if (imageView != null) {
            imageView.clearAnimation();
        }
        super.onDetachedFromWindow();
    }
}
