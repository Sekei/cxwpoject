package com.cxw.cxwproject.cart;

import java.util.List;

import com.cxw.cxwproject.adapter.CartAdapter;
import com.cxw.cxwproject.bean.ShoppingCartBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.tool.CartTool;
import com.cxw.cxwproject.widget.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Context;

public class CartDataHttpApi {
	/**
	 * 这个类不能实例化
	 */
	private CartDataHttpApi() {
	}

	public static OnCartHttpData mOnCartHttpData;

	public static void setOnCartHttpData(OnCartHttpData mOnCartHttpData) {
		CartDataHttpApi.mOnCartHttpData = mOnCartHttpData;
	}

	/**
	 * 购物车数据请求
	 * 
	 * @param pull_refresh_cartlist
	 */
	public static void getShoppingCart(final PullToRefreshListView pull_refresh_cartlist) {
		Api.getShoppingCart(new OnResponse<List<ShoppingCartBean>>() {
			@Override
			public void onResponse(List<ShoppingCartBean> item) {
				pull_refresh_cartlist.onRefreshComplete();
				mOnCartHttpData.onCartData(item);
			}

			@Override
			public void onErrorResponse(String error) {
				pull_refresh_cartlist.onRefreshComplete();
				ToastUtils.show(error);
			}
		});
	}

	/**
	 * 删除购物车数据
	 * 
	 * @param pull_refresh_cartlist
	 * @param mcontext
	 * @param listItems
	 * @param adapter
	 */
	public static void getDelete(final PullToRefreshListView pull_refresh_cartlist, Context mcontext,
			List<ShoppingCartBean> listItems, CartAdapter adapter) {
		final LoadingDialog loading = new LoadingDialog(mcontext);
		Api.getDelete(CartTool.getInstance().deleteOrCheckOutShop(listItems, adapter), new OnResponse<String>() {
			@Override
			public void onResponse(String item) {
				getShoppingCart(pull_refresh_cartlist);
				loading.dismiss();
			}

			@Override
			public void onErrorResponse(String error) {
				loading.dismiss();
				ToastUtils.show(error);
			}
		});
	}

	/**
	 * 数据请求成功回调使用
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnCartHttpData {
		public void onCartData(List<ShoppingCartBean> itemShoppingCartBean);
	}
}
