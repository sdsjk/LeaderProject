package com.linewell.pulllistview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linewell.core.exception.BugReporter;
import com.linewell.http.AppHttpResultHandler;
import com.linewell.http.AppHttpUtils;
import com.linewell.http.BaseParamsInits;
import com.linewell.utils.GsonUtil;
import com.linewell.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 列表的组件
 *
 * @author zjianning
 * @since 2016-07-21
 */
public abstract class RecyclerViewFragment extends BasePagerFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener, RecyclerViewListener {

    private Handler mHandler = new Handler();

    // 参数
    public static final String PARAMS = "params";

    // Context
    protected Context mContext;

    protected Activity mActivity;

    // 为空的提示对象
    protected RelativeLayout mBlankLL;
    // 为空的提示图片
    private ImageView mBlankIV;
    //为空的提示tv
    private TextView mBlankTipTV;

    // 出错的界面
    protected LinearLayout mErrorLL;

    //反馈问题按钮
    private Button mFeedBT;

    /**
     * 带图标的反馈按钮
     */
    private LinearLayout mFeedLL;

    //
    public RecyclerView mRecyclerView;

    //
    protected RecyclerViewFragmentAdapter mListAdapter;

    //
    protected PtrClassicFrameLayout mFrame;

    // 默认的分页的大小
    public static int PAGE_SIZE = 30;

    // 下拉的参数
    public static final String DOWN_TYPE = "DOWN";
    // 上拉的参数
    public static final String UP_TYPE = "UP";

    // 刷新数据的时间
    private Long mRefreshDate = null;
    //刷新排序字段值
    private Object mRefreshSortFieldValue = null;

    private Object sortFieldValue = null;

    // 加载更多数据的时间
    private Long mLoadMoreDate = null;

    // 列表延迟的时间
    private int delayMillis = 100;

    // 服务端交互的地址
    protected String mServiceUrl;

    // 是否加载过 主要是第一次加载和刷新用
    public boolean isLoaded = false;

    // 页面的参数
    public ListParams mParams;

    // 获取数据的时间参数变量
    private static final String GET_DATA_LIST_STR = "lastdate";

    //是否允许上拉刷新
    private boolean forbidUp = true;

    //下拉是否整个刷掉
    private boolean isRefreshReload = true;
    //是否采用分页刷新
    private boolean isPageRefresh = true;
    // 判断在显示的个数小于
    private boolean isCountSize;

    // 加载更多的Handler
    private Handler onLoadMoreRequestedHandler = new Handler();


    protected View mRootView;

    //是否在加载更多
    protected boolean isLoadingMore = false;
    //是否有更多数据
    protected boolean hasMoreData = true;

    //是否开启自动预加载
    protected boolean isAutoLoadMoreEnable = false;
    // 背景覆盖区域
    protected LinearLayout mBackGroudView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        mActivity = getActivity();
        mParams = (ListParams) getArguments().getSerializable(PARAMS);
        mServiceUrl = mParams.getServiceUrl();

        int pageSize = mParams.getPageSize();
        if (pageSize > 0) {
            PAGE_SIZE = pageSize;
        }

        mListAdapter = new RecyclerViewFragmentAdapter(mParams, new ArrayList<JsonObject>(), this);

//        mListAdapter.openLoadAnimation();

        mListAdapter.setOnLoadMoreListener(RecyclerViewFragment.this);

        mListAdapter.openLoadMore(PAGE_SIZE, true);
        mListAdapter.setHasStableIds(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mRootView != null) {
            return  mRootView;
        }
        mRootView = inflater.inflate(R.layout.activity_recycler_view_fragment, container, false);

        mBackGroudView = (LinearLayout) mRootView.findViewById(R.id.rv_backgroud_ll);

