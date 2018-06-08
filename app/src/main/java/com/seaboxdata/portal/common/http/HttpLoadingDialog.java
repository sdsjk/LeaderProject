package com.seaboxdata.portal.common.http;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.seaboxdata.portal.R;


/**
 * 新的加载对话框
 * Created by linyixin on 2017/11/29.
 */
public class HttpLoadingDialog extends Dialog {


    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 消息
     */
    private String mMsg;

    /**
     * 加载时的文字
     */
    private TextView textView;

    public HttpLoadingDialog(Context context) {
        super(context);
    }

    public HttpLoadingDialog(Context context, int themeResId) {
        super(context, R.style.curDialog);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param msg     为空时 = 正在加载
     */
    public HttpLoadingDialog(Context context, String msg) {
        this(context, 0);
        this.mContext = context;
        this.mMsg = msg;
        init();

    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.common_pop_loading, null);
        setContentView(view);

        //init view
        textView = (TextView) view.findViewById(com.linewell.core.R.id.loading_tv);

        //提示信息
        if (!TextUtils.isEmpty(mMsg)) {
            textView.setText(mMsg);
        }


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
        super.onDetachedFromWindow();
    }
}
