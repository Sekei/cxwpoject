package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class VersionUpdateBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String type;// ": "ios",
	private String version;// ": "6.0.2",
	private String url;// ": "http://baidu.com",
	private String content;// ": "版本更新内容 ",
	private String is_update;// ": 0
	private String build_version;// ":602

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIs_update() {
		return is_update;
	}

	public void setIs_update(String is_update) {
		this.is_update = is_update;
	}

	public String getBuild_version() {
		return build_version;
	}

	public void setBuild_version(String build_version) {
		this.build_version = build_version;
	}

}