        // 列表的对象
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext));

        mRecyclerView.setHasFixedSize(true);//最重要的这句

        // 空提示
        mBlankLL = (RelativeLayout) mRootView.findViewById(R.id.list_blank_ll);
        mBlankLL.setOnClickListener(RecyclerViewFragment.this);

        View emptyView = getEmptyView(R.layout.recycler_view_blank_tip);
        mBlankLL.addView(emptyView);

        //空提示的图片提示
        mBlankIV = (ImageView) emptyView.findViewById(R.id.blank_iv);

        // 空的界面
        int emptyIV = getEmptyImageResource();
        if (emptyIV > 0) {
            mBlankIV.setImageResource(emptyIV);
        }

        String tipType = getArguments().getString("SEARCH_SHOW_TYPE");
        //空提示的文字
        mBlankTipTV = (TextView) emptyView.findViewById(R.id.blank_tip_tv);
        String emptyTipStr = getEmptyTip();
        if (!TextUtils.isEmpty(emptyTipStr)) {
            //空页面文字设置
            mBlankTipTV.setText(emptyTipStr);
        } else {
//            View tipLL = emptyView.findViewById(R.id.blank_tip_ll);
//            if(tipLL!=null){
//                tipLL.setVisibility(View.GONE);
//            }
        }

        //更改空tip的图片
        changeBlankTipIV(tipType);

        // 出错的提示
        mErrorLL = (LinearLayout) mRootView.findViewById(R.id.list_error_tv);
        mErrorLL.setOnClickListener(RecyclerViewFragment.this);

        View errorView = getErrorView(R.layout.recycler_view_error_tip);
        mErrorLL.addView(errorView);

        setErrorView(errorView);

        ImageView errorIV = (ImageView) errorView.findViewById(R.id.recycleview_view_error_tip_img);
        // 出错的图标
        int errorImg = getErrorImageResource();
        if (errorImg > 0) {
            errorIV.setImageResource(errorImg);
        }

        TextView errorTV = (TextView) errorView.findViewById(R.id.recycleview_view_error_tip_text);
        String errorTipStr = getErrorTip();
        if (!TextUtils.isEmpty(errorTipStr)) {
            //空页面文字设置
            errorTV.setText(errorTipStr);
        }

        //反馈按钮
        mFeedBT = (Button) emptyView.findViewById(R.id.feed_question_bt);

        //带图标的反馈按钮
        mFeedLL = (LinearLayout) emptyView.findViewById(R.id.feed_question_ll);

        initHeadRefresh(mRootView);

        View loadingView = getActivity().getLayoutInflater().inflate(R.layout.list_loading,
                (ViewGroup) mRecyclerView.getParent(), false);

        mListAdapter.setLoadingView(loadingView);

        loadingView.findViewById(R.id.list_load_more_iv).startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.rotation_loading));

        return mRootView;
    }

    protected void setErrorView(View errorView) {
    }

    protected String getErrorTip() {
        return "";
    }

    /**
     * 获取网络出错界面的图片
     *
     * @return
     */
    protected int getErrorImageResource() {
        return R.drawable.img_tip_fail;
    }

    /**
     * 获取网络出错页面
     * @param errorLayoutId
     * @return
     */
    protected View getErrorView(int errorLayoutId) {
        return LayoutInflater.from(mContext).inflate(errorLayoutId, mErrorLL, false);
    }

    /**
     * 获取空界面
     *
     * @param emptyLayoutId
     * @return
     */
    public View getEmptyView(int emptyLayoutId) {

        return LayoutInflater.from(mContext).inflate(emptyLayoutId, mBlankLL, false);
    }

    /**
     * 获取空界面的图片
     *
     * @return
     */
    public int getEmptyImageResource() {
        return 0;
    }

    /**
     * 列表自定义处理
     *
     * @param baseViewHolder
     * @param obj
     */
    @Override
    public void customRenderItem(BaseViewHolder baseViewHolder, JsonObject obj) {
    }

    @Override
    public void setNewData(List<JsonObject> jsonObjectList) {



            if (jsonObjectList.size() > 0) {
                mListAdapter.setNewData(jsonObjectList);
                mListAdapter.removeAllFooterView();
//                mListAdapter.notifyDataChangedAfterLoadMore(jsonObjectList,false);
            }


    }

    @Override
    public void customRenderItemAfter(BaseViewHolder baseViewHolder, JsonObject obj) {
        int postion = baseViewHolder.getAdapterPosition();

        if(!isAutoLoadMoreEnable){
            return;
        }

        //没有更到数据
        if(!hasMoreData){
            return;
        }

        if(postion>Math.abs(getItemCount()-20) && mLoadMoreDate != null){
            onLoadMoreRequested();
        }
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (mRecyclerView != null && mRecyclerView.getChildCount() == 0) {
                mRecyclerView.smoothScrollBy(0, 1);
            }
        }
    }

    /**
     * 下拉刷新的控件初始化
     *
     * @param view
     */
    private void initHeadRefresh(View view) {
        mFrame = (PtrClassicFrameLayout) view.findViewById(R.id.store_house_ptr_frame);
        mFrame.setLastUpdateTimeRelateObject(this);

        // 隐藏空提示
        mBlankLL.setVisibility(View.GONE);

        mFrame.setVisibility(View.VISIBLE);

        mRecyclerView.setAdapter(mListAdapter);

        final boolean forbidDown = mParams.isForbidDown();

//        isPageRefresh=mParams.isPageRefresh();

//        isRefreshReload=mParams.isRefreshReload();

//
//        mFrame.setEnabledNextPtrAtOnce(true);
        mFrame.setPullToRefresh(false);

        mFrame.setDurationToClose(2000);
        mFrame.setDurationToCloseHeader(200);

        setHeadView(mFrame);

        mFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                if (forbidDown) {
                    return false;
                }


                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBlankLL.setVisibility(View.GONE);
                        mErrorLL.setVisibility(View.GONE);

                        if (isLoaded) {
                            if (isRefreshReload) {
                                refreshStartCallBack();
                                pageRefresh();
                                refreshEndCallBack();
                                return;
                            }
                        }

                        getAndHandleListData(DOWN_TYPE, mRefreshDate, mRefreshSortFieldValue, mListCallBack);

                    }// end run
                }, 100);// end postDelayed
            }// end onRefreshBegin


        });

        if (mParams.isLoadLocal()) {
            onLoadNewRequested();
        } else {
            autoRefresh();
        }

    }

    /**
     * 列表加载完的监听事件
     */
    public ListCallBack mListCallBack = new ListCallBack<JsonObject>() {
        @Override
        public void successCallBack(final List<JsonObject> dataList) {

            try {
                RecyclerViewFragment.this.successCallBack(dataList);
            } catch (Exception e) {
                BugReporter.getInstance().postException(e);
                Log.e("", e.getMessage());
            }

        }// end successCallBack

        @Override
        public void emptyCallBack() {
            try {
                RecyclerViewFragment.this.emptyCallBack();
            } catch (Exception e) {
                BugReporter.getInstance().postException(e);
                Log.e("", e.getMessage());
            }
        }// end emptyCallBack
    };

    /**
     * 加载为空的回调
     */
    public void emptyCallBack() {
        mFrame.refreshComplete();
        // 如果是第一次加载的 要初始化
        if (!isLoaded) {
            mRefreshDate = null;
            mLoadMoreDate = null;
            // 设置空界面
            initAdapter(new ArrayList<JsonObject>(), mParams);
            isLoaded = false;

            if (mBlankLL.getVisibility() != View.VISIBLE) {

                if(mListAdapter.getFooterLayoutCount() > 0) {
                    mListAdapter.removeAllFooterView();
                }

                mFrame.refreshComplete();

                mFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBlankLL.setVisibility(View.VISIBLE);
                    }
                }, 600);

