package com.seaboxdata.portal.module.search;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linewell.core.utils.AppSessionUtils;
import com.linewell.core.view.AutoFixViewGroup;
import com.linewell.innochina.core.entity.params.base.BaseParams;
import com.linewell.utils.GsonUtil;
import com.linewell.utils.SharedPreferencesUtil;
import com.linewell.utils.SystemUtils;
import com.seaboxdata.portal.R;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.common.CustomDialog;
import com.seaboxdata.portal.common.input.BaseInputLinearLayout;
import com.seaboxdata.portal.utils.StatusBarUtil;
import com.seaboxdata.portal.utils.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面
 */

public class SearchActivity extends CommonActivity {
    private static final String SP_KEY_SEARCH_HISTORY = "SP_KEY_SEARCH_HISTORY";

    private static final int TABLAYOT_INDICATOR_LENGTH = 10;

    private Context mContext;

    private List<String> mSearchHistory = new ArrayList<>();

    private LinearLayout mSearchHistoryListview;

    private View layoutSearchHistory;

    private BaseInputLinearLayout input;

    private View layoutSearchTips;//热门搜索
    private View layoutSearchResult;//结果
    private View layoutSearchRele;//联想词

    private LinearLayout listViewSearchRele;

    private SearchResultFragment mMultipleFragment;

    private String mLastSearchKey = "";

    public static void startAction(Activity activity, String value) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(CommonActivity.KEY_DATA, value);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search, R.layout.fragment_search_bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);

            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.search_bar_ll);
            linear_bar.getLayoutParams().height = linear_bar.getLayoutParams().height + getStatusBarHeight();
            linear_bar.setPadding(linear_bar.getPaddingLeft(), linear_bar.getPaddingTop() + getStatusBarHeight(),
                    linear_bar.getPaddingRight(), linear_bar.getPaddingBottom());

            RelativeLayout rootView = (RelativeLayout) findViewById(R.id.search_root_view);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rootView.getLayoutParams();
            layoutParams.setMargins(0, SystemUtils.dip2px(mCommonContext, 24), 0, 0);

            if (StatusBarUtil.StatusBarLightMode(this) == 0) {
//                linear_bar.setBackgroundColor(mContext.getResources().getColor(R.color.black_opacity80));
            }
        }

        initView();

        bindViews();
        EventBus.getDefault().register(this);
    }

    private void initView() {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void bindViews() {
        mContext = getApplicationContext();

        layoutSearchTips = findViewById(R.id.layout_search_tips);
        layoutSearchResult = findViewById(R.id.layout_search_result);
        layoutSearchRele = findViewById(R.id.search_rele_sv);

        listViewSearchRele = (LinearLayout) layoutSearchRele.findViewById(R.id.prefix_result_ll);
        listViewSearchRele.setOnTouchListener(hideSoftInputListener);

        mSearchHistoryListview = (LinearLayout) findViewById(R.id.list_search_history);
        layoutSearchHistory = findViewById(R.id.layout_search_history);

        // TODO
        getHotKeyword();

        //搜索框
        input = (BaseInputLinearLayout) findViewById(R.id.search_input_ll);
        input.getEditText().setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input.getEditText().setSingleLine();
        String value = getIntent().getStringExtra(KEY_DATA);
        if (!TextUtils.isEmpty(value)) {
            input.getEditText().setHint(value);
            Selection.setSelection(input.getEditText().getText(), input.getEditText().getText().length());
        }
        input.getIconClearInput().setTextColor(mCommonActivity.getResources().getColor(R.color.white_opacity50));
        input.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_UP) {
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String content = input.getEditTextContent().intern();
                    if(TextUtils.isEmpty(content)){
                        CharSequence hint = input.getEditText().getHint();
                        if(!TextUtils.isEmpty(hint)){
                            search(hint.toString());
                            input.getEditText().setText(hint);
                            input.getEditText().setSelection(input.getEditTextContent().length());
                            input.getEditText().clearFocus();
                        }
                    }else{
                        search(content);
                    }
                }
                return false;
            }
        });
        input.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable!=null&&editable.length()>0){
                    //有内容
                    if(editable.toString().equals(mLastSearchKey)){
                        hideSearchRele();
                    }else{
                        //显示联想词
                        showSearchRele(editable.toString());
                        mLastSearchKey = "";
                    }
                }else{
                    //没内容
                    hideResult();
                    //隐藏联想词
                    hideSearchRele();
                }
            }
        });

        input.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(input.getEditText().getText())){
                    //无内容
                }else{
                    //显示联想词
                    showSearchRele(input.getEditText().getText().toString());
                }
            }
        });

        SystemUtils.setTextCursor(input.getEditText(), R.drawable.shape_cursor_white);

        if(getIntent()!=null){
            String hint = getIntent().getStringExtra(KEY_DATA);
            if(!TextUtils.isEmpty(hint)){
                input.getEditText().setHint(hint);
            }
        }

        findViewById(R.id.fragment_fit_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        showSearchHistory();

        //清空搜索记录
        findViewById(R.id.button_clear_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClearHistoryDialog();
            }
        });

