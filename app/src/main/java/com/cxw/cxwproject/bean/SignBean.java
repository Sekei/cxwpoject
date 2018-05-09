package com.cxw.cxwproject.bean;

import java.io.Serializable;

public class SignBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String integral;// ":200,
	private String continue_signin_days;// ":20,
	private String score;// ":10,
	private String next_reward_days;// ":11
	private int is_signin;// ":true
	// 签到特有
	private String tomorrow_singin_integral;// 明日签到积分

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getContinue_signin_days() {
		return continue_signin_days;
	}

	public void setContinue_signin_days(String continue_signin_days) {
		this.continue_signin_days = continue_signin_days;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getNext_reward_days() {
		return next_reward_days;
	}

	public void setNext_reward_days(String next_reward_days) {
		this.next_reward_days = next_reward_days;
	}

	public int getIs_signin() {
		return is_signin;
	}

	public void setIs_signin(int is_signin) {
		this.is_signin = is_signin;
	}

	public String getTomorrow_singin_integral() {
		return tomorrow_singin_integral;
	}

	public void setTomorrow_singin_integral(String tomorrow_singin_integral) {
		this.tomorrow_singin_integral = tomorrow_singin_integral;
	}

}
