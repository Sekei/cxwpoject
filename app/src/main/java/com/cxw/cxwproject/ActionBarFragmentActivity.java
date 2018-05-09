package com.cxw.cxwproject;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.cxw.cxwproject.transparent.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;

public abstract class ActionBarFragmentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // 锁定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initActionBar();
        initView();
        // 友盟统计相关======================友盟统计==========================
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);
    }

    //初始化控件
    protected void initView() {
    }

    protected abstract int getLayoutId();

    public static final int ACTIONBAR_DARK = 0;//正常状态
    public static final int ACTIONBAR_WHITE = 1;//状态栏透明

    public int getActionBarType() {
        return ACTIONBAR_DARK;
    }

    private void initActionBar() {
        //设置通知栏是否透明问题
        if (getActionBarType() == ACTIONBAR_WHITE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
            }
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(false);// 通知栏透明
        }
        setContentView(getLayoutId());
    }

    //判断状态栏是否允许修改
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getTitle().toString());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getTitle().toString());
        MobclickAgent.onPause(this);
    }
}
