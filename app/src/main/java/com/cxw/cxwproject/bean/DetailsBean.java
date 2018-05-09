package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class DetailsBean implements Serializable {
	DecimalFormat df = new DecimalFormat("0.00");// 保留2位小数
	private static final long serialVersionUID = 1L;
	private String product_id;// ":"",
	private String name;// ":"尘晓屋凉茶吧",
	private String icon;// ":"礼",
	private String stock;// ":200,
	private String price;// ":39.9
	private String introduction;// ":"尘晓屋凉茶是将药性寒凉",
	private String home_images;// ":"http://cdn.me-to-me.com/FmPo_OYFHshcDQmjBdqgkazz9ziz?imageView2/2/w/800",
	private String[] images;// ":[
	private String url;// 访问链接
	private String attribute;
	// =============额外新增数量===============
	private int num = 1;// 商品购买数量
	//===========新增评价============
	private CommentBean comment;

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getPrice() {
		return df.format(Double.parseDouble(price));
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

	public String getHome_images() {
		return home_images;
	}

	public void setHome_images(String home_images) {
		this.home_images = home_images;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public CommentBean getComment() {
		return comment;
	}

	public void setComment(CommentBean comment) {
		this.comment = comment;
	}

}
