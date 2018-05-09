package com.cxw.cxwproject.fragment;

import java.util.List;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.BaseFragmentOrder;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.OrderDetailsActivity;
import com.cxw.cxwproject.adapter.OrderPageAdapter;
import com.cxw.cxwproject.bean.OrderAllBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imp.OrderPageImp;
import com.cxw.cxwproject.inter.OrderDetailsViewInter;
import com.cxw.cxwproject.inter.OrderListViewItemInter;
import com.cxw.cxwproject.util.TLog;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCListViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.RefreshLayout;
import com.joanzapata.android.QuickAdapter;

import android.view.View;
import android.widget.ListView;

public class Order_Page3 extends BaseFragmentOrder
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
		((OrderPageAdapter) adapter).getOrderDetailsViewInter(this);
		// 这行代码一定要放在adapter之后
		new OrderPageImp().getOrderPageImp(getActivity(), adapter);
		// 网络请求
		mvcHelper = MVCHelper.createListHelper(swipeLayout, order_list, emptyLayout);
		mvcHelper.setAdapter(adapter);
		mvcHelper.setRequestListener(this);
		mvcHelper.refresh();
	}

	/**
	 * 待发货
	 */
	@Override
	public void requestData(int page) {
		Api.getOrderAll(page + 1, "100003", new OnResponse<List<OrderAllBean>>() {
			@Override
			public void onResponse(List<OrderAllBean> o) {
				mvcHelper.executeOnLoadDataSuccess(o);
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
		startActivity(OrderDetailsActivity.class, goods);
	}

	@Override
	public void OrderEvaluateViewOnClick(Object obj) {
		TLog.d("评论订单点击事件监听");
	}

	@Override
	public void volleySuccess(Object obj) {
		TLog.d("提醒发货，界面不做任何操作");
	}

	@Override
	protected void lazyLoad() {
		if (!isVisible) {
			return;
		}
		new OrderPageImp().setOrderListViewItemInter(this);
	}
}
