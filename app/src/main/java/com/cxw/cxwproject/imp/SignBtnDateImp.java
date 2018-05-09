package com.cxw.cxwproject.imp;

import android.content.Context;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.bean.SignBean;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest;
import com.cxw.cxwproject.inter.SignPageDateInter;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.widget.ToastUtils;

/**
 * 签到点击接口
 * Created by Administrator on 2017/8/25.
 */

public class SignBtnDateImp extends BeanRequest.OnResponse<SignBean> {
    private SignPageDateInter mSignPageDateInter;
    private LoadingDialog loading;//loading加载框
    private static SignBtnDateImp instance = new SignBtnDateImp();

    public static SignBtnDateImp getInstance() {
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
    public void SignBtnDateImpApi(Context mContext) {
        loading = new LoadingDialog(mContext);
        Api.getSignBtn(this);
    }

    @Override
    public void onResponse(SignBean o) {
        loading.dismiss();
        ToastUtils.show("签到成功");
        UserBean.defaultShop().getIntegral().setIntegral(o.getIntegral());
        mSignPageDateInter.onResponse(o);
    }

    @Override
    public void onErrorResponse(String error) {
        loading.dismiss();
        ToastUtils.show(error);
    }
}
