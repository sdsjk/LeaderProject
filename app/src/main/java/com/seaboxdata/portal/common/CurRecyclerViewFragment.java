package com.seaboxdata.portal.common;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linewell.pulllistview.RecyclerViewFragment;
import com.linewell.pulllistview.RecyclerViewFragmentAdapter;
import com.linewell.pulllistview.WrapContentLinearLayoutManager;
import com.seaboxdata.portal.R;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 列表
 * Created by zjianning on 2017/9/20.
 */

public abstract class CurRecyclerViewFragment extends RecyclerViewFragment {

    protected static final String KEY_LOADED = "KEY_LOADED";

    public static final String SHOW_GO_TOP_VIEW_INDEX = "SHOW_GO_TOP_VIEW_INDEX";

    public static final String SHOW_GO_TOP_VIEW_DIRE = "SHOW_GO_OP_VIEW_DIRE";

    protected int mGoTopViewIndex = -1;

//    protected View mGoTopView;

    protected boolean isFindFirst = true;

    private Handler mHandler = new Handler();

    @Override
    public void setHeadView(PtrClassicFrameLayout frameLayout) {

//        CurPtrHouseHeader curPtrHouseHeader = new CurPtrHouseHeader(mContext);
//        frameLayout.setHeaderView(curPtrHouseHeader);
//        frameLayout.addPtrUIHandler(curPtrHouseHeader);
//        frameLayout.setResistance(1.7f);
//        frameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
//        frameLayout.setDurationToClose(200);
//        frameLayout.setDurationToCloseHeader(200);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setAutoLoadMoreEnable(true);


        View view = super.onCreateView(inflater, container, savedInstanceState);

//        mGoTopView = mRootView.findViewById(R.id.font_go_top);

        mGoTopViewIndex = getArguments().getInt(SHOW_GO_TOP_VIEW_INDEX, -1);

        isFindFirst =  getArguments().getBoolean(SHOW_GO_TOP_VIEW_DIRE, true);

        final WrapContentLinearLayoutManager wrapContentLinearLayoutManager = (WrapContentLinearLayoutManager) mRecyclerView.getLayoutManager();

        if(mGoTopViewIndex == -1) {
//            mGoTopView.setVisibility(View.GONE);
        } else {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            int postion;
                            if (isFindFirst) {
                                postion = wrapContentLinearLayoutManager.findFirstVisibleItemPosition();
                            } else {
                                postion = wrapContentLinearLayoutManager.findLastVisibleItemPosition();
                            }

                            if (postion < mGoTopViewIndex) {
//                                mGoTopView.setVisibility(View.GONE);
                            } else {
//                                mGoTopView.setVisibility(View.VISIBLE);
                            }
                        }
                    });



                }
            });


            // 点击会到顶部
//            mGoTopView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mGoTopView.setVisibility(View.GONE);
//
////                    wrapContentLinearLayoutManager.scrollToPositionWithOffset(0, 0);
//
//                    mRecyclerView.smoothScrollToPosition(0);
//                }
//            });
        }

        return view;

    }

    @Override
    public View getEmptyView(int emptyLayoutId) {
        return super.getEmptyView(R.layout.recycler_view_blank_ll);
    }

    @Override
    public int getEmptyImageResource() {
        return R.drawable.img_blank_common;
    }

    @Override
    public int getErrorImageResource() {
        return R.drawable.img_blank_network_error;
    }

    public void setPadding(int left, int top, int right, int bottom){
        if(mRecyclerView!=null){
            mRecyclerView.setPadding(left, top, right, bottom);
        }
    }

    @Override
    public void reload() {
        if(mRecyclerView!=null&&mRecyclerView.getAdapter()!=null){
            ((RecyclerViewFragmentAdapter)(mRecyclerView.getAdapter())).removeAllFooterView();
        }
        super.reload();
    }

    @Override
    public void onShowFail() {
        super.onShowFail();
        mFrame.setVisibility(View.GONE);
    }

    @Override
    public boolean showNoDataViewOneScreen() {
        return super.showNoDataViewOneScreen();
    }

}
