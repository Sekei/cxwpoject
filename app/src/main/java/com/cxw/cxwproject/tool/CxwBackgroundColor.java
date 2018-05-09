package com.cxw.cxwproject.tool;

import com.cxw.cxwproject.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class CxwBackgroundColor {
	/**
	 * 这个类不能实例化
	 */
	private CxwBackgroundColor() {
	}

	/**
	 * 1.主题色
	 */
	static int[] mcolor = { R.color.uli_bg, R.color.uli_FF0000 };

	/**
	 * 控件背景颜色修改
	 * 
	 * @param mContext
	 * @param view
	 * @param color
	 */
	@SuppressWarnings("deprecation")
	public static void setBackgroundColor(Context mContext, View view, int subscript) {
		view.setBackgroundColor(mContext.getResources().getColor(mcolor[subscript]));
	}
}
