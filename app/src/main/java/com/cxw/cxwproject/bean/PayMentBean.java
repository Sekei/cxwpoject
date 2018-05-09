package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class PayMentBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String charges;// 支付相关

	public String getCharges() {
		return charges;
	}

	public void setCharges(String charges) {
		this.charges = charges;
	}

}
