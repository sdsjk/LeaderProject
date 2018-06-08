package com.linewell.core.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author lyixin
 * @since 2017/6/13.
 */

public abstract class BaseFragment extends Fragment  implements ILoadingView,IEmptyView,IErrorView{

    private FrameLayout view;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = new FrameLayout(inflater.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.addView(onCreateCustomView(inflater, container, savedInstanceState), 0);
        return view;
    }

    public abstract View onCreateCustomView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    private FrameLayout getStatusRootView(){
        return view;
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
            if(getStatusRootView()!=null){
                getStatusRootView().addView(loadingView);
            }
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
            if(getStatusRootView()!=null){
                getStatusRootView().addView(emptyView);
            }
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
            if(getStatusRootView()!=null){
                getStatusRootView().addView(errorView);
            }
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
}