//        initTabs();

        hideResult();
    }

    private void getHotKeyword(){
        BaseParams params = new BaseParams();
//        AppHttpUtils.postJson(mContext, InnochinaServiceConfig.HOME_SERVICE.GET_HOT_KEYWORD, params, new AppHttpResultHandler<Object>() {
//            @Override
//            public void onSuccess(Object result, JsonObject allResult) {
//                super.onSuccess(result, allResult);
//                if(result==null){
//                    return;
//                }
//                List<String> list = GsonUtil.jsonToBean(result.toString(), new TypeToken<List<String>>(){}.getType());
//                showHotKeyword(list);
//            }
//        });

        List<String> list = new ArrayList<>();
        list.add("北京流动人口");
        list.add("高新企业流动人数");
                showHotKeyword(list);

    }

    private void showHotKeyword(List<String> list){
        if(list==null||list.size()==0){
            return;
        }
        findViewById(R.id.layout_hot_keyword).setVisibility(View.VISIBLE);

        AutoFixViewGroup autoFixViewGroup = (AutoFixViewGroup) findViewById(R.id.viewgroup_keywor_list);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        for(final String item:list){
            View view = layoutInflater.inflate(R.layout.item_search_hot, autoFixViewGroup, false);
            TextView textView = (TextView) view.findViewById(R.id.text_keyword);
            textView.setText(item);
            autoFixViewGroup.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search(item);
                    input.getEditText().setText(item);
                    input.getEditText().setSelection(input.getEditTextContent().length());
                    input.getEditText().clearFocus();
                }
            });
        }
    }

    /**
     * 显示结果集
     */
    private void showResult(){
        layoutSearchTips.setVisibility(View.GONE);
        layoutSearchResult.setVisibility(View.VISIBLE);

        //隐藏联想词
        hideSearchRele();
    }

    /**
     * 隐藏结果
     */
    private void hideResult(){
        mLastSearchKey = "";

        layoutSearchTips.setVisibility(View.VISIBLE);
        layoutSearchResult.setVisibility(View.GONE);

        hideEmptyView();
    }

    /**
     * 显示历史搜索
     */
    private void showSearchHistory(){
        String searchHistListStr = (String) SharedPreferencesUtil.get(this, getSpKeySearchHistory(), "");
        if(TextUtils.isEmpty(searchHistListStr)){
            //搜索记录为空
            layoutSearchHistory.setVisibility(View.GONE);
        }else{
            layoutSearchHistory.setVisibility(View.VISIBLE);

//            mSearchHistory = GsonUtil.jsonToBean(searchHistListStr,  new TypeToken<List<String>>(){}.getType());

            mSearchHistory.add("交通指数");

            if(mSearchHistory.size()==0){
                layoutSearchHistory.setVisibility(View.GONE);
                return;
            }
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            for(final String str:mSearchHistory){
                addSearchItemView(layoutInflater, str);
            }
        }
    }

    /**
     * 在界面上添加一个历史搜索
     * @param layoutInflater
     * @param str
     */
    private void addSearchItemView(LayoutInflater layoutInflater, final String str){
        final View searchItemView = layoutInflater.inflate(R.layout.item_search_history, mSearchHistoryListview, false);
        TextView searchItem = (TextView) searchItemView.findViewById(R.id.text_search_item);
        searchItem.setText(str);

        searchItemView.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mSearchHistoryListview.removeView(searchItemView);
                removeSearchHistory(str);
            }
        });
        searchItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(str);
                input.getEditText().setText(str);
                input.getEditText().setSelection(input.getEditTextContent().length());
                input.getEditText().clearFocus();
            }
        });
        mSearchHistoryListview.addView(searchItemView, 0);
    }

    private void showClearHistoryDialog(){
        CustomDialog dialog = new CustomDialog.Builder(SearchActivity.this).setTitle("确认删除全部历史记录？")
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearSearchHistory();
                        mSearchHistoryListview.removeAllViews();
                        showSearchHistory();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();
    }

    /**
     * 搜索
     * @param searchStr
     */
    private void search(String searchStr){
        mLastSearchKey = searchStr;


        input.getEditText().clearFocus();
        // 先隐藏键盘
        SystemUtils.hideSoftInput(SearchActivity.this, 0);

        addSearchHistory(searchStr);

        showResult();

        initTabs();
    }

    private String getSpKeySearchHistory(){
        String loginId = "";
        if(AppSessionUtils.getInstance().isLogin(this)){
            loginId = AppSessionUtils.getInstance().getLoginInfo(this).getUserId();
        }
        return SP_KEY_SEARCH_HISTORY+loginId;
    }

    /**
     * 增加搜索记录
     * @param searchStr
     */
    private void addSearchHistory(String searchStr){
        if(TextUtils.isEmpty(searchStr)){
            return;
        }
        removeSearchHistory(searchStr);

        if(mSearchHistory.size()>=10){
            mSearchHistoryListview.removeViewAt(mSearchHistory.size()-1);
            mSearchHistory.remove(0);
        }

        mSearchHistory.add(searchStr);
        SharedPreferencesUtil.save(this, getSpKeySearchHistory(), GsonUtil.getJsonStr(mSearchHistory));

        layoutSearchHistory.setVisibility(View.VISIBLE);
        addSearchItemView(LayoutInflater.from(this), searchStr);
    }

    /**
     * 移除搜索记录
     * @param searchStr
     */
    private void removeSearchHistory(String searchStr){
        int result = -1;
        for(int i=0;i<mSearchHistory.size();i++){
            String item = mSearchHistory.get(i);
            if(item.equals(searchStr)){
                result = i;
                break;
            }
        }
        if(result>=0){
            mSearchHistoryListview.removeViewAt(mSearchHistory.size()-1-result);
            mSearchHistory.remove(result);
        }
        SharedPreferencesUtil.save(this, getSpKeySearchHistory(), GsonUtil.getJsonStr(mSearchHistory));

        if(mSearchHistory.size()==0){
            layoutSearchHistory.setVisibility(View.GONE);
        }
    }

    /**
     * 清空搜索记录
     */
    private void clearSearchHistory(){
        mSearchHistory.clear();
        SharedPreferencesUtil.save(this, getSpKeySearchHistory(), GsonUtil.getJsonStr(mSearchHistory));
    }


    private void initTabs(){
        if(mMultipleFragment==null){

            String searchStr = input.getEditTextContent();

            FragmentManager fragmentManage = getSupportFragmentManager();
            if (fragmentManage.getFragments() == null || fragmentManage.getFragments().size() == 0) {
                FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
                mMultipleFragment = SearchResultFragment.createNew(searchStr);

                fTransaction.add(R.id.search_list_fl, mMultipleFragment);
                fTransaction.commit();
            }


        }else{
            //刷新
            if(mMultipleFragment!=null){
                String searchStr = mLastSearchKey;

                mMultipleFragment.setKeyword(searchStr);
                mMultipleFragment.reloadWithOutAnim();

            }

        }
    }

    @Subscribe          //订阅事件FirstEvent
    public  void onEventMainThread(CommonRefreshEvent event){
        if(event.getValue()==View.VISIBLE){
            hideEmptyView();
            hideLoadingView();
        }else if(event.getValue()==-2){
            showLoadingView();
        }else{
            showEmptyView();
        }
    }

    @Override
    public View initEmptyView() {
        View view = LayoutInflater.from(mCommonContext).inflate(R.layout.recycler_view_blank_ll,null,false);
        view.setBackgroundColor(getResources().getColor(R.color.backgroundGrey));
        ImageView imageView = (ImageView) view.findViewById(R.id.blank_iv);
        imageView.setImageResource(R.drawable.img_blank_common);

        TextView textView = (TextView) view.findViewById(R.id.blank_tip_tv);
        textView.setText("没有找到您想要的内容");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                autoRefresh();
            }
        });
        return view;
    }

    /**
     * 滑动隐藏键盘
     */
    private View.OnTouchListener hideSoftInputListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                SystemUtils.hideSoftInput(SearchActivity.this, 0);
            }
            return false;
        }
    };

    /**
     * 显示联想词
     */
    private void showSearchRele(final String prefix){
//        String url = InnochinaServiceConfig.HOME_SERVICE.GET_SEARCH_KEYWORD_PREFIX;
//        KeywordPrefixParams params = new KeywordPrefixParams();
//        params.setPrefix(prefix);
////
//        AppHttpResultHandler appHttpResultHandler = new AppHttpResultHandler<Object>() {
//            @Override
//            public void onSuccess(Object result, JsonObject allResult) {
//                super.onSuccess(result, allResult);
//                List<String> list = null;
//                if(!StringUtils.isEmpty(result.toString())){
//                    list = GsonUtil.jsonToBean(result.toString(), new TypeToken<List<String>>(){}.getType());
//                }
//                renderSearchRele(list, prefix);
//            }
//        };
//        AppHttpUtils.postJson(this, url, params, appHttpResultHandler);

    }

    private void renderSearchRele(List<String> list, String key){
//        if(list==null || list.size()==0){
//            hideSearchRele();
//            return;
//        }
//        layoutSearchRele.setVisibility(View.VISIBLE);
//        layoutSearchTips.setVisibility(View.GONE);
//        layoutSearchResult.setVisibility(View.GONE);
//
//        listViewSearchRele.removeAllViews();
//        for(final String item:list){
//            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
//            View viewItem = layoutInflater.inflate(R.layout.item_search_match, listViewSearchRele, false);
//
//            String color = "#ff3b30";
//            String keyWord = "<font color='" + color + "'>" + key + "</font>";
//            final String showText = item.replaceAll(key, keyWord);
//
//            //搜索关键字
//            TextView textView = (TextView) viewItem.findViewById(R.id.text_search_relative);
//            textView.setText(Html.fromHtml(showText));
//
//            viewItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    search(item);
//                    input.getEditText().setText(item);
//                    input.getEditText().setSelection(input.getEditTextContent().length());
//                    input.getEditText().clearFocus();
//                }
//            });
//
//            listViewSearchRele.addView(viewItem);
//        }

    }

    /**
     * 隐藏联想词
     */
    private void hideSearchRele(){
        layoutSearchRele.setVisibility(View.GONE);

    }

    public static class CommonRefreshEvent {

        private int value = 0;

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


}
