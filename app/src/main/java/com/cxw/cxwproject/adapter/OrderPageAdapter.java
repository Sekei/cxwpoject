package com.cxw.cxwproject.adapter;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.PayBuyActivity;
import com.cxw.cxwproject.bean.BuyBean;
import com.cxw.cxwproject.bean.OrderAllBean;
import com.cxw.cxwproject.enums.OderStateEnums;
import com.cxw.cxwproject.imp.OrderPageImp;
import com.cxw.cxwproject.inter.OrderDetailsViewInter;
import com.cxw.cxwproject.inter.OrderPageInter;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class OrderPageAdapter extends QuickAdapter<OrderAllBean> {

	public OrderPageAdapter(Context context, int layoutResId) {
		super(context, layoutResId);
	}

	/**
	 * 使用接口，查看订单详情回调
	 */
	public OrderDetailsViewInter mOrderDetailsViewInter = null;

	public void getOrderDetailsViewInter(OrderDetailsViewInter mOrderDetailsViewInter) {
		this.mOrderDetailsViewInter = mOrderDetailsViewInter;
	}

	@Override
	protected void convert(final BaseAdapterHelper helper, final OrderAllBean goods) {
		// 商品列表
		if (goods.getProducts().size() != 0) {
			helper.setImageUrl(R.id.order_goodsimg, goods.getProducts().get(0).getImage());
			helper.setText(R.id.order_name, goods.getProducts().get(0).getName());
			helper.setText(R.id.order_introduction, goods.getProducts().get(0).getIntroduction());
			helper.setText(R.id.order_number, goods.getProducts().get(0).getNumber());
		} else {
			helper.setImageUrl(R.id.order_goodsimg, null);
			helper.setText(R.id.order_name, null);
			helper.setText(R.id.order_introduction, null);
			helper.setText(R.id.order_number, null);
		}
		helper.setText(R.id.order_no, "订单号：" + goods.getOrder_no());
		helper.setText(R.id.order_status, OderStateEnums.getWeek(goods.getOrder_status()));
		helper.setText(R.id.order_productcount, "共" + goods.getProduct_count() + "个商品，总价￥");
		helper.setText(R.id.order_price, goods.getPrice());
		// 取消订单
		helper.setVisible(R.id.cancel_delete_btn, OderStateEnums.getVisible(goods.getOrder_status()));
		helper.setOnClickListener(R.id.cancel_delete_btn, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 通过接口来调用
				OrderPageInter orderPageInter = new OrderPageImp();
				((OrderPageImp) orderPageInter).AlertDialog("您的确定要取消当前订单？", goods.getOrder_status(), goods);
			}
		});
		// 全部订单----一系列的名称---自动匹配
		helper.setText(R.id.payment_btn, OderStateEnums.getName(goods.getOrder_status()));
		helper.setOnClickListener(R.id.payment_btn, new OnClickListener() {
			@Override
			public void onClick(View v) {
				int order_status = goods.getOrder_status();
				String orderId = goods.getOrder_id();
				OrderPageInter orderPageInter = new OrderPageImp();
				if (order_status == 100003) {// 提醒发货
					orderPageInter.OrderRemind(orderId);
				} else if (order_status == 100001) {// 立即支付
					Intent mIntent = new Intent(context, PayBuyActivity.class);
					BuyBean mdata = new BuyBean();
					mdata.setOrder_id(goods.getOrder_id());
					mdata.setPay_money(goods.getPrice());
					mIntent.putExtra("buydata", mdata);
					context.startActivity(mIntent);
				} else if (order_status == 100002) {// 提醒接单
					orderPageInter.ReminderConnection(orderId);
				} else if (order_status == 100004) {// 确认收货
					orderPageInter.ConfirmReceipt(goods);
				} else if (order_status == 100005) {// 评价
					mOrderDetailsViewInter.OrderEvaluateViewOnClick(goods);
				} else {// 订单删除----暂无评价
					((OrderPageImp) orderPageInter).AlertDialog("您的确定要删除当前订单？", goods.getOrder_status(), goods);
				}
			}
		});
		// item点击事件
		helper.setOnClickListener(R.id.order_item, new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOrderDetailsViewInter.OrderDetailsViewOnClick(goods);
			}
		});
	}
}
