package com.cxw.cxwproject.fragment;

import java.util.List;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.DetailsActivity;
import com.cxw.cxwproject.activity.DetailsActivity;
import com.cxw.cxwproject.adapter.FragmentPage2Adapter;
import com.cxw.cxwproject.bean.CommodityBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imp.BuyHotDateImp;
import com.cxw.cxwproject.util.mvc.MVCGridViewHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.RefreshLayout;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 特产
 *
 * @author Sekei
 */
public class FragmentPage2 extends BaseFragment implements RequestListener {
    private GridView mRefreshGrid;
    private FragmentPage2Adapter adapter;
    private MVCGridViewHelper<CommodityBean> mvcHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_2;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mRefreshGrid = (GridView) view.findViewById(R.id.pull_refresh_grid);
        adapter = new FragmentPage2Adapter(getActivity(), R.layout.item_specialty01);
        // 网络请求加载显示页面
        RefreshLayout swipeLayout = (RefreshLayout) view.findViewById(R.id.swipelayout);
        EmptyLayout emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        mvcHelper = MVCHelper.createGridHelper(swipeLayout, mRefreshGrid, emptyLayout);
        mvcHelper.setAdapter(adapter);
        mvcHelper.setRequestListener(this);
        mvcHelper.refresh();
    }


    @Override
    public void requestData(int page) {
        BuyHotDateImp.getInstance().BuyHotDateImpApi(mvcHelper, page + 1);
    }
}