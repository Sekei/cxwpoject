package com.cxw.cxwproject.widget;

import com.cxw.cxwproject.MyApp;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 控件适配工具类
 * 
 * @author Administrator
 *
 */
public class ViewAdaptation {

	private static ViewAdaptation instance = new ViewAdaptation();

	// 这里提供了一个供外部访问本class的静态方法，可以直接访问
	public static ViewAdaptation getInstance() {
		return instance;
	}

	// 线性布局
	public View AdaptationLinearLayout(View view, int width, int height) {
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) view.getLayoutParams();
		params.height = height;
		params.width = width;
		view.setLayoutParams(params);
		return view;
	}

	// 相对布局
	public View AdaptationRelativeLayout(View view, int width, int height) {
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) view.getLayoutParams();
		params.height = height;
		params.width = width;
		view.setLayoutParams(params);
		return view;
	}
}
