package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class GoodsBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String product_id;// ":"",
	private String name;// ":"尘晓屋凉茶吧",
	private String stock;// ":200,
	private String price;// ":39.9
	private String introduction;// ":"尘晓屋凉茶是将药性寒凉",
	private String attribute;//
	private String images;// 图片
	// ============来源判断===========、
	private String source;// 0表示直接购买，1表示购物车购买

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

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
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

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
}