//                mFrame.setVisibility(View.GONE);
            }

        } else {
            // 数据最新
            // TODO
//            ToastUtils.show(getActivity(), "数据最新", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 加载有数据的回调
     *
     * @param dataList
     */
    public void successCallBack(final List<JsonObject> dataList) {
//        mFrame.setEnabledNextPtrAtOnce(false);
        //初始化
        isCountSize = false;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mFrame.refreshComplete();
            }
        });

        // 如果是第一次加载的 要初始化
        if (!isLoaded) {
            initAdapter(dataList, mParams);
        } else {
            if (dataList != null && dataList.size() > 0) {

                if (dataList.get(0).get(GET_DATA_LIST_STR) == null) {
                    mRefreshDate = null;
                } else {
                    mRefreshDate = Long.parseLong(dataList.get(0).get(GET_DATA_LIST_STR).getAsString());
                }

                isLoaded = true;

                // 如果是重新加载数据
                if (isRefreshReload) {

                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListAdapter.setNewData(dataList);
                        }
                    });

                } else {
                    mListAdapter.addData(dataList);
                }

//                                        mListAdapter.openLoadMore(PAGE_SIZE, true);
            }
        }

        setRefreshSortFieldValue(dataList.get(0));
    }

    /**
     * 下拉
     */
    public void onLoadNewRequested() {

        getAndHandleListData(DOWN_TYPE, mRefreshDate, mRefreshSortFieldValue, mListCallBack);// end getAndHandleListData
    }

    /**
     * 下拉加载更多
     */
    public void onLoadNewRequested(final ListCallBack<JsonObject> listCallBack) {
        //重写回调
        ListCallBack mListCallBack = new ListCallBack<JsonObject>() {
            @Override
            public void successCallBack(final List<JsonObject> dataList) {

                RecyclerViewFragment.this.successCallBack(dataList);
                listCallBack.successCallBack(dataList);

            }// end successCallBack

            @Override
            public void emptyCallBack() {
                RecyclerViewFragment.this.emptyCallBack();
                listCallBack.emptyCallBack();
            }// end emptyCallBack
        };
        getAndHandleListData(DOWN_TYPE, mRefreshDate, mRefreshSortFieldValue, mListCallBack);// end getAndHandleListData
    }

    /**
     * 初始化适配器
     *
     * @param listData 初始的数据列表
     * @param params   列表的参数
     */
    private void initAdapter(final List<JsonObject> listData, ListParams params) {

        if(mListAdapter.getFooterLayoutCount() > 0) {
            mListAdapter.removeAllFooterView();
        }

        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 设置数据为空
                mListAdapter.setNewData(new ArrayList<JsonObject>());
                mListAdapter.setNewData(listData);

//                mListAdapter.setData(listData);
            }
        });

        // 监听列表的单击事件
        mListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onRecyclerViewItemClick(view, listData.get(position), position);
            }
        });

        // 监听列表的长按事件
        mListAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                onRecyclerViewLongItemClick(view, listData.get(position), position);
                return true;
            }
        });

        if (null != mFeedBT) {
            //空页面显隐
            if (isEmptyBTVisibility()) {
                mFeedBT.setVisibility(View.VISIBLE);
            } else {
                mFeedBT.setVisibility(View.GONE);
            }
            //空界面按钮的文字描述
            mFeedBT.setText(getButtonName());
            //空页面按钮的点击事件
            mFeedBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doFeed();
                }
            });
        }

        if (null != mFeedLL) {
            //空白界面带图标的按钮
            if (isEmptyLLVisibility()) {
                mFeedLL.setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) mFeedLL.findViewById(R.id.img);
                TextView textView = (TextView) mFeedLL.findViewById(R.id.text);
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, getButtonImage()));
                textView.setText(getButtonName());
                mFeedLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doFeed();
                    }
                });
            } else {
                mFeedLL.setVisibility(View.GONE);
            }
        }


        mBlankLL.setVisibility(View.GONE);
        mErrorLL.setVisibility(View.GONE);
        mFrame.setVisibility(View.VISIBLE);

        int size = listData.size();

        if (size > 0) {

            if (listData.get(0).get(GET_DATA_LIST_STR) == null) {
                mRefreshDate = null;
                mLoadMoreDate = null;
            } else {
                // 刷新的时间
                mRefreshDate = Long.parseLong(listData.get(0).get(GET_DATA_LIST_STR).getAsString());

                // 加载更多的时间
                mLoadMoreDate = Long.parseLong(listData.get(size - 1).get(GET_DATA_LIST_STR).getAsString());

                setSortFieldValue(listData.get(size - 1));
            }

            int listDataSize = listData.size();

            // 如果初始的数据小于分页数据，表明已经到最后一页，显示loading
            if (listDataSize < PAGE_SIZE) {
                hasMoreData = false;
//                if(isLoaded){
                    showNotLoading();
//                }
            }


            if (listDataSize < PAGE_SIZE) {
               // mRecyclerView.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);
            } else {
              //  mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);
            }

        }// end if

        isLoaded = true;

    }

    /**
     * 监听列表渲染过程
     */
    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            // 列表高度
            int recycleViewHeiht = mRecyclerView.getLayoutManager().getHeight();
            // item高度
            View childView = mRecyclerView.getLayoutManager().getChildAt(0);
            if (childView == null) {
                return true;
            }
            int itemHeight = childView.getHeight();
            if ((showNoDataViewOneScreen()||mRecyclerView.getLayoutManager().getChildCount() * itemHeight > recycleViewHeiht) && !isCountSize) {

                isCountSize = true;

                showNotLoading();
            }
