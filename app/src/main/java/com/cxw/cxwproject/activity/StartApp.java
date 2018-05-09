package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.AdvertisementBean;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.cxw.cxwproject.tool.AnimationTool;
import com.cxw.cxwproject.tool.AnimationTool.onAnimationEnd;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.tool.StartGuideViewPage;
import com.cxw.cxwproject.tool.StartGuideViewPage.PageOnClik;
import com.cxw.cxwproject.util.CountDownView;
import com.cxw.cxwproject.util.CountDownView.CountDownTimerListener;
import com.cxw.cxwproject.util.TDevice;
import com.cxw.cxwproject.util.TLog;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class StartApp extends ActionBarActivity
        implements onAnimationEnd, OnClickListener, CountDownTimerListener, PageOnClik {
    private SharePreferenceUtil preferences;
    private String bannerurl;
    private CountDownView count_down_view;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    public int getActionBarType() {
        return ACTIONBAR_WHITE;
    }

    @Override
    protected void initView() {
        preferences = new SharePreferenceUtil();
        if (preferences.getisFirst() != TDevice.getVersionCode()) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
            viewPager.setVisibility(View.VISIBLE);
            StartGuideViewPage.getStartPage().mPageOnClik(StartApp.this).initViews(this, viewPager);
        } else {
            //2秒淡显效果
            //AnimationTool.getAnimation().setOnAnimationEnd(StartApp.this).setShowAnimation(start_img, 2000);
            login();//登录请求
            Api.getDeviceImg(Banner);// 请求广告
        }
    }

    private void login() {
        String Member = new SharePreferenceUtil().getMember();
        String Pwd = new SharePreferenceUtil().getPwd();
        if (Member != null) {
            Api.login(Member, Pwd, User);
        }
    }

    /*
     * 密码登录请求
     */
    OnResponse<UserBean> User = new OnResponse<UserBean>() {
        @Override
        public void onResponse(UserBean o) {
            UserBean.save(o);
        }

        @Override
        public void onErrorResponse(String error) {
            TLog.d("StartApp", "密码登录失败");
        }
    };

    /**
     * 广告数据请求
     */
    OnResponse<AdvertisementBean> Banner = new OnResponse<AdvertisementBean>() {
        @Override
        public void onResponse(AdvertisementBean o) {
            initBannerData(o);
        }

        @Override
        public void onErrorResponse(String error) {
            TLog.d("StartApp", "启动广告页面请求失败");
            startActivity(HomeActivity.class);
        }
    };

    private void initBannerData(AdvertisementBean o) {
        if (o != null) {
            ImageView device_img = (ImageView) findViewById(R.id.device_img);
            device_img.setVisibility(View.VISIBLE);
            ImageDisplay.getSingleton().ImageLoader(device_img, o.getImg_url(), false);
            count_down_view = (CountDownView) findViewById(R.id.countDownView);
            count_down_view.setVisibility(View.VISIBLE);
            count_down_view.setCountDownTimerListener(this);
            count_down_view.start((long) o.getDuration() * 360);
            count_down_view.setOnClickListener(this);
            if (o.isIs_click()) {
                bannerurl = o.getUrl();
                device_img.setOnClickListener(this);
            }
        } else {
            startActivity(HomeActivity.class);
        }
    }

    @Override
    protected void onRestart() {
        this.finish();
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device_img:
                count_down_view.FINISH = false;
                startActivity(BannerActivity.class, bannerurl);
                break;
            case R.id.countDownView:
                count_down_view.FINISH = false;
                startActivity(HomeActivity.class);
                break;
            default:
                break;
        }

    }

    @Override
    public void onPageOnClik() {
        preferences.setIsFirst(TDevice.getVersionCode());
        startActivity(HomeActivity.class);
    }

    @Override
    public void onStartCount() {
        TLog.d("Timer", "显示多少秒钟");
    }

    @Override
    public void onFinishCount() {
        startActivity(HomeActivity.class);
    }

    @Override
    public void animationEnd() {
        TLog.d("淡出效果结束");
        Api.getDeviceImg(Banner);// 请求广告
    }
}
