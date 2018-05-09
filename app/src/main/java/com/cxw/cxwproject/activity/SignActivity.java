package com.cxw.cxwproject.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.SignModeAdapter;
import com.cxw.cxwproject.bean.SignBean;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.dialog.SignShareDialog;
import com.cxw.cxwproject.dialog.SignShareDialog.OnItemClick;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imp.SignBtnDateImp;
import com.cxw.cxwproject.imp.SignGiftDateImp;
import com.cxw.cxwproject.imp.SignPageDateImp;
import com.cxw.cxwproject.imp.SignShareDateImp;
import com.cxw.cxwproject.inter.SignPageDateInter;
import com.cxw.cxwproject.inter.UmengShareInter;
import com.cxw.cxwproject.util.UmengShareDao;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.ToastUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class SignActivity extends ActionBarActivity
        implements SignPageDateInter, UmengShareInter, OnItemClick, RequestListener, OnClickListener, OnItemClickListener {
    private TextView integral, is_signin, continue_signin_days, next_reward_days;
    private MVCViewHelper<SignBean> mvcViewHelper;
    private SignShareDialog mShareDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    public int getActionBarType() {
        return ACTIONBAR_WHITE;
    }

    @Override
    protected void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("签到送积分");
        ImageView back = (ImageView) findViewById(R.id.back); // 返回
        back.setOnClickListener(this);
        integral = (TextView) findViewById(R.id.integral);// 总积分数
        is_signin = (TextView) findViewById(R.id.is_signin);// 是否签到
        continue_signin_days = (TextView) findViewById(R.id.continue_signin_days);
        next_reward_days = (TextView) findViewById(R.id.next_reward_days);
        // 积分说明
        TextView explain_btn = (TextView) findViewById(R.id.explain_btn);
        explain_btn.setOnClickListener(this);
        RelativeLayout signbtn = (RelativeLayout) findViewById(R.id.signbtn);
        signbtn.setOnClickListener(this);
        // 更多积分获取方式
        ListView sign_list = (ListView) findViewById(R.id.sign_list);
        sign_list.setOnItemClickListener(this);
        sign_list.setAdapter(new SignModeAdapter(this, R.layout.item_sign));
        // 分享相关
        mShareDialog = new SignShareDialog(this);
        mShareDialog.setOnItemClick(this);// 分享点击事件监听
        UmengShareDao.getUmengShare(this);
        // 网络请求加载显示页面数据
        SignPageDateImp.getInstance().SignPageDateInter(this);
        EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
        mvcViewHelper = MVCHelper.createViewHelper(null, emptyLayout);
        mvcViewHelper.setRequestListener(this);
        mvcViewHelper.refresh();
        // 连续签到奖励
        GridView mGridView = (GridView) findViewById(R.id.gift_gridview);
        SignGiftDateImp.getInstance().SignGiftDateImpApi(mvcViewHelper, mGridView);
        //签到请求接口反馈
        SignBtnDateImp.getInstance().SignPageDateInter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 != 2) {
            Intent mIntent = new Intent(this, GeneralizedIntegralActivity.class);
            mIntent.putExtra("id", arg2);
            startActivityForResult(mIntent, 100);
        } else {
            mShareDialog.show();
        }
    }

    /*
     * 页面控件数据绑定
     */
    private void ControlData(String integralt, int is_signint, String days, String score, String reward_days) {
        integral.setText(integralt);
        is_signin.setText(is_signint == 0 ? "签到" : "已签到");
        continue_signin_days.setText("连续" + days + "天");
        next_reward_days.setText("明日可领取" + score + "积分，距领取礼品还有" + reward_days + "天");
    }

    @Override
    public void requestData(int page) {
        SignPageDateImp.getInstance().SignPageDateImpApi(mvcViewHelper);
    }

    @Override
    public void onResponse(SignBean o) {
        ControlData(o.getIntegral(), o.getIs_signin(), o.getContinue_signin_days(), o.getScore(), o.getNext_reward_days());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:// 返回
                finish();
                break;
            case R.id.signbtn:// 签到
                SignBtnDateImp.getInstance().SignBtnDateImpApi(this);
                break;
            case R.id.explain_btn:// 积分说明
                startActivity(ExplainActivity.class);
                break;
        }
    }

    /*
     * 回调展现失败and成功
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 100100) {
                String result = data.getExtras().getString("result");
                integral.setText(result);
            }
        } else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void OnClick(SHARE_MEDIA media) {
        UmengShareDao.getUMWeb(this, media);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    public void UmengShareOk() {
        mShareDialog.dismiss();
        SignShareDateImp.getInstance().SignShareDateImpApi(integral);
    }
}
