package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class AdvertisementBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int duration;// ": 5, //停留时间
	private boolean is_click;// ": 1, //是否可以点击
	private String url; //跳转链接
	private String img_url;//图片路径
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isIs_click() {
		return is_click;
	}

	public void setIs_click(boolean is_click) {
		this.is_click = is_click;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

}