//            else {
//
//                if (mListAdapter.getFooterLayoutCount() > 0 && hasMoreData) {
//                    mListAdapter.removeAllFooterView();
//                }
//
//            }
            return true;
        }
    };


    /**
     * 列表的点击事件
     *
     * @param view     列表的对象
     * @param t        JsonObject对象
     * @param position dan
     */
    public abstract void onRecyclerViewItemClick(final View view, final JsonObject t, int position);

    /**
     * 列表的长按事件
     *
     * @param view
     * @param t
     * @param position
     */
    public void onRecyclerViewLongItemClick(View view, JsonObject t, int position) {

    }

    /**
     * 显示无更多数据
     */
    private void showNotLoading() {
//        mListAdapter.notifyDataChangedAfterLoadMore(false);
        if (null != getActivity()) {
            View view = onCreateNotLoading();
            mListAdapter.removeAllFooterView();
            mListAdapter.addFooterView(view);
        }
        hasMoreData = false;
    }

    public View onCreateNotLoading(){
        View view = getActivity().getLayoutInflater().inflate(R.layout.list_not_more_data,
                (ViewGroup) mRecyclerView.getParent(), false);
        return view;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        // 数据为空
        if (i == R.id.list_blank_ll) {
            mFrame.setVisibility(View.VISIBLE);
//            mFrame.setEnabledNextPtrAtOnce(true);

            mFrame.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBlankLL.setVisibility(View.GONE);
                    reload();
                    blandCallBack();
                }
            }, 200);

            // 网络出错
        } else if (i == R.id.list_error_tv) {

            mFrame.setVisibility(View.VISIBLE);
//            mFrame.setEnabledNextPtrAtOnce(true);

            mListAdapter.getData().clear();
            mListAdapter.notifyDataSetChanged();

            mFrame.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mErrorLL.setVisibility(View.GONE);
                    reload();
                    errorCallBack();
                }
            }, 200);
        }
    }

    /**
     * 点击为空的回调
     */
    public void blandCallBack() {

    }

    /**
     * 点击网络出错的回调
     */
    public void errorCallBack() {

    }

    //上拉加载
    @Override
    public void onLoadMoreRequested() {
        if(isLoadingMore){
            return;
        }

        // 加载更多
        // 获取远程数据

        if (mParams != null && mParams.isForbidUp()) {
            //如果禁用上拉则不执行
            return;
        }
        isLoadingMore = true;
        getAndHandleListData(UP_TYPE, mLoadMoreDate, sortFieldValue, new ListCallBack<JsonObject>() {
            @Override
            public void successCallBack(final List<JsonObject> dataList) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        if (null == onLoadMoreRequestedHandler) {
                            onLoadMoreRequestedHandler = new Handler();
                        }

                        onLoadMoreRequestedHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 添加到尾部
                                mListAdapter.notifyDataChangedAfterLoadMore(dataList, true);

                                int size = dataList.size();

                                JsonObject subJsonObject = dataList.get(size - 1);

                                try {
                                    // 加载更多的时间
                                    mLoadMoreDate = Long.parseLong(subJsonObject.get(GET_DATA_LIST_STR).getAsString());
                                }catch (Exception e) {

                                }

                                setSortFieldValue(subJsonObject);


                                if (size < PAGE_SIZE) {
                                    showNotLoading();
                                }else{
                                    hasMoreData = true;
                                }

                                isLoadingMore = false;

                            }
                        }, delayMillis);
                    }
                });
            }

            @Override
            public void emptyCallBack() {
                // 设置尾部 没有更多数据的样式
                showNotLoading();
            }
        });
    }

    /**
     * 下拉刷新当前页面
     */
    private void pageRefresh() {
        // 初始化时间戳为0
        mRefreshDate = null;

        // 设置未加载过数据
        isLoaded = false;

        mRefreshSortFieldValue = null;

        mListAdapter.notifyDataChangedAfterLoadMore(true);

        hasMoreData = true;

        getAndHandleListData(DOWN_TYPE, null, mRefreshSortFieldValue, mListCallBack);
    }

    /**
     * 返回回馈按钮
     *
     * @return
     */
    public Button getFeedBt() {
        return this.mFeedBT;
    }

    /**
     * 按钮显隐
     */
    public boolean isEmptyBTVisibility() {
        return false;
    }

    /**
     * 按钮显隐
     */
    public boolean isEmptyLLVisibility() {
        return false;
    }

    /**
     * 按钮文字
     */
    public String getButtonName() {
        return "";
    }


    /**
     * 按钮图标
     */
    public int getButtonImage() {
        return R.drawable.img_pub_require;
    }

    /**
     * 空页面按钮点击事件
     */
    public void doFeed() {
        Log.e("TAG", "empty");
    }

    /**
     * 空页面提示语言
     */
    public String getEmptyTip() {
        return "";
    }


    /**
     * 设置sortFieldValue
     *
     * @param subJsonObject
     */
    private void setSortFieldValue(JsonObject subJsonObject) {
        if (!TextUtils.isEmpty(mParams.getSortFieldName())) {
            if (subJsonObject.has(mParams.getSortFieldName()) && !subJsonObject.get(mParams.getSortFieldName()).isJsonNull()) {
                if (mParams.getSortFieldType() == ListParams.FIELD_BOOLEAN_VALUE) {
                    sortFieldValue = subJsonObject.get(mParams.getSortFieldName()).getAsBoolean();
                } else if (mParams.getSortFieldType() == ListParams.FIELD_INTEGER_VALUE) {
                    sortFieldValue = subJsonObject.get(mParams.getSortFieldName()).getAsInt();
                } else if (mParams.getSortFieldType() == ListParams.FIELD_LONG_VALUE) {
                    sortFieldValue = subJsonObject.get(mParams.getSortFieldName()).getAsLong();
                } else if (mParams.getSortFieldType() == ListParams.FIELD_STRING_VALUE) {
                    sortFieldValue = subJsonObject.get(mParams.getSortFieldName()).getAsString();
                }
            }
        }
    }

    /**
     * 设置refreshSortFieldValue
     *
     * @param subJsonObject
     */
    private void setRefreshSortFieldValue(JsonObject subJsonObject) {
        if (!TextUtils.isEmpty(mParams.getSortFieldName())) {
            if (subJsonObject.has(mParams.getSortFieldName()) && !subJsonObject.get(mParams.getSortFieldName()).isJsonNull()) {
                if (mParams.getSortFieldType() == ListParams.FIELD_BOOLEAN_VALUE) {
                    mRefreshSortFieldValue = subJsonObject.get(mParams.getSortFieldName()).getAsBoolean();
                } else if (mParams.getSortFieldType() == ListParams.FIELD_INTEGER_VALUE) {
                    mRefreshSortFieldValue = subJsonObject.get(mParams.getSortFieldName()).getAsInt();
                } else if (mParams.getSortFieldType() == ListParams.FIELD_LONG_VALUE) {
                    mRefreshSortFieldValue = subJsonObject.get(mParams.getSortFieldName()).getAsLong();
                } else if (mParams.getSortFieldType() == ListParams.FIELD_STRING_VALUE) {
                    mRefreshSortFieldValue = subJsonObject.get(mParams.getSortFieldName()).getAsString();
                }
            }
        }
    }

    /**
     * 获取数据
     *
     * @param type         类型
     * @param date         时间
     * @param listCallBack 列表的回调
     */
    protected void getAndHandleListData(final String type,
                                        final Long date, final Object sortFieldValue,
                                        final ListCallBack listCallBack) {

        // 默认的访问的参数

        // 进行数据交互
        AppHttpUtils.postJson(getContext(), mServiceUrl, getParams(type, date, sortFieldValue), new AppHttpResultHandler<Object>() {
            @Override
            public void onSuccess(Object result, JsonObject allResult) {


                JsonArray jsonArray = new JsonArray();
                if (result == null) {
                    listCallBack.emptyCallBack();
                    return;
                }

                if (result instanceof JsonObject) {
                    JsonObject jsonObject = (JsonObject) result;
                    JsonElement listData = jsonObject.get("list");
                    if (null == listData || listData.toString().equals("null")) {
                        listCallBack.emptyCallBack();
                        return;
                    }

                    jsonArray = listData.getAsJsonArray();
                } else if (result instanceof JsonArray) {
                    jsonArray = (JsonArray) result;
                }


                Iterator it = jsonArray.iterator();
                List<JsonObject> jsonObjectList = new ArrayList<JsonObject>();
                while (it.hasNext()) {
                    JsonElement e = (JsonElement) it.next();
                    jsonObjectList.add(e.getAsJsonObject());
                }

                // 非空判断
                if (jsonObjectList.size() > 0) {
                    listCallBack.successCallBack(jsonObjectList);
                } else {
                    listCallBack.emptyCallBack();
                }

            }

            @Override
            public boolean onSysFail(JsonObject resultJson) {
                mFrame.refreshComplete();

                mErrorLL.setVisibility(View.VISIBLE);
//                mFrame.setVisibility(View.GONE);

                isLoadingMore = false;

                onShowFail();

//                ToastUtils.show(getActivity(), resultJson.get("message").getAsString(), Toast.LENGTH_SHORT);
                return false;
            }

            @Override
            public boolean onFail(JsonObject resultJson) {
                mFrame.refreshComplete();

                if(mListAdapter.getFooterLayoutCount() > 0) {
                    mListAdapter.removeAllFooterView();
                }

                mBlankLL.setVisibility(View.VISIBLE);
//                mFrame.setVisibility(View.GONE);
                isLoadingMore = false;

                onShowFail();
//                ToastUtils.show(getActivity(), resultJson.get("message").getAsString(), Toast.LENGTH_SHORT);
                return false;
            }
        });// end postJson
    }

    public void onShowFail() {

    }


    /**
     * 下拉刷新开始后
     */
    protected void refreshStartCallBack() {
    }

    /**
     * 下拉刷新结束后
     */
    protected void refreshEndCallBack() {
    }

    public void deleteItem(List<Integer> positionList) {
        mListAdapter.deleteItem(positionList);

        // 列表值为空
        if (mListAdapter.getData().size() < 1) {
            mRefreshDate = null;
            isLoaded = false;

            if (mBlankLL.getVisibility() != View.VISIBLE) {

                if(mListAdapter.getFooterLayoutCount() > 0) {
                    mListAdapter.removeAllFooterView();
                }

                mFrame.refreshComplete();
//                mFrame.setVisibility(View.GONE);
                mBlankLL.setVisibility(View.VISIBLE);
            }

            emptyCallBack();

        } else {
            // 获取的刷新时间
            mRefreshDate = Long.parseLong(mListAdapter.getData().get(0).get(GET_DATA_LIST_STR).getAsString());
        }
    }

    /**
     * 删除列表数据
     *
     * @param position
     */
    public void deleteItem(int position) {
        mListAdapter.deleteItem(position);
        // 列表值为空
        if (mListAdapter.getData().size() < 1) {
            mRefreshDate = null;
            isLoaded = false;

            if (mBlankLL.getVisibility() != View.VISIBLE) {

                if(mListAdapter.getFooterLayoutCount() > 0) {
                    mListAdapter.removeAllFooterView();
                }

                mFrame.refreshComplete();
//                mFrame.setVisibility(View.GONE);
                mBlankLL.setVisibility(View.VISIBLE);
            }

        } else {
            // 获取的刷新时间
            mRefreshDate = Long.parseLong(mListAdapter.getData().get(0).get(GET_DATA_LIST_STR).getAsString());
        }
    }

    public List<JsonObject> getData() {
        return mListAdapter.getData();
    }

    /**
     * 增加列表item数据
     *
     * @param jsonObject
     */
    public void insertItem(JsonObject jsonObject) {
        mListAdapter.insertItem(jsonObject);

        JsonObject subJsonObject = mListAdapter.getData().get(0);

        // 获取的刷新时间
        mRefreshDate = Long.parseLong(subJsonObject.get(GET_DATA_LIST_STR).getAsString());

        setRefreshSortFieldValue(subJsonObject);


        // 从空界面新增一条数据
        if (mListAdapter.getData().size() == 1) {
            isLoaded = true;
            mBlankLL.setVisibility(View.GONE);
            mFrame.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 增加列表item数据
     *
     * @param jsonObject
     */
    public void insertItemLast(JsonObject jsonObject) {
        mListAdapter.insertItemLast(jsonObject);

        JsonObject subJsonObject = mListAdapter.getData().get(0);

        // 获取的刷新时间
        mRefreshDate = Long.parseLong(subJsonObject.get(GET_DATA_LIST_STR).getAsString());

        setRefreshSortFieldValue(subJsonObject);


        // 从空界面新增一条数据
        if (mListAdapter.getData().size() == 1) {
            isLoaded = true;
            mBlankLL.setVisibility(View.GONE);
            mFrame.setVisibility(View.VISIBLE);
        }
    }

    public void reloadWithOutAnim() {

        if (null == mListAdapter) {
            return;
        }

        pageRefresh();

        // 加载更多
        mListAdapter.openLoadMore(PAGE_SIZE, true);
    }

    /**
     * 重新加载
     */
    public void reload() {

        // 初始化时间戳为0
        mRefreshDate = null;

        // 设置未加载过数据
        isLoaded = false;

        if (null == mListAdapter) {
            return;
        }

        // 加载更多
        mListAdapter.openLoadMore(PAGE_SIZE, true);

        // 自动刷新获取数据
        autoRefresh();
    }

    public void reload(String defaultVisitorParams) {
        mParams.setDefaultVisitorParams(defaultVisitorParams);
        mListAdapter.setNewData(new ArrayList<JsonObject>());
        mBlankLL.setVisibility(View.GONE);
        mFrame.setVisibility(View.VISIBLE);
        this.reload();
    }

    public void setDefaultVisitorParams(String defaultVisitorParams) {
        mParams.setDefaultVisitorParams(defaultVisitorParams);
    }

    public String getDefaultVisitorParams() {
        return mParams.getDefaultVisitorParams();
    }

    public void setIsPageRefresh(boolean flag) {
        isPageRefresh = flag;
    }


    /**
     * 显示自动刷新的样式
     */
    public void autoRefresh() {
        if(mFrame==null){
            return;
        }
        mFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFrame.autoRefresh();
            }
        }, 100);
    }

    /**
     * 获取交互参数
     *
     * @param type     类型
     * @param lastdate 时间戳
     * @return 默认的访问的参数
     */
    protected JsonObject getParams(final String type, final Long lastdate, final Object sortFieldValue) {

        // 默认的列表的参数
        JsonObject listVisitorParams = null;

        if (mParams != null) {
            String params = mParams.getDefaultVisitorParams();
            if (params != null) {
                listVisitorParams = GsonUtil.getJsonObject(params);
            } else {
                listVisitorParams = new JsonObject();
            }
        } else {
            listVisitorParams = new JsonObject();
        }

        if (sortFieldValue != null && !TextUtils.isEmpty(mParams.getSortFieldName())) {
//            pageParams.setSortFieldValue(sortFieldValue);
            String sortFieldName = "sortFieldName";
            if (!TextUtils.isEmpty(mParams.getCustomSortFields())) {
                sortFieldName = mParams.getCustomSortFields();
            }
            JsonParser jsonParser = new JsonParser();
            listVisitorParams.add(sortFieldName, jsonParser.parse(mParams.getSortFieldName()));
            listVisitorParams.add("sortFieldValue", jsonParser.parse(sortFieldValue.toString()));
        }

        listVisitorParams.addProperty("lastdate", lastdate);
        listVisitorParams.addProperty("pageSize", PAGE_SIZE);
        listVisitorParams.addProperty("type", type);

        // 构建公共提交的参数
        BaseParamsInits.getInstance(mContext).initParms(listVisitorParams);

        return listVisitorParams;
    }

    /**
     * 更新item数据
     *
     * @param index
     * @param jsonObject
     */
    public void updateItemData(int index, JsonObject jsonObject) {
        mListAdapter.updateItemData(index, jsonObject);
    }

    /**
     * 更新
     */
    public void update() {

        if (mListAdapter != null) {
            mListAdapter.update();
        }

    }

    /**
     * 获取当前的item数据
     *
     * @param index
     * @return
     */
    public JsonObject getItemData(int index) {
        return mListAdapter.getItem(index);
    }

    /**
     * 更改空白数据提示图片
     */
    private void changeBlankTipIV(String tipType) {
        if (TextUtils.isEmpty(tipType)) {
            return;
        }
        switch (tipType) {
            case "SEARCH":
                mBlankIV.setImageResource(R.drawable.img_tip_problem);
                break;
        }
    }

    /**
     * 设置下拉刷新的头部样式
     *
     * @param frameLayout
     */
    public void setHeadView(PtrClassicFrameLayout frameLayout) {


//        CurPtrHouseHeader header = new CurPtrHouseHeader(mContext);
//        frameLayout.setHeaderView(header);
//        frameLayout.addPtrUIHandler(header);
//
//        frameLayout.setEnabledNextPtrAtOnce(true);
//        frameLayout.setPullToRefresh(false);
//
//        frameLayout.setDurationToClose(2000);
//        frameLayout.setDurationToCloseHeader(200);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        onLoadMoreRequestedHandler.removeCallbacksAndMessages(null);
        // 关闭的时候 解绑
//        if (mRecyclerView != null && mRecyclerView.getViewTreeObserver() != null) {
//            mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);
//        }

    }

    /**
     * 访问服务端后 成功的回调
     */
    public interface ListCallBack<T> {
        /**
         * 成功有值后的回调
         *
         * @param list 列表数据
         */
        public void successCallBack(List<T> list);

        /**
         * 无数据
         */
        public void emptyCallBack();

    }

    /**
     * 获取列表的数量
     *
     * @return
     */
    public int getItemCount() {
        if (null == mListAdapter) return 0;
        return mListAdapter.getItemCount();
    }

    public void setAutoLoadMoreEnable(boolean autoLoadMoreEnable) {
        isAutoLoadMoreEnable = autoLoadMoreEnable;
    }

    public void setItemResId(int resId){
        if(mListAdapter!=null){
            mListAdapter.setLayoutResId(resId);
        }
    }

    /**
     * 只有一屏的时候是否显示 到底提示
     * @return
     */
    public boolean showNoDataViewOneScreen(){
        return true;
    }
}
