package com.cxw.cxwproject.tool;

import com.cxw.cxwproject.R;

/**
 * 男、女、保密性别单例
 * 
 * @author Administrator
 *
 */
public class GenderTool {
	/* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
	private static GenderTool instance = null;

	/* 私有构造方法，防止被实例化 */
	private GenderTool() {
	}

	/* 1:懒汉式，静态工程方法，创建实例 */
	public static GenderTool getInstance() {
		if (instance == null) {
			instance = new GenderTool();
		}
		return instance;
	}

	public int GednerStr(String gedner) {
		int resid = R.drawable.mi_icon;
		if (gedner.equals("M") || gedner.equals("m")) {// 男
			resid = R.drawable.nan_icon;
		} else if (gedner.equals("F") || gedner.equals("f")) {//女
			resid = R.drawable.nv_icon;
		} else {
			resid = R.drawable.mi_icon;
		}
		return resid;
	}

}
