package com.cxw.cxwproject.fragment;

import java.util.List;

import com.cxw.cxwproject.BaseFragmentOrder;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.EvaluateActivity;
import com.cxw.cxwproject.activity.OrderDetailsActivity;
import com.cxw.cxwproject.adapter.OrderPageAdapter;
import com.cxw.cxwproject.bean.OrderAllBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imp.OrderPageImp;
import com.cxw.cxwproject.inter.OrderDetailsViewInter;
import com.cxw.cxwproject.inter.OrderListViewItemInter;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCListViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.RefreshLayout;
import com.joanzapata.android.QuickAdapter;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

public class Order_Page1 extends BaseFragmentOrder
		implements OrderListViewItemInter, RequestListener, OrderDetailsViewInter {
	private MVCListViewHelper<OrderAllBean> mvcHelper;
	private QuickAdapter<OrderAllBean> adapter;
	private OrderAllBean goods;

	@Override
	protected int getLayoutId() {
		return R.layout.order_page1;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		ListView order_list = (ListView) view.findViewById(R.id.order_list);
		RefreshLayout swipeLayout = (RefreshLayout) view.findViewById(R.id.swipelayout);
		EmptyLayout emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
		adapter = new OrderPageAdapter(getActivity(), R.layout.order_item);
		new OrderPageImp().getOrderPageImp(getActivity(), adapter);
		((OrderPageAdapter) adapter).getOrderDetailsViewInter(this);
		// 网络请求
		mvcHelper = MVCHelper.createListHelper(swipeLayout, order_list, emptyLayout);
		mvcHelper.setAdapter(adapter);
		mvcHelper.setRequestListener(this);
		mvcHelper.refresh();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == 101) {// 表示取消订单
				goods.setOrder_status(100011);
				adapter.notifyDataSetInvalidated();
			} else if (resultCode == 102) {// 删除订单
				adapter.remove(goods);
				adapter.notifyDataSetInvalidated();
			} else if (resultCode == 103) {// 确认收货
				goods.setOrder_status(100005);
				adapter.notifyDataSetInvalidated();
			} else if (resultCode == 104) {// 已经评价
				goods.setOrder_status(100006);
				adapter.notifyDataSetInvalidated();
			}
		}
	}

	@Override
	public void requestData(int page) {
		Api.getOrderAll(page + 1, "100000", new OnResponse<List<OrderAllBean>>() {
			@Override
			public void onResponse(List<OrderAllBean> data) {
				mvcHelper.executeOnLoadDataSuccess(data);
			}

			@Override
			public void onErrorResponse(String error) {
				mvcHelper.executeOnLoadDataFail(error);
			}
		});
	}

	/**
	 * 查看详情点击事件
	 */
	@Override
	public void OrderDetailsViewOnClick(Object obj) {
		goods = (OrderAllBean) obj;
		startActivityForResult(OrderDetailsActivity.class, 0, goods);
	}

	/**
	 * 订单评论
	 */
	@Override
	public void OrderEvaluateViewOnClick(Object obj) {
		goods = (OrderAllBean) obj;
		startActivityForResult(EvaluateActivity.class, 0, goods);
	}

	@Override
	public void volleySuccess(Object obj) {
		adapter.notifyDataSetInvalidated();
	}

	@Override
	protected void lazyLoad() {
		if (!isVisible) {
			return;
		}
		new OrderPageImp().setOrderListViewItemInter(this);
	}
}
