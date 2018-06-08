package com.seaboxdata.portal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.linewell.core.CommonApplicaton;
import com.linewell.http.AppHttpResultHandler;
import com.seaboxdata.portal.common.CommonActivity;
import com.seaboxdata.portal.module.home.HomeFragment;
import com.seaboxdata.portal.module.info.InfoFragment;
import com.seaboxdata.portal.module.my.MyFragment;
import com.seaboxdata.portal.module.sentiment.SentimentFragment;
import com.seaboxdata.portal.module.work.WorkFragment;
import com.seaboxdata.portal.utils.CleanLeakedUtils;
import com.seaboxdata.portal.utils.badge.BadgeUtil;
import com.seaboxdata.portal.utils.badge.MessageCountUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends CommonActivity implements View.OnClickListener {

    public static final String KEY_INDEX = "KEY_INDEX";

    private static final int MESSAGE_REQUEST_CODE = 1112;

    private Context mContext = this;

    /**
     * 记录点击的view
     */
    private View clickedView;

    /**
     * Fragment管理
     */
    private FragmentManager mFragmentManager;


    //页签fragment
    private HomeFragment mHomeFragment;
    private InfoFragment mInfoFragment;
    private SentimentFragment mSentimentFragment;
    private WorkFragment mWorkFragment;
    private MyFragment mMyFragment;

    private int mCurIndex = HOME_INDEX;

    /**
     * 当前页面
     */
    private Fragment mCurrentFragment;
    //页签
    private View mHomeView;
    private View mInfoView; 
    private View mSentimentView;
    private View mWorkView;
    private View mMyView;
    //页签顺序
    public static final int HOME_INDEX = 0;
    public static final int INFO_INDEX = 1;
    public static final int SENTIMENT_INDEX = 2;
    public static final int WORK_INDEX = 3;
    public static final int MY_INDEX = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEnableToolbar(false);

//        if (!AppSessionUtils.getInstance().isLogin(this)) {
//            LoginActivity.startAction(this, 11);
//        }

        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        bindViews();

        onNewIntent(getIntent());

        checkVersionUpdate();

        if(savedInstanceState!=null){
            mCurIndex = savedInstanceState.getInt("test", HOME_INDEX);
        }

        handleNotifyIntent();

        EventBus.getDefault().register(this);
    }

    private void handleNotifyIntent(){
//        Intent intent = getIntent();
//        if(intent==null){
//            return;
//        }
//        Intent realIntent = intent.getParcelableExtra(NotifyReceiver.KEY_INTENT);
//        if(realIntent==null){
//            return;
//        }
//
//        startActivity(realIntent);
    }

    private void onRefreshFragments(){
        switch (mCurIndex){
            case HOME_INDEX:
                if (null != mHomeView) {
                    mHomeView.performClick();
                }
                break;
            case INFO_INDEX:
                if (null != mInfoView) {
                    mInfoView.performClick();
                }
                break;
            case SENTIMENT_INDEX:
                if (null != mSentimentView) {
                    mSentimentView.performClick();
                }
                break;
            case WORK_INDEX:
                if (null != mWorkView) {
                    mWorkView.performClick();
                }
                break;
            case MY_INDEX:
                if (null != mMyView) {
                    mMyView.performClick();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        refreshMsgCount();

    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefreshFragments();
    }

    /**
     * 控件绑定
     */
    private void bindViews() {
        //
        mHomeView = findViewById(R.id.home);
        mHomeView.setTag(HOME_INDEX);
        mHomeView.setOnClickListener(this);

        // 分类
        mInfoView = findViewById(R.id.market);
        mInfoView.setTag(INFO_INDEX);
        mInfoView.setOnClickListener(this);

        // 发现
        mSentimentView = findViewById(R.id.discovery);
        mSentimentView.setTag(SENTIMENT_INDEX);
        mSentimentView.setOnClickListener(this);

        mWorkView = findViewById(R.id.message);
        mWorkView.setTag(WORK_INDEX);
        mWorkView.setOnClickListener(this);

        //我的
        mMyView = findViewById(R.id.my);
        mMyView.setTag(MY_INDEX);
        mMyView.setOnClickListener(this);

        onClick(mHomeView);
    }


    /**
     * 当前页签按钮
     */
    private View currentTab = null;
    private View previousTab = null;

    @Override
    public void onClick(View v) {

        currentTab = v;
        if (changeFragment((Integer)v.getTag())) {
            changeTabMenu();
        }


    }


    /**
     * 切换页面
     *
     * @param index
     */
    private boolean changeFragment(int index) {

        int selectIndex = -1;

        if (null != previousTab) {

            selectIndex = (Integer) previousTab.getTag();
        }

        switch (index) {
            case HOME_INDEX:

                if (selectIndex == HOME_INDEX) {
                    return true;
                }

                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                } else {
                }
                switchFragment(mHomeFragment);
                mCurIndex = HOME_INDEX;
                break;
            case INFO_INDEX:

                if (selectIndex == INFO_INDEX) {
                    return true;
                }

                if (mInfoFragment == null) {
                    mInfoFragment = new InfoFragment();
                } else {
//                    ((DiscoveryFragment)mWarningFragment).reload();
                }
                switchFragment(mInfoFragment);
                mCurIndex = INFO_INDEX;
//                //关闭键盘
//                SystemUtils.hideSoftInput(MainActivity.this, 0);
                break;


            case SENTIMENT_INDEX:

                if (selectIndex == SENTIMENT_INDEX) {
                    return true;
                }

//                if (!AppSessionUtils.getInstance().isLogin(mContext)) {
//
//                    LoginActivity.startAction(this, MESSAGE_REQUEST_CODE, new OnResultListener() {
//                        @Override
//                        public void onResult(int requestCode, int resultCode, Intent data) {
//                            if(requestCode==MESSAGE_REQUEST_CODE){
//                                if(resultCode==RESULT_OK){
//                                    onClick(mDiscoveryView);
//                                }
//                            }
//                        }
//                    });
//                    return false;
//                }

                if (mSentimentFragment == null) {
                    mSentimentFragment = new SentimentFragment();
                }

                switchFragment(mSentimentFragment);
                mCurIndex = SENTIMENT_INDEX;
                break;
            case WORK_INDEX:

                if (selectIndex == WORK_INDEX) {
                    return true;
                }


                if (mWorkFragment == null) {
                    mWorkFragment = new WorkFragment();
                }

                switchFragment(mWorkFragment);
                mCurIndex = WORK_INDEX;

                break;
            case MY_INDEX:

                if (selectIndex == MY_INDEX) {
                    return true;
                }

                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                } else {
                    //刷新我的页面
                }
                switchFragment(mMyFragment);
                mCurIndex = MY_INDEX;
                break;
            default:
                break;
        }
        return true;

    }

    private Map<Fragment, Object> fragmentAddFlag = new HashMap<>();

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction fTransaction = mFragmentManager.beginTransaction();
        if (mCurrentFragment != null) {
            //隐藏现在的
            fTransaction.hide(mCurrentFragment);
        }

        if(fragment!=mHomeFragment&&mHomeFragment!=null){
            fTransaction.hide(mHomeFragment);
        }
        if(fragment!= mInfoFragment && mInfoFragment !=null){
            fTransaction.hide(mInfoFragment);
        }
        if(fragment!=mSentimentFragment&&mSentimentFragment!=null){
            fTransaction.hide(mSentimentFragment);
        }
        if (mWorkFragment!=null&&fragment!=mWorkFragment) {
            fTransaction.hide(mWorkFragment);
        }
        if(fragment!=mMyFragment&&mMyFragment!=null){
            fTransaction.hide(mMyFragment);
        }

        if (fragment.isAdded() || fragmentAddFlag.get(fragment) != null) {
            //已添加,直接显示
            fTransaction.show(fragment);
        } else {
            //未添加,添加
            fTransaction.add(R.id.content, fragment);
            fragmentAddFlag.put(fragment, true);
        }
        mCurrentFragment = fragment;

        fTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof HomeFragment) {
            mHomeFragment = (HomeFragment)fragment;
        } else if (fragment instanceof MyFragment) {
            mMyFragment = (MyFragment)fragment;
        } else if (fragment instanceof InfoFragment) {
            mInfoFragment = (InfoFragment)fragment;
        }  else if (fragment instanceof SentimentFragment) {
            mSentimentFragment = (SentimentFragment) fragment;
        } else if (fragment instanceof WorkFragment) {
            mWorkFragment = (WorkFragment) fragment;
        }
    }

    private void changeTabMenu() {
        if (previousTab != null) {
            previousTab.setSelected(false);
            changeIcon(previousTab);
        }
        currentTab.setSelected(true);
        changeIcon(currentTab);
        previousTab = currentTab;
    }

    /**
     * 切换图标
     *
     * @param v
     */
    private void changeIcon(View v) {

        int index = (Integer) v.getTag();
        ImageView img = (ImageView) v.findViewById(R.id.foot_img);
        if (img == null) {
            return;
        }
        boolean selected = v.isSelected();
        switch (index) {
            case HOME_INDEX:
                img.setImageResource(selected ? R.drawable.icon_home_active : R.drawable.icon_home);
                break;
            case INFO_INDEX:
                img.setImageResource(selected ? R.drawable.icon_home_service_active : R.drawable.icon_home_message);
                break;
            case SENTIMENT_INDEX:
                img.setImageResource(selected ? R.drawable.icon_home_discovery_active : R.drawable.icon_home_discovery);
                break;
            case WORK_INDEX:
                img.setImageResource(selected ? R.drawable.icon_home_office_active : R.drawable.icon_home_office);
                break;
            case MY_INDEX:
                img.setImageResource(selected ? R.drawable.icon_home_my_active : R.drawable.icon_home_my);
                break;
            default:
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mCurrentFragment != null) {
            mCurrentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurrentFragment != null) {
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        CleanLeakedUtils.fixInputMethodManagerLeaked(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private long exitTime = 0;// 按返回键的时间戳

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {

                Toast.makeText(this, com.linewell.core.R.string.exit_tip, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                finish();
//                System.exit(0);
                CommonApplicaton application = (CommonApplicaton) getApplication();
                application.finishAllActivities();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 启动首页并定位到某个页签
     *
     * @param context
     * @param indexType
     */
    public static void startAction(Context context, int indexType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_INDEX, indexType);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            int index = intent.getIntExtra(KEY_INDEX, -1);
            switch (index) {
                case 0://首页
                    if (null != mHomeView) {
                        mHomeView.performClick();
                    }
                    break;
                case 1://服务分类
                    if (null != mInfoView) {
                        mInfoView.performClick();
                    }
                    break;
                case 2://发现
                    if (null != mSentimentView) {
                        mSentimentView.performClick();
                    }
                    break;
                case 3: // 消息
                    if (null != mWorkView) {
                        mWorkView.performClick();
                    }
                    break;
                case 4://我的
                    if (null != mMyView) {
                        mMyView.performClick();
                    }
                    break;
            }
        }
    }

    private void refreshMsgCount(){

        //其他数量
        MessageCountUtils.refresh(this, new AppHttpResultHandler<Object>() {
            @Override
            public void onSuccess(Object result, JsonObject allResult) {
                super.onSuccess(result, allResult);
                onRefreshMsgCount();
            }
        });

//        if (null != mMessageFragment) {
//            mMessageFragment.refresh();
//        }


    }

    private void onRefreshMsgCount(){
        int count = MessageCountUtils.getAllCount();

        BadgeUtil.setBadgeCount(this, count);

//        if (mHomeFragment != null) {
//            mHomeFragment.refreshMesCount(count);
//        }
//
//        if (mInfoFragment != null) {
//            mInfoFragment.refreshMesCount(count);
//        }

        TextView mMesCountTV = (TextView)findViewById(R.id.messageCountTV);
        if (count > 0) {
            mMesCountTV.setVisibility(View.VISIBLE);

            if (count <= 99) {
                mMesCountTV.setText(count + "");
            } else {
                mMesCountTV.setText("99+");
            }

        } else {
            mMesCountTV.setVisibility(View.GONE);
            mMesCountTV.setText("");
        }
    }


    private void checkVersionUpdate() {

        //当前版本
//        String curVersion = ApkUtils.getVerName(MainActivity.this, MainActivity.this.getPackageName());
//        // 跳过更新的版本
//        String ignoreVersion = (String) SharedPreferencesUtil.get(MainActivity.this, SPKeyConstants.UPDATE_SERVICES.KEY_VERSION,
//                curVersion);
//
//        if(ApkUtils.compareVersion(ignoreVersion, curVersion)) {
//            curVersion = ignoreVersion;
//        }
//
//        AppVersionAPI.getInstance(ServiceConfig.BASE).getAppLastVersion(MainActivity.this, curVersion, new AppVersionResultHandler<AppUpgradeDTO>() {
//            @Override
//            public void onUpdate(AppUpgradeDTO appUpgradeDTO) {
//                UpdateVersion update = new UpdateVersion(mContext);
//                update.showAutoUpdateDialog(appUpgradeDTO);
//            }
//
//            @Override
//            public void onNewest() {
//
//            }
//        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (null != previousTab) {
            outState.putInt("test", (Integer)previousTab.getTag());
        }
        super.onSaveInstanceState(outState);
    }

    @Subscribe          //订阅事件FirstEvent
    public void onEventMainThread(MessageEvent event) {
        refreshMsgCount();
    }


    public static class MessageEvent{}




}
