package com.cxw.cxwproject.imp;

import com.cxw.cxwproject.bean.HomesBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest;
import com.cxw.cxwproject.util.mvc.PullRefreshListView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public class HomeDateImp extends BeanRequest.OnResponse<List<HomesBean>> {
    private PullRefreshListView<HomesBean> mvcHelper;
    private static HomeDateImp instance = new HomeDateImp();

    public static HomeDateImp getInstance() {
        return instance;
    }

    /*
     * 接口数据请求
     * @param mvcHelper
     */
    public void HomeDateImpApi(PullRefreshListView<HomesBean> mvcHelper) {
        this.mvcHelper = mvcHelper;
        Api.getHome(this);
    }

    @Override
    public void onResponse(List<HomesBean> o) {
        mvcHelper.executeOnLoadDataSuccess(o);
    }

    @Override
    public void onErrorResponse(String error) {
        mvcHelper.executeOnLoadDataFail(error);
    }
}
