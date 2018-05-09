package com.cxw.cxwproject.imp;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.bean.OrderAllBean;
import com.cxw.cxwproject.dialog.CustomDialog;
import com.cxw.cxwproject.dialog.CustomDialog.Builder;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.inter.OrderListViewItemInter;
import com.cxw.cxwproject.inter.OrderPageInter;
import com.cxw.cxwproject.widget.ToastUtils;
import com.joanzapata.android.QuickAdapter;

import android.content.Context;
import android.content.DialogInterface;

/**
 * 订单业务层
 * 
 * @author Administrator
 */
public class OrderPageImp implements OrderPageInter {
	private static Context mContext;
	private static QuickAdapter<OrderAllBean> mAdapter;

	/**
	 * 单例模式
	 */
	// private static OrderPageImp instance = new OrderPageImp();
	//
	// public static OrderPageImp getInstance() {
	// return instance;
	// }
	private static OrderListViewItemInter mOrderListViewItemInter;

	public void setOrderListViewItemInter(OrderListViewItemInter ItemInter) {
		mOrderListViewItemInter = ItemInter;
	}

	/**
	 * 方法
	 * 
	 * @param context
	 * @param adapter
	 * @param removeView
	 */
	public void getOrderPageImp(Context context, QuickAdapter<OrderAllBean> adapter) {
		mContext = context;
		mAdapter = adapter;
	}

	/**
	 * 删除-取消订单
	 * 
	 * @param context
	 * @param msg
	 * @param status
	 * @param position
	 */
	public void AlertDialog(String msg, final int status, final OrderAllBean goods) {
		CustomDialog.Builder builder = new Builder(mContext);
		builder.setTitle("温馨提示");
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				if (status == 100001) {
					OrderCancel(goods);// 取消订单
				} else {
					OrderDelete(goods);
				}
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 取消订单
	 * 
	 * @param position
	 * @param order_id
	 */
	@Override
	public void OrderCancel(final OrderAllBean goods) {
		final LoadingDialog loDialog = new LoadingDialog(mContext);
		Api.getOrderCancel(goods.getOrder_id(), new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				ToastUtils.show(o);
				loDialog.dismiss();
				// 订单已经取消，表示取消成功
				goods.setOrder_status(100011);
				mOrderListViewItemInter.volleySuccess(goods);
			}

			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
				loDialog.dismiss();
			}
		});
	}

	/**
	 * 订单删除
	 * 
	 * @param position
	 * @param order_id
	 */
	@Override
	public void OrderDelete(final OrderAllBean goods) {
		// TriggerLoadingDialog.getInstance().show(getActivity());
		final LoadingDialog loDialog = new LoadingDialog(mContext);
		Api.getOrderDelete(goods.getOrder_id(), new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				try {
					loDialog.dismiss();
					ToastUtils.show(o);
					mAdapter.remove(goods);
					mAdapter.notifyDataSetInvalidated();
				} catch (Exception e) {
					e.getMessage();
				}
			}

			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
				loDialog.dismiss();
			}
		});

	}

	/**
	 * 提醒发货
	 * 
	 * @param order_id
	 */
	@Override
	public void OrderRemind(String order_id) {
		final LoadingDialog loDialog = new LoadingDialog(mContext);
		Api.getOrderRemind(order_id, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				ToastUtils.show(o);
				loDialog.dismiss();
			}

			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
				loDialog.dismiss();
			}
		});
	}

	/**
	 * 提醒接单
	 * 
	 * @param order_id
	 */
	@Override
	public void ReminderConnection(String order_id) {
		final LoadingDialog loDialog = new LoadingDialog(mContext);
		Api.getReminderConnection(order_id, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				ToastUtils.show(o);
				loDialog.dismiss();
			}

			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
				loDialog.dismiss();
			}
		});
	}

	/**
	 * 确认收货
	 * 
	 * @param order_id
	 * @param position
	 */
	@Override
	public void ConfirmReceipt(final OrderAllBean goods) {
		final LoadingDialog loDialog = new LoadingDialog(mContext);
		Api.getConfirmReceipt(goods.getOrder_id(), new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				loDialog.dismiss();
				ToastUtils.show(o);
				// 订单确认收货了-----
				goods.setOrder_status(100005);
				mOrderListViewItemInter.volleySuccess(goods);
			}

			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
				loDialog.dismiss();
			}
		});
	}
}
