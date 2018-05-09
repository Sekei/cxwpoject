package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class CollectionBean implements Serializable{
	private static final long serialVersionUID = 1L;
	 private String nation_id;//":"12345",
	 private String name;//":"汉族",
	 private String avatar;//":"",
	public String getNation_id() {
		return nation_id;
	}
	public void setNation_id(String nation_id) {
		this.nation_id = nation_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	 
}
