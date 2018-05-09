package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String comment_id;// ": "58ac09961f253",
	private String nickname;// u9753\u4e3d\u7684\u5c0f\u767d\u6768\u793c\u8d5e",
	private String avatar;// uploads\/images\/face\/20170113\/b2567403e6b02799451a011a493fff90.png",
	private String product_id;// ": "5864a6e9f1f66",
	private String stars;// ": 5,
	private String content;// \u5f88\u6ee1\u610f\u7684\u4e00\u6b21\u8d2d\u7269\u3001\u4ea7\u54c1\u4e5f\u5f88\u597d",
	private String created;// ": "2017-02-21 17:34",
	private String level;// ": "\u597d\u8bc4"

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
