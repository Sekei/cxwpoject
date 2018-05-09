package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.BuyBean;
import com.cxw.cxwproject.bean.OrderAllBean;
import com.cxw.cxwproject.bean.OrderDetailsBean;
import com.cxw.cxwproject.dialog.CustomDialog;
import com.cxw.cxwproject.dialog.CustomDialog.Builder;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.enums.OderStateEnums;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.cxw.cxwproject.imp.OrderPageImp;
import com.cxw.cxwproject.inter.OrderPageInter;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.view.StateLineView;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.ToastUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderDetailsActivity extends ActionBarActivity implements OnClickListener, RequestListener {
	private MVCViewHelper<OrderDetailsBean> mvcHelper;
	private OrderAllBean mOrderData;
	private String orderId;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_orderdetails;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		mOrderData = getIntentObject(OrderAllBean.class);
		orderId = mOrderData.getOrder_id();
		// 实例化控件
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("订单详情");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		String[] strContent = { "*支付完成后,我们将尽快为您发货!", "*我们将尽快为您发货!", "*我们将尽快为您发货!", " *商品正在路上,等待您的收货!", "*可以对本次购买进行评价了!",
				"*本次交易结束,谢谢您的购买!" };
		TextView oderstateview = (TextView) findViewById(R.id.oderstateview);
		StateLineView stateline = (StateLineView) findViewById(R.id.stateline);
		oderstateview.setText(strContent[OderStateEnums.getStep(mOrderData.getOrder_status()) - 1]);
		stateline.setNewestPointPosition(OderStateEnums.getStep(mOrderData.getOrder_status()));
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		// 网络请求
		mvcHelper = MVCHelper.createViewHelper(null, emptyLayout);
		mvcHelper.setRequestListener(this);
		mvcHelper.refresh();
	}

	/**
	 * 赋值相关
	 */
	private void initData(OrderDetailsBean mdata) {
		TextView name = (TextView) findViewById(R.id.name);
		name.setText("联系方式：" + mdata.getAddress().getName() + "\t\t" + mdata.getAddress().getPhone());
		TextView address = (TextView) findViewById(R.id.address);
		address.setText(mdata.getAddress().getArea() + "\n" + mdata.getAddress().getAddress());
		TextView remark = (TextView) findViewById(R.id.remark);
		if (!mdata.getRemark().equals("") && mdata.getRemark() != null) {
			remark.setVisibility(View.VISIBLE);
			remark.setText("留言备注：" + mdata.getRemark());
		}
		// =============传值赋值============================
		// 订单信息
		TextView order_no = (TextView) findViewById(R.id.order_no);
		order_no.setText("订单编号：" + mOrderData.getOrder_no());
		TextView order_time = (TextView) findViewById(R.id.order_time);
		order_time.setText("下单时间：" + mOrderData.getTime());
		TextView order_status = (TextView) findViewById(R.id.order_status);
		order_status.setText(OderStateEnums.getWeek(mOrderData.getOrder_status()));
		ImageView order_goodsimg = (ImageView) findViewById(R.id.order_goodsimg);
		// 商品信息
		if (mOrderData.getProducts().size() != 0) {
			ImageDisplay.getSingleton().ImageLoader(order_goodsimg, mOrderData.getProducts().get(0).getImage(), false);
			TextView order_name = (TextView) findViewById(R.id.order_name);
			order_name.setText(
					mOrderData.getProducts().get(0).getName() +"　"+ mOrderData.getProducts().get(0).getAttribute());
			TextView order_introduction = (TextView) findViewById(R.id.order_introduction);
			order_introduction.setText("售价：" + mOrderData.getProducts().get(0).getDiscount() + "元　X"
					+ mOrderData.getProducts().get(0).getNumber());
			TextView order_price = (TextView) findViewById(R.id.order_price);
			order_price.setText(mOrderData.getProducts().get(0).getDiscount() + "元");
		}
		// 实付款
		TextView sumprice = (TextView) findViewById(R.id.sumprice);
		sumprice.setText("￥" + mOrderData.getPrice());
		// 底部按钮控制显示----操作---
		TextView cancel_delete_btn = (TextView) findViewById(R.id.cancel_delete_btn);
		cancel_delete_btn.setOnClickListener(this);
		cancel_delete_btn.setVisibility(
				OderStateEnums.getVisible(mOrderData.getOrder_status()) == true ? View.VISIBLE : View.GONE);
		TextView payment_btn = (TextView) findViewById(R.id.payment_btn);
		payment_btn.setOnClickListener(this);
		payment_btn.setText(OderStateEnums.getName(mOrderData.getOrder_status()));
	}

	/**
	 * 底部操作
	 */
	private void initControl() {
		int order_status = mOrderData.getOrder_status();
		OrderPageInter orderpageinter = new OrderPageImp();
		if (order_status == 100003) {// 提醒发货
			orderpageinter.OrderRemind(orderId);
		} else if (order_status == 100001) {// 立即支付
			Intent mIntent = new Intent(this, PayBuyActivity.class);
			BuyBean mdata = new BuyBean();
			mdata.setOrder_id(orderId);
			mdata.setPay_money(mOrderData.getPrice());
			mIntent.putExtra("buydata", mdata);
			startActivity(mIntent);
		} else if (order_status == 100002) {// 提醒接单
			orderpageinter.ReminderConnection(orderId);
		} else if (order_status == 100004) {// 确认收货
			ConfirmReceipt(orderId);
		} else if (order_status == 100005) {// 评价
			startActivityForResult(EvaluateActivity.class, 0, mOrderData);
		} else {// 订单删除----暂无评价
			AlertDialog("您的确定要删除当前订单？");
		}
	}

	@Override
	public void requestData(int page) {
		Api.getOrderDetails(orderId, new OnResponse<OrderDetailsBean>() {
			@Override
			public void onResponse(OrderDetailsBean o) {
				mvcHelper.executeOnLoadDataSuccess(o);
				initData(o);
			}

			@Override
			public void onErrorResponse(String error) {
				mvcHelper.executeOnLoadDataFail(error);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.cancel_delete_btn:
			AlertDialog("您的确定要取消当前订单？");
			break;
		case R.id.payment_btn:
			initControl();
			break;
		default:
			break;
		}

	}

	/**
	 * 删除-取消订单
	 */
	private void AlertDialog(final String msg) {
		CustomDialog.Builder builder = new Builder(this);
		builder.setTitle("温馨提示");
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				if (mOrderData.getOrder_status() == 100001) {
					OrderCancel();// 取消订单
				} else {
					OrderDelete();
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
	 */
	private void OrderCancel() {
		final LoadingDialog loDialog = new LoadingDialog(this);
		Api.getOrderCancel(orderId, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				ToastUtils.show(o);
				loDialog.dismiss();
				setResult(101, getIntent());
				finish();
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
	 */
	private void OrderDelete() {
		// TriggerLoadingDialog.getInstance().show(getActivity());
		final LoadingDialog loDialog = new LoadingDialog(this);
		Api.getOrderDelete(orderId, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				loDialog.dismiss();
				ToastUtils.show(o);
				setResult(102, getIntent());
				finish();
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
	 */
	private void ConfirmReceipt(String order_id) {
		final LoadingDialog loDialog = new LoadingDialog(this);
		Api.getConfirmReceipt(order_id, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				loDialog.dismiss();
				ToastUtils.show(o);
				setResult(103, getIntent());
				finish();
			}

			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
				loDialog.dismiss();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == 104) {// 已经评价
				setResult(104, getIntent());
				finish();
			}
		}
	}
}
