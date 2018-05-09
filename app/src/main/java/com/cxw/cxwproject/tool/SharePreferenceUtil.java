package com.cxw.cxwproject.tool;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.bean.UserBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/*
 * app数据存储
 */
public class SharePreferenceUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	@SuppressLint("CommitPrefEdits")
	public SharePreferenceUtil() {
		sp = MyApp.getApp().getSharedPreferences("DataStorage", Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// Cookie清除
	public void Delete() {
		editor.remove("cookie").commit();
	}

	// 新消息通知开关
	public void setIsNewsOff(boolean isNewsOff) {
		editor.putBoolean("isNewsOff", isNewsOff);
		editor.commit();
	}

	public boolean getisNewsOff() {
		return sp.getBoolean("isNewsOff", true);
	}

	// 签到提醒开关
	public void setSignRemind(boolean signRemind) {
		editor.putBoolean("signRemind", signRemind);
		editor.commit();
	}

	public boolean getsignRemind() {
		return sp.getBoolean("signRemind", false);
	}

	// 保存cookie
	public void setCookie(String cookie) {
		editor.putString("cookie", cookie);
		editor.commit();
	}

	public String getCookie() {
		return sp.getString("cookie", null);
	}

	// 保存会员信息
	public void setUser(String user) {
		editor.putString("userdata", user);
		editor.commit();
	}

	public String getUser() {
		return sp.getString("userdata", null);
	}

	// 保存会员名
	public void setMember(String member) {
		editor.putString("memberdata", member);
		editor.commit();
	}

	public String getMember() {
		return sp.getString("memberdata", null);
	}

	public void setPwd(String pwd) {
		editor.putString("pwddata", pwd);
		editor.commit();
	}

	public String getPwd() {
		return sp.getString("pwddata", null);
	}

	// 是否第一次运行本应用
	public void setIsFirst(int isFirst) {
		editor.putInt("isFirst", isFirst);
		editor.commit();
	}

	public int getisFirst() {
		return sp.getInt("isFirst", 0);
	}
}
