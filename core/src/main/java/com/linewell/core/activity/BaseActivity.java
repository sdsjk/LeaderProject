package com.linewell.core.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.linewell.core.CommonApplicaton;
import com.linewell.core.R;


/**
 * Activity基类，提供返回以及再按一次退出的功能
 */
public abstract class BaseActivity extends PortraitActivity {

    private long exitTime = 0;// 按返回键的时间戳

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN){

            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(this, R.string.exit_tip, Toast.LENGTH_SHORT).show();
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
}
