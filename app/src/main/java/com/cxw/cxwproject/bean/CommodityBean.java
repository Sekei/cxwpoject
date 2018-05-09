package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

import android.text.TextUtils;

public class CommodityBean implements Serializable {
	DecimalFormat df = new DecimalFormat("0.00");// 保留2位小数
	private static final long serialVersionUID = 1L;
	private String product_id;// ":"",
	private String name;// ":"尘晓屋凉茶吧",
	private String attribute;// ":"精包装200g",
	private String icon;// ":"礼",
	private String introduction;// ":"尘晓屋凉茶是将药性寒凉",
	private String image;// ":"http://cdn.me-to-me.com/FmPo_OYFHshcDQmjBdqgkazz9ziz?imageView2/2/w/800",
	private String stock;// ":200,
	private String price;// ":39.9
	//=======热销相关=======
	private String nation_name;//民族
	
	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getName() {
		if (TextUtils.isEmpty(getAttribute())) {
			return name;
		}
		return name + "\t" + getAttribute();
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getNation_name() {
		return nation_name;
	}

	public void setNation_name(String nation_name) {
		this.nation_name = nation_name;
	}

}
