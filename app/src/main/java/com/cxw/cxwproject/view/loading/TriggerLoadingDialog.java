package com.cxw.cxwproject.view.loading;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 提交按钮请求数据loading
 */
public class TriggerLoadingDialog {

	private Dialog mDialog;
	private View mDialogContentView;

	public static TriggerLoadingDialog getInstance() {
		return new TriggerLoadingDialog();
	}

	// public void setLoadingText(CharSequence charSequence) {
	// mLoadingView.setLoadingText(charSequence);
	// }
	/**
	 * 箭头旋转加载进度
	 * 
	 * @param context
	 */
	public void show(Context context) {
		mDialog = new Dialog(context, R.style.custom_dialog);
		mDialogContentView = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);
		mDialog.setContentView(mDialogContentView);
		// 获得当前窗体
		Window window = mDialog.getWindow();
		// 重新设置
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.TOP);
		lp.y = MyApp.getApp().dp2px(65); // 新位置Y坐标相对上边的偏移65
		window.setAttributes(lp);
		mDialog.show();
	}

	/**
	 * 圆圈放大、缩小进度
	 * 
	 * @param context
	 */
	public void show2(Context context) {
		mDialog = new Dialog(context, R.style.custom_dialog);
		mDialogContentView = LayoutInflater.from(context).inflate(R.layout.monindicator_dialog, null);
		mDialog.setContentView(mDialogContentView);
		// 获得当前窗体
		Window window = mDialog.getWindow();
		// 重新设置
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.TOP);
		lp.y = MyApp.getApp().dp2px(65); // 新位置Y坐标相对上边的偏移65
		window.setAttributes(lp);
		mDialog.show();
	}

	/**
	 * 左右摆钟效果
	 * @param context
	 */
	public void show3(Context context){
		mDialog = new Dialog(context, R.style.custom_dialog);
		mDialogContentView = LayoutInflater.from(context).inflate(R.layout.swingindicator_dialog, null);
		mDialog.setContentView(mDialogContentView);
		// 获得当前窗体
		Window window = mDialog.getWindow();
		// 重新设置
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.TOP);
		lp.y = MyApp.getApp().dp2px(65); // 新位置Y坐标相对上边的偏移65
		window.setAttributes(lp);
		mDialog.show();
	}

	public void dismiss() {
		if (mDialog != null) {
			try {
				mDialog.dismiss();
			} catch (Exception e) {
				e.fillInStackTrace();
			}

		}
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}
}
