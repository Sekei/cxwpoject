package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarFragmentActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.fragment.FragmentPage1;
import com.cxw.cxwproject.fragment.FragmentPage2;
import com.cxw.cxwproject.fragment.FragmentPage3;
import com.cxw.cxwproject.fragment.FragmentPage4;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.util.UpdateManager;
import com.cxw.cxwproject.widget.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeActivity extends ActionBarFragmentActivity implements OnClickListener, OnCheckedChangeListener {
    private long exitTime = 0;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentPage1 mFragmentPage1;// 首页
    private FragmentPage2 mFragmentPage2;// 特产
    private FragmentPage3 mFragmentPage3;// 购物车
    private FragmentPage4 mFragmentPage4;// 个人中心
    private RadioButton rbOne, rblogistics, rbThree;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }


    @Override
    protected void initView() {
        super.initView();
        rbOne = (RadioButton) findViewById(R.id.rbOne);// 首页
        rbThree = (RadioButton) findViewById(R.id.rbThree);// 购物车
        rblogistics = (RadioButton) findViewById(R.id.rblogistics);// 个人中心
        pageListener();// 默认选中首页
        RadioGroup bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
        bottomRg.setOnCheckedChangeListener(this);
        new UpdateManager(this).checkLatestVersion();// 检查版本更新
        // 个人中心判断
        View core_btn = findViewById(R.id.personalcenter_btn);
        core_btn.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbOne:// 首页
                pageListener();
                break;
            case R.id.rbTwo:// 特产
                if (null == mFragmentPage2) {
                    mFragmentPage2 = new FragmentPage2();
                }
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragement_main, mFragmentPage2);
                fragmentTransaction.commit();
                break;
            case R.id.rbThree:// 购物车
                if (null == mFragmentPage3) {
                    mFragmentPage3 = new FragmentPage3();
                }
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragement_main, mFragmentPage3);
                fragmentTransaction.commit();
                break;
        }
    }

    /*
     * 首页跳转方法
     */
    public void pageListener() {
        if (null == mFragmentPage1) {
            mFragmentPage1 = new FragmentPage1();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragement_main, mFragmentPage1);
        fragmentTransaction.commit();
        rbOne.setChecked(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra("NewIntent").equals("1000100")) {
            pageListener();// 首页
        } else if (intent.getStringExtra("NewIntent").equals("1000101")) {
            if (null == mFragmentPage3) {
                mFragmentPage3 = new FragmentPage3();
            }
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragement_main, mFragmentPage3);
            fragmentTransaction.commit();
            rbThree.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personalcenter_btn:
                if (new SharePreferenceUtil().getCookie() != null) {
                    if (null == mFragmentPage4) {
                        mFragmentPage4 = new FragmentPage4();
                    }
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragement_main, mFragmentPage4);
                    fragmentTransaction.commit();
                    rblogistics.setChecked(true);
                } else {
                    startActivity(LoginActivity.class, 1);
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            exitTime = System.currentTimeMillis();
            ToastUtils.show("再按一次退出程序!");
            return;
        } else {
            MobclickAgent.onProfileSignOff();
        }
        super.onBackPressed();
    }

}
