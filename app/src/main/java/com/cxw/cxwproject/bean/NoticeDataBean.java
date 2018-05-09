package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class NoticeDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;// ":"尘晓屋红糖开始预售啦",
	private String introduction;// ":"尘晓屋红糖开始预售啦尘晓屋红糖开始预售啦尘晓屋红糖开始预售啦尘晓屋红糖开始预售啦",
	private String message_id;// ":"",
	private String send_time;// ":"2016-10-21 12:30",
	private int is_read;// ":true
	private String url;//url内容
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public int getIs_read() {
		return is_read;
	}

	public void setIs_read(int is_read) {
		this.is_read = is_read;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
