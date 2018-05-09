package com.cxw.cxwproject.tool;

import android.view.View;
import android.widget.CheckBox;

public class ControlVisibleGoone {
	/**
	 * 这个类不能实例化
	 */
	private ControlVisibleGoone() {
	}

	/**
	 * 控件显示隐藏控制
	 * 
	 * @param visibileity
	 * @param view
	 */
	public static void setVisibility(int visibileity, View... view) {
		for (int i = 0; i < view.length; i++) {
			view[i].setVisibility(visibileity);
		}
	}

	/**
	 * 选择框是否选中
	 * 
	 * @param checked
	 * @param view
	 */
	public static void setChecked(boolean checked, CheckBox... view) {
		for (int i = 0; i < view.length; i++) {
			view[i].setChecked(checked);
		}
	}
}
