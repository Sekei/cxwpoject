package com.cxw.cxwproject.imp;

import com.cxw.cxwproject.bean.CommodityBean;
import com.cxw.cxwproject.bean.SignBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest;
import com.cxw.cxwproject.inter.SignPageDateInter;
import com.cxw.cxwproject.util.mvc.MVCGridViewHelper;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;

import java.util.List;

/**
 * 热购接口请求
 * Created by Administrator on 2017/8/25.
 */

public class BuyHotDateImp extends BeanRequest.OnResponse<List<CommodityBean>> {
    private MVCGridViewHelper<CommodityBean> mvcHelper;
    private static BuyHotDateImp instance = new BuyHotDateImp();

    public static BuyHotDateImp getInstance() {
        return instance;
    }

    /*
     * 接口数据请求
     * @param mvcHelper
     */
    public void BuyHotDateImpApi(MVCGridViewHelper<CommodityBean> mvcHelper, int page) {
        this.mvcHelper = mvcHelper;
        Api.getBestSellers(page, this);
    }

    @Override
    public void onResponse(List<CommodityBean> o) {
        mvcHelper.executeOnLoadDataSuccess(o);
    }

    @Override
    public void onErrorResponse(String error) {
        mvcHelper.executeOnLoadDataFail(error);
    }
}
