package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class RewardBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String days;// ":20,
	private int is_receive;// ":0,
	private String score;// ":100
	private String img_url;

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public int getIs_receive() {
		return is_receive;
	}

	public void setIs_receive(int is_receive) {
		this.is_receive = is_receive;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

}
