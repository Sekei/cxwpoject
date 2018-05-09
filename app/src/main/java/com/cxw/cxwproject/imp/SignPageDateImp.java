package com.cxw.cxwproject.imp;

import com.cxw.cxwproject.bean.SignBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest;
import com.cxw.cxwproject.inter.SignPageDateInter;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;

/**
 * 签到页面数据
 * Created by Administrator on 2017/8/25.
 */

public class SignPageDateImp extends BeanRequest.OnResponse<SignBean> {
    private MVCViewHelper<SignBean> mvcViewHelper;
    private SignPageDateInter mSignPageDateInter;
    private static SignPageDateImp instance = new SignPageDateImp();

    public static SignPageDateImp getInstance() {
        return instance;
    }

    //接口实例化反馈数据
    public void SignPageDateInter(SignPageDateInter mSignPageDateInter) {
        this.mSignPageDateInter = mSignPageDateInter;
    }

    /*
     * 接口数据请求
     * @param mvcViewHelper
     */
    public void SignPageDateImpApi(MVCViewHelper<SignBean> mvcViewHelper) {
        this.mvcViewHelper = mvcViewHelper;
        Api.getSign(this);
    }

    @Override
    public void onResponse(SignBean o) {
        mvcViewHelper.executeOnLoadDataSuccess(o);
        mSignPageDateInter.onResponse(o);
    }

    @Override
    public void onErrorResponse(String error) {
        mvcViewHelper.executeOnLoadDataFail(error);
    }
}
