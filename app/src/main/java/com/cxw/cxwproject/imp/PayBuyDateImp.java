package com.cxw.cxwproject.imp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.cxw.cxwproject.bean.PayMentBean;
import com.cxw.cxwproject.bean.SignBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest;
import com.cxw.cxwproject.inter.ResponseInter;
import com.cxw.cxwproject.inter.SignPageDateInter;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.widget.ToastUtils;
import com.pingplusplus.android.PaymentActivity;

/**
 * 支付页面数据
 * Created by Administrator on 2017/8/25.
 */

public class PayBuyDateImp extends BeanRequest.OnResponse<PayMentBean> {

    private static PayBuyDateImp instance = new PayBuyDateImp();

    public static PayBuyDateImp getInstance() {
        return instance;
    }

    private LoadingDialog loading;//loading加载框

    private ResponseInter<PayMentBean> mResponseInter;

    //接口实例化反馈数据
    public void ResponseInter(ResponseInter<PayMentBean> mResponseInter) {
        this.mResponseInter = mResponseInter;
    }

    /*
     * 接口数据请求
     * @param mvcViewHelper
     */
    public void PayBuyDateImpApi(LoadingDialog loading, String mOrderId, int mPayType) {
        this.loading = loading;
        Api.getPayment(mOrderId, mPayType, this);
    }

    @Override
    public void onResponse(PayMentBean item) {
        loading.dismiss();
        mResponseInter.onResponse(item);
    }

    @Override
    public void onErrorResponse(String error) {
        loading.dismiss();
        ToastUtils.show(error);
    }
}
