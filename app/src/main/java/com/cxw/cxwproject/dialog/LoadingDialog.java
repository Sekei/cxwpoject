package com.cxw.cxwproject.dialog;

import com.cxw.cxwproject.R;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
	ImageView loading_img;

	public LoadingDialog(Context context, String msg) {
		super(context, R.style.dialog_light);
		setContentView(R.layout.dialog_loading);
		// 动画
		loading_img = (ImageView) findViewById(R.id.loading_img);
		Animation myAlphaAnimation = AnimationUtils.loadAnimation(context, R.anim.loading);
		LinearInterpolator lin = new LinearInterpolator();
		myAlphaAnimation.setInterpolator(lin);
		loading_img.startAnimation(myAlphaAnimation);
		// 文字提示
		TextView textview = (TextView) findViewById(R.id.msg);
		textview.setText(msg);
		//setCancelable(false);
		// 让他已加载出来就显示
		show();
	}

	public LoadingDialog(Context context) {
		this(context, "Loading...");
	}

	@Override
	public void dismiss() {
		super.dismiss();
		loading_img.clearAnimation();
	}
}
