package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class OrderAllBean implements Serializable {
	DecimalFormat df = new DecimalFormat("0.00");// 保留2位小数
	private static final long serialVersionUID = 1L;
	private String order_id;// ":"",
	private String order_no;// ":"2332323232",
	private String product_count;// ":100,
	private String price;// ":10000,
	private String service_price;// ":0,
	private int order_status;// ":0,
	private String time;// ":"2016年13月31日 12:67",
	private List<ProductsBean> products;// ":[
	
	

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getProduct_count() {
		return product_count;
	}

	public void setProduct_count(String product_count) {
		this.product_count = product_count;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getService_price() {
		return service_price;
	}

	public void setService_price(String service_price) {
		this.service_price = service_price;
	}

	public int getOrder_status() {
		return order_status;
	}

	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<ProductsBean> getProducts() {
		return products;
	}

	public void setProducts(List<ProductsBean> products) {
		this.products = products;
	}

	public class ProductsBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private String product_id;// ":"",
		private String name;// ":"",
		private String price;// ":1000,
		private String introduction;// ":"",
		private String number;// ":10,
		private String image;// ":""
		private String discount;//现卖价
		private String attribute;
		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getIntroduction() {
			return introduction;
		}

		public void setIntroduction(String introduction) {
			this.introduction = introduction;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getDiscount() {
			return df.format(Double.parseDouble(discount));
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getAttribute() {
			return attribute;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}

	}
}
