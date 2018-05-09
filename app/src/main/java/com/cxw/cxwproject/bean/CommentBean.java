package com.cxw.cxwproject.bean;

import java.io.Serializable;
import java.util.List;

public class CommentBean implements Serializable{
	private static final long serialVersionUID = 1L;
	 private String picture_cnt;//": 0,
	 private String all_cnt;//": 2,
	 private String good_cnt;//": 2,
	 private String normal_cnt;//": 0,
	 private String bad_cnt;//": 0,
	 private String good_rate;//": "100%",
	 private String showpic_cnt;//": 0,
	 private List<CommentsBean> comments;//":
	 
	 
	 
	 public String getPicture_cnt() {
		return picture_cnt;
	}



	public void setPicture_cnt(String picture_cnt) {
		this.picture_cnt = picture_cnt;
	}



	public String getAll_cnt() {
		return all_cnt;
	}



	public void setAll_cnt(String all_cnt) {
		this.all_cnt = all_cnt;
	}



	public String getGood_cnt() {
		return good_cnt;
	}



	public void setGood_cnt(String good_cnt) {
		this.good_cnt = good_cnt;
	}



	public String getNormal_cnt() {
		return normal_cnt;
	}



	public void setNormal_cnt(String normal_cnt) {
		this.normal_cnt = normal_cnt;
	}



	public String getBad_cnt() {
		return bad_cnt;
	}



	public void setBad_cnt(String bad_cnt) {
		this.bad_cnt = bad_cnt;
	}



	public String getGood_rate() {
		return good_rate;
	}



	public void setGood_rate(String good_rate) {
		this.good_rate = good_rate;
	}



	public String getShowpic_cnt() {
		return showpic_cnt;
	}



	public void setShowpic_cnt(String showpic_cnt) {
		this.showpic_cnt = showpic_cnt;
	}



	public List<CommentsBean> getComments() {
		return comments;
	}



	public void setComments(List<CommentsBean> comments) {
		this.comments = comments;
	}
}
