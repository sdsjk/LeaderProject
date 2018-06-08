package com.linewell.core.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.linewell.core.R;


/**
 * toolbar
 * @author zjianning
 */
public class ToolBarHelper {

    // 上下文，创建view的时候需要用到
    private Context mContext;

    /*base view*/
    private FrameLayout mContentView;

    // 用户定义的view
    private FrameLayout mUserView;

    // toolbar
    private Toolbar mToolBar;

    // 视图构造器
    private LayoutInflater mInflater;

    /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * */
    private static int[] ATTRS = {
            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };

    /**
     * 构造函数,使用默认的toolbar
     * @param context   上下文
     * @param layoutId  布局id
     */
    public ToolBarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        // 初始化整个内容
        initContentView();
        // 初始化用户定义的布局
        initUserView(layoutId);
        // 初始化toolbar
        initToolBar(R.layout.activity_tool_bar);
    }

    /**
     * 构造函数(使用自定义toolbar)
     * @param context       上下文
     * @param layoutId      布局id
     * @param toolbarResId  toolbar布局id
     */
    public ToolBarHelper(Context context, int layoutId, int toolbarResId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        // 初始化整个内容
        initContentView();
        // 初始化用户定义的布局
        initUserView(layoutId);
        // 初始化toolbar
        initToolBar(toolbarResId);
    }

    private void initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

    }

    private void initToolBar(int toolBarResId) {
        /*通过inflater获取toolbar的布局文件*/
        View toolbar = mInflater.inflate(toolBarResId, mContentView);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.toolbar);
    }

    private void initUserView(int id) {
        mUserView = new FrameLayout(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(0, false);
        /*获取主题中定义的toolbar的高度*/
        int toolBarSize = (int) typedArray.getDimension(1,
                (int) mContext.getResources().getDimension(R.dimen.activity_horizontal_margin));
        typedArray.recycle();

        /*如果是悬浮状态，则不需要设置间距*/
        params.topMargin = overly ? 0 : toolBarSize;


        View userChildView = mInflater.inflate(id, null);
        mUserView.addView(userChildView);
        mContentView.addView(mUserView, params);

    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    /**
     * 是否悬浮
     * @param overly    是否悬浮
     */
    public void setOverly(boolean overly){
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /*获取主题中定义的toolbar的高度*/
        int toolBarSize = (int) typedArray.getDimension(1,
                (int) mContext.getResources().getDimension(R.dimen.activity_horizontal_margin));
        typedArray.recycle();

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUserView.getLayoutParams();
        params.topMargin = overly ? 0 : toolBarSize;
        mUserView.setLayoutParams(params);
    }

    /**
     * 设置界面的顶部间隔
     * @param marginTop 间隔
     */
    public void setUserViewMarginTop(int marginTop){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUserView.getLayoutParams();
        params.topMargin = marginTop;
        mUserView.setLayoutParams(params);
    }

    public  FrameLayout getUserView(){
        return mUserView;
    }
}