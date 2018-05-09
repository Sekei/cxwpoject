package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class HelpBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;//": 1,
	private String question;//": "忘记密码怎么办？",//问题
	private String answer;//": "可以用手机号码找回。。。",//答案
	private String created;//": "2017-05-18 09:32"
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
}
