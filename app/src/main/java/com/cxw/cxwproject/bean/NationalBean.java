package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class NationalBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;//":"汉族",
	private String introduction;//":"简介",
	private String avatar;//":"",
	private String house_theme_bg;//":"",
	private int is_follow;//":false,
	private String follow_count;//":200,
	private String culture_url;//":""
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getHouse_theme_bg() {
		return house_theme_bg;
	}
	public void setHouse_theme_bg(String house_theme_bg) {
		this.house_theme_bg = house_theme_bg;
	}

	public int getIs_follow() {
		return is_follow;
	}
	public void setIs_follow(int is_follow) {
		this.is_follow = is_follow;
	}
	public String getFollow_count() {
		return follow_count;
	}
	public void setFollow_count(String follow_count) {
		this.follow_count = follow_count;
	}
	public String getCulture_url() {
		return culture_url;
	}
	public void setCulture_url(String culture_url) {
		this.culture_url = culture_url;
	}
	
}
