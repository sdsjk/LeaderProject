package com.seaboxdata.portal.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.linewell.core.activity.BaseFragment;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import butterknife.Unbinder;

/**
 * 公共的fragment
 * @author lyixin
 * @since 2017/6/12.
 */

public abstract class CommonFragment extends BaseFragment {

    protected Activity mActivity;

    public static final String KEY_DATA = "KEY_DATA";

    public Context mContext;

    protected Unbinder unbinder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        unbinder = ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
        mContext = activity.getApplicationContext();
    }

    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }

    public void setContext(Context context){
        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }

    }

    @Override
    public View initLoadingView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View loadingView = inflater.inflate(R.layout.loading, null, false);
//        GifImageView gifImageView = (GifImageView) loadingView.findViewById(R.id.image_loading);
//        try {
//            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
//            // 将gif图资源转化为GifDrawable
//            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.img_loading);
//
//            // gif1加载一个动态图gif
//            gifImageView.setImageDrawable(gifDrawable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return loadingView;
        return null;
    }

    public int getStatusBarHeight(Activity activity) {
        return new SystemBarTintManager(activity).getConfig().getStatusBarHeight();
    }

}
