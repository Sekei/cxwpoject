package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class DataBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nickname;//":"用户名",
	private String gender;//F--女--M--男----S---保密
	private String avatar;//:"url",
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
