package com.cxw.cxwproject.imp;

import android.content.Context;
import android.widget.TextView;

import com.cxw.cxwproject.bean.SignBean;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest;
import com.cxw.cxwproject.inter.SignPageDateInter;
import com.cxw.cxwproject.widget.ToastUtils;

/**
 * 分享接口
 * Created by Administrator on 2017/8/25.
 */

public class SignShareDateImp extends BeanRequest.OnResponse<SignBean> {
    private TextView mIntegral;//总积分数
    private static SignShareDateImp instance = new SignShareDateImp();
    public static SignShareDateImp getInstance() {
        return instance;
    }

    /*
     * 接口数据请求
     * @param
     */
    public void SignShareDateImpApi(TextView mIntegral) {
        this.mIntegral=mIntegral;
        Api.getDailyShare(this);
    }

    @Override
    public void onResponse(SignBean o) {
        mIntegral.setText(o.getIntegral());
        UserBean.defaultShop().getIntegral().setIntegral(o.getIntegral());
    }

    @Override
    public void onErrorResponse(String error) {
        ToastUtils.show(error);
    }
}
