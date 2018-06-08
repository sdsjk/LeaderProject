package com.linewell.core.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.linewell.core.CommonApplicaton;
import com.linewell.core.R;
import com.linewell.core.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortraitActivity extends AppCompatActivity implements ILoadingView,IEmptyView,IErrorView{


    private List<Dialog> dialogList = new ArrayList<Dialog>();

    /**
     * 用户事件处理action
     */
    public static final String EVENT_ACTION = "com.innochina.common.EVENT_ACTION";

    /**
     * 是否能回退
     */
    private boolean canBack = true;

    /**
     * toolbar对象
     */
    private Toolbar mToolBar;

    private ToolBarHelper toolBarHelper;

    /**
     * 标题
     */
    private String mTitle = null;

    /**
     * toolbar开关
     */
    private boolean enableToolbar = true;

    /**
     * Logo图标开关
     */
    private boolean enableLogo = true;

    private boolean defaultToobar = true;

    /**
     * 标题的key
     */
    public static final String KEY_TITLE = "key_activity_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mTitle = extras.getString(KEY_TITLE);
        }

        //把界面加入application统一管理
        if(getApplication() instanceof CommonApplicaton){
            CommonApplicaton application = (CommonApplicaton) getApplication();
            application.addActivity(this);
        }

    }


    @Override
    protected void onDestroy() {
        //从application移除界面信息
        if(getApplication() instanceof CommonApplicaton){
            CommonApplicaton application = (CommonApplicaton) getApplication();
            application.removeActivity(this);
        }

        for(Dialog dialog:dialogList){
            if(dialog!=null&&dialog.isShowing()){
                dialog.cancel();
            }
            dialog = null;
        }
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        if(!enableToolbar){
            super.setContentView(layoutResID);
            return;
        }

        toolBarHelper = new ToolBarHelper(this,layoutResID) ;
        mToolBar = toolBarHelper.getToolBar() ;
        setContentView(toolBarHelper.getContentView());
        // 把 toolbar 设置到Activity 中

        initToolBar();
    }

    /**
     * 设置页面布局
     * @param layoutResID   页面布局id
     * @param toolbarResId  toolbar布局id
     */
    public void setContentView(int layoutResID, int toolbarResId) {
        if(!enableToolbar){
            super.setContentView(layoutResID);
            return;
        }

        defaultToobar =false;
        toolBarHelper = new ToolBarHelper(this, layoutResID, toolbarResId) ;
        mToolBar = toolBarHelper.getToolBar() ;
        setContentView(toolBarHelper.getContentView());
        // 把 toolbar 设置到Activity 中

        initToolBar();
    }

    /**
     * 初始化toolbar
     */
    private  void initToolBar(){
        if(mToolBar==null || !enableToolbar){
            return;
        }
        onCreateCustomToolBar(mToolBar);

        //这句一定要放在最后面,否则不生效

        setSupportActionBar(mToolBar);

        //设置监听一定要放到后面
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNavigationBack()){
                    finish();
                }
            }
        });
    }

    /**
     * 自定义的操作
     * @param toolbar
     */
    public void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.setContentInsetsRelative(0,0);
        if(defaultToobar){
            if(canBack){
                toolbar.setNavigationIcon(R.drawable.header_back_btn);
                toolbar.setLogo(null);
            }else{
                if(enableLogo){
                    toolbar.setLogo(R.drawable.ic_launcher);
                }else{
                    toolbar.setLogo(null);
                }
                toolbar.setNavigationIcon(null);
            }
            if(mTitle!=null){
                toolbar.setTitle(mTitle);
            }
        }else{
            toolbar.setTitle("");
        }
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(CharSequence title){
        mTitle = title.toString();
        initToolBar();
    }

    /**
     * 设置是否可以用导航返回
     * 为true时显示返回图标
     * 为false时,显示图标
     * (比如登录页,首页要设置false)
     * @param canBack
     */
    public void setCanBack(boolean canBack) {
        this.canBack = canBack;
        initToolBar();

    }

    /**
     * 设置是否启用toolbar
     * @param enableToolbar
     */
    public void setEnableToolbar(boolean enableToolbar) {
        this.enableToolbar = enableToolbar;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //发送到用户事件处理服务
        Intent intent = new Intent();
        intent.setAction(EVENT_ACTION);
        //5.0之后隐式调用要加
        intent.setPackage(this.getPackageName());
        try{
            startService(intent);
        }catch (Exception e){
            Log.e(this.getLocalClassName(),e.getMessage());

        }

        boolean flag = true;
        try{
            flag = super.dispatchTouchEvent(ev);
        }catch (IllegalArgumentException e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 获取toolbar
     * @return
     */
    public Toolbar getToolBar() {
        if (!enableToolbar) return null;
        return mToolBar;
    }

    private   TextView textOneView;

    /**
     * 设置居中标题
     * @param title 标题文字
     */
    public void setOneCenterTitle(String title){

        if(textOneView == null) {
            Toolbar toolbar = getToolBar();
            if (toolbar == null) {
                return;
            }
            //生成标题控件
            textOneView = createCenterTitle();
            textOneView.setText(title);
            //添加到toolbar
            toolbar.addView(textOneView);
        }else{
            textOneView.setText(title);
        }
    }


    /**
     * 设置居中标题
     * @param title 标题文字
     */
    public void setCenterTitle(String title){
        Toolbar toolbar = getToolBar();
        if(toolbar==null){
            return;
        }
        //生成标题控件
        TextView textView = createCenterTitle();
        textView.setText(title);
        //添加到toolbar
        toolbar.addView(textView);
    }

    /**
     * 设置居中标题
     * @param resId 标题文字资源id
     */
    public void setCenterTitle(int resId){
        Toolbar toolbar = getToolBar();
        if(toolbar==null){
            return;
        }
        //生成标题控件
        TextView textView = createCenterTitle();
        textView.setText(resId);

        //添加到toolbar
        toolbar.addView(textView);
    }

    private TextView createCenterTitle(){
        TextView textView = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.toolbar_title_center, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        //设置居中
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        return textView;
    }


    /**
     * 添加右侧按钮
     * @return
     */
    public Button addMenu(){
        Toolbar toolbar = getToolBar();
        if(toolbar==null){
            return null;
        }
        //生成控件
        Button menu = (Button) LayoutInflater.from(getApplicationContext()).inflate(R.layout.toolbar_menu, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        //设置居右
        layoutParams.gravity = Gravity.RIGHT;
        menu.setLayoutParams(layoutParams);
        //添加到toolbar
        toolbar.addView(menu);
        return menu;
    }

    /**
     * 左侧logo的开关
     * @param enableLogo
     */
    public void setEnableLogo(boolean enableLogo) {
        this.enableLogo = enableLogo;
        initToolBar();
    }

    /**
     * 导航栏返回回调(返回true,则自动finish页面)
     */
    public boolean onNavigationBack(){

        return true;
    }

    /**
     * 设置文字
     * @param resId         资源id
     * @param stringResId   stringid
     */
    public void setText(int resId, int stringResId){
        if(mToolBar==null){
            return;
        }
        TextView textView = (TextView) mToolBar.findViewById(resId);
        if(textView==null){
            return;
        }
        textView.setText(stringResId);
    }
    /**
     * 设置文字
     * @param resId         资源id
     * @param stringRes   string
     */
    public void setText(int resId, String stringRes){
        if(mToolBar==null){
            return;
        }
        TextView textView = (TextView) mToolBar.findViewById(resId);
        if(textView==null){
            return;
        }
        textView.setText(stringRes);
    }

    /**
     * 是否悬浮
     * @param overly    是否悬浮
     */
    public void setOverly(boolean overly){
        if(toolBarHelper!=null){
            toolBarHelper.setOverly(overly);
        }
    }

    /**
     * 设置界面的顶部间隔
     * @param marginTop 间隔
     */
    public void setUserViewMarginTop(int marginTop){
        if(toolBarHelper!=null){
            toolBarHelper.setUserViewMarginTop(marginTop);
        }
    }

    public void addDialog(Dialog dialog){
        dialogList.add(dialog);
    }


    private FrameLayout getStatusRootView(){
        if(enableToolbar){
            return toolBarHelper.getUserView();
        }else{
            return ((FrameLayout)getWindow().findViewById(Window.ID_ANDROID_CONTENT));
        }
    }

    private View loadingView;

    @Override
    public View initLoadingView(){
        return null;
    }

    @Override
    public void showLoadingView(){
        if(loadingView==null){
            loadingView = initLoadingView();

            if(loadingView==null){
                return;
            }

            //添加到布局
            getStatusRootView().addView(loadingView);
        }else{
            loadingView.setVisibility(View.VISIBLE);
        }

        hideEmptyView();
        hideErrorView();
    }


    @Override
    public void hideLoadingView(){
        if(loadingView!=null){
            loadingView.setVisibility(View.GONE);
        }
    }


    private View emptyView;
    @Override
    public View initEmptyView() {
        return null;
    }

    @Override
    public void showEmptyView() {
        if(emptyView==null){
            emptyView = initEmptyView();

            if(emptyView==null){
                return;
            }

            //添加到布局
            getStatusRootView().addView(emptyView);
        }else{
            emptyView.setVisibility(View.VISIBLE);
        }

        hideLoadingView();
        hideErrorView();
    }

    @Override
    public void hideEmptyView() {
        if(emptyView!=null){
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnEmptyClickListener(View.OnClickListener onClickListener) {
        if(emptyView!=null){
            emptyView.setOnClickListener(onClickListener);
        }
    }

    private View errorView;

    @Override
    public View initErrorView() {
        return null;
    }

    @Override
    public void showErrorView() {
        if(errorView==null){
            errorView = initErrorView();

            if(errorView==null){
                return;
            }

            //添加到布局
            getStatusRootView().addView(errorView);
        }else{
            errorView.setVisibility(View.VISIBLE);
        }

        hideLoadingView();
        hideEmptyView();
    }

    @Override
    public void hideErrorView() {
        if(errorView!=null){
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnErrorClickListener(View.OnClickListener onClickListener) {
        if(errorView!=null){
            errorView.setOnClickListener(onClickListener);
        }
    }

    private Map<String, PermissionUtils.OnPermissionResultListener> permissionResultListenerMap = new HashMap<>();

    public void setPerssionResultListener(String perssion, PermissionUtils.OnPermissionResultListener onPermissionResultListener){
        permissionResultListenerMap.put(perssion, onPermissionResultListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PermissionUtils.PERMISSION_CODE:
                if(permissions==null||permissions.length==0){
                    return;
                }
                PermissionUtils.OnPermissionResultListener listener = permissionResultListenerMap.get(permissions[0]);
                if(listener!=null){
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //获取成功
                        listener.onSuccess(requestCode, permissions);
                    }else{
                        listener.onCancel(requestCode, permissions);
                    }
                }
                break;
        }
    }


}
