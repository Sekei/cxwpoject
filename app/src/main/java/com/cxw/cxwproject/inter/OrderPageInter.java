package com.cxw.cxwproject.inter;

import com.cxw.cxwproject.bean.OrderAllBean;

/**
 * 订单管理接口类
 * 
 * @author Administrator
 *
 */
public interface OrderPageInter {
	/**
	 * 取消订单
	 * 
	 * @param position
	 * @param order_id
	 */
	public void OrderCancel(OrderAllBean goods);

	/**
	 * 订单删除
	 * 
	 * @param position
	 * @param order_id
	 */
	public void OrderDelete(OrderAllBean goods);

	/**
	 * 提醒发货
	 * 
	 * @param order_id
	 */
	public void OrderRemind(String order_id);

	/**
	 * 提醒接单
	 * 
	 * @param order_id
	 */
	public void ReminderConnection(String order_id);

	/**
	 * 确认收货
	 * 
	 * @param order_id
	 * @param position
	 */
	public void ConfirmReceipt(OrderAllBean goods);
}
