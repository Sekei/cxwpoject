package com.cxw.cxwproject.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.cxw.cxwproject.tool.SharePreferenceUtil;

public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static UserBean _instance;
	private String avatar;// ":用户头像
	private String nickname;// ":用户昵称
	private String gender;// ": 性别
	private int is_newmsg;// ":是否有新消息(0否 1是)
	private boolean password_is_enable;// ":是否启用密码
	// 用户积分相关
	private IntegralBean integral;

	// 读取
	public static UserBean defaultShop() {
		if (_instance == null) {
			String data = new SharePreferenceUtil().getUser();
			if (data != null) {
				_instance = JSON.parseObject(data, UserBean.class);
			} else {
				_instance = new UserBean();
			}
		}
		return _instance;
	}

	// 写入
	public static void save(UserBean shop) {
		_instance = shop;
		if (shop == null) {
			new SharePreferenceUtil().Delete();
			return;
		}
		new SharePreferenceUtil().setUser(JSON.toJSONString(shop));
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		// 当昵称为空时，取手机号码
		if (nickname.equals("")) {
			nickname = new SharePreferenceUtil().getMember();
		}
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

	public IntegralBean getIntegral() {
		return integral;
	}

	public void setIntegral(IntegralBean integral) {
		this.integral = integral;
	}


	public int getIs_newmsg() {
		return is_newmsg;
	}

	public void setIs_newmsg(int is_newmsg) {
		this.is_newmsg = is_newmsg;
	}

	public boolean isPassword_is_enable() {
		return password_is_enable;
	}

	public void setPassword_is_enable(boolean password_is_enable) {
		this.password_is_enable = password_is_enable;
	}

	@Override
	public String toString() {
		return "UserBean [avatar=" + avatar + ", nickname=" + nickname + ", gender=" + gender + ", integral=" + integral
				+ ", is_newmsg=" + is_newmsg + ", password_is_enable=" + password_is_enable + "]";
	}

	/**
	 * 积分相关
	 */
	public class IntegralBean implements Serializable {
		private static final long serialVersionUID = 1L;
		private boolean is_attend_wechat;// ":是否关注微信
		private boolean is_attend_weibo;// ":是否关注新浪
		private boolean is_share;// ":每日分享
		private String integral;// 用户积分
		private String continue_signin_days;// 连续签到天数
		private String score;// 下次签到获取的积分
		private String next_reward_days;// 下次获取奖品的时间
		private boolean is_signin;// 今日是否已签到

		public boolean isIs_attend_wechat() {
			return is_attend_wechat;
		}

		public void setIs_attend_wechat(boolean is_attend_wechat) {
			this.is_attend_wechat = is_attend_wechat;
		}

		public boolean isIs_attend_weibo() {
			return is_attend_weibo;
		}

		public void setIs_attend_weibo(boolean is_attend_weibo) {
			this.is_attend_weibo = is_attend_weibo;
		}

		public boolean isIs_share() {
			return is_share;
		}

		public void setIs_share(boolean is_share) {
			this.is_share = is_share;
		}

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

		public boolean isIs_signin() {
			return is_signin;
		}

		public void setIs_signin(boolean is_signin) {
			this.is_signin = is_signin;
		}

	}

	/**
	 * 判断男、女、保密相关
	 * 
	 * @return
	 */
	public static String Gender() {
		String gender_str = UserBean.defaultShop().getGender();
		if (gender_str.equals("m") || gender_str.equals("M")) {
			gender_str = "男";
		} else if (gender_str.equals("f") || gender_str.equals("F")) {
			gender_str = "女";
		} else {
			gender_str = "保密";
		}
		return gender_str;
	}
}
