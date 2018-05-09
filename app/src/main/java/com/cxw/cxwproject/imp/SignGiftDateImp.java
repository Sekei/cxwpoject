package com.cxw.cxwproject.imp;

import android.widget.GridView;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.SignActivity;
import com.cxw.cxwproject.adapter.SignGiftAdapter;
import com.cxw.cxwproject.bean.HomesBean;
import com.cxw.cxwproject.bean.RewardBean;
import com.cxw.cxwproject.bean.SignBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.util.mvc.PullRefreshListView;

import java.util.List;

/**
 * 连续签到礼品
 * Created by Administrator on 2017/8/25.
 */

public class SignGiftDateImp extends BeanRequest.OnResponse<List<RewardBean>> {
    private MVCViewHelper<SignBean> mvcViewHelper;
    private GridView mGridView;
    private static SignGiftDateImp instance = new SignGiftDateImp();

    public static SignGiftDateImp getInstance() {
        return instance;
    }

    /*
     * 接口数据请求
     * @param mvcViewHelper
     */
    public void SignGiftDateImpApi(MVCViewHelper<SignBean> mvcViewHelper,GridView mGridView) {
        this.mvcViewHelper = mvcViewHelper;
        this.mGridView=mGridView;
        Api.getReward(this);
    }

    @Override
    public void onResponse(List<RewardBean> item) {
        mGridView.setAdapter(new SignGiftAdapter(MyApp.getApp(), R.layout.item_reward, item));
    }

    @Override
    public void onErrorResponse(String error) {
        mvcViewHelper.executeOnLoadDataFail(error);
    }
}
