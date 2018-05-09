package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class ShoppingCartBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String chart_id;// ":"5555555",
	private int number;// ":2,
	private String status;// ":100000
	private boolean isChoosed = false; // 商品是否在购物车中被选中
	private ProductBean product;// ":

	public String getChart_id() {
		return chart_id;
	}

	public void setChart_id(String chart_id) {
		this.chart_id = chart_id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ProductBean getProduct() {
		return product;
	}

	public void setProduct(ProductBean product) {
		this.product = product;
	}

	
	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}


	public class ProductBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private String product_id;// ":"",
		private String name;// ":"尘晓屋凉茶吧",
		private String attribute;// ":"精包装200g",
		private String introduction;// ":"尘晓屋凉茶是将药性寒凉",
		private String image;// ":"http://cdn.me-to-me.com/FmPo_OYFHshcDQmjBdqgkazz9ziz?imageView2/2/w/800",
		private int stock;// ":200,
		private float price;// ":39.9,
		private float discount;// ":39.9

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

		public String getAttribute() {
			return attribute;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}

		public String getIntroduction() {
			return introduction;
		}

		public void setIntroduction(String introduction) {
			this.introduction = introduction;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public int getStock() {
			return stock;
		}

		public void setStock(int stock) {
			this.stock = stock;
		}

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public float getDiscount() {
			return discount;
		}

		public void setDiscount(float discount) {
			this.discount = discount;
		}
	}
}
