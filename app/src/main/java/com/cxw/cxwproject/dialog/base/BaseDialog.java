package com.cxw.cxwproject.dialog.base;

import com.cxw.cxwproject.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseDialog extends Dialog {

	private Window window;

	public BaseDialog(Context context) {
		super(context, R.style.dialog_default);
		setContentView(getLayoutId());
		setWindowAttribute();
		initView();
	}

	protected void setWindowAttribute() {
		window = getWindow();
		window.setWindowAnimations(R.style.dialog_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		window.getDecorView().setPadding(0, 0, 0, 0);
		wl.gravity = Gravity.BOTTOM;
		window.setAttributes(wl);
	}

	protected abstract int getLayoutId();

	protected void initView() {

	}

	public Object getTAG() {
		return this;
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void cancel() {
		super.cancel();
	}

}
