package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class BuyBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pay_money;// ":9900,
	private String order_id;// ":"123456"

	public String getPay_money() {
		return pay_money;
	}

	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

}
