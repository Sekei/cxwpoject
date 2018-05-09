package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class NewsBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String img_url;// ":"http://imgsize.ph.126.net/?imgurl=http://cms-bucket.nosdn.127.net/71db16330cd94b5e9635d1525091d91c20161130104709.jpeg_140x88x1x85.jpg",
	private String title;// ":"陆生赴台减少 国台办：教育“台独”恶化交流环境",
	private String time;// ":"2016-11-30 11:00"
	private String news_id;// ":"",
	private String url;// ":""

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNews_id() {
		return news_id;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
