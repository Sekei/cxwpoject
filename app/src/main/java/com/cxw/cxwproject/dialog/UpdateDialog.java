package com.cxw.cxwproject.dialog;

import com.cxw.cxwproject.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * 版本更新提示对话框
 */
public class UpdateDialog extends Dialog {

	public UpdateDialog(Context context) {
		super(context);
	}

	public UpdateDialog(Context context, int theme) {
		super(context, theme);
	}
	
	/**
	 * 自定义控件
	 * 
	 * @author Administrator
	 *
	 */
	public static class Builder {
		private Context context; // 上下文对象
		private String title; // 对话框标题
		private String message; // 对话框内容
		private String confirm_btnText; // 按钮名称“确定”
		private String cancel_btnText; // 按钮名称“取消”
		private View contentView; // 对话框中间加载的其他布局界面

		/* 按钮坚挺事件 */
		private DialogInterface.OnClickListener confirm_btnClickListener;
		private DialogInterface.OnClickListener cancel_btnClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/* 设置对话框信息 */
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * 设置对话框界面
		 * 
		 * @param v
		 *            View
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(int confirm_btnText, DialogInterface.OnClickListener listener) {
			this.confirm_btnText = (String) context.getText(confirm_btnText);
			this.confirm_btnClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String confirm_btnText, DialogInterface.OnClickListener listener) {
			this.confirm_btnText = confirm_btnText;
			this.confirm_btnClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int cancel_btnText, DialogInterface.OnClickListener listener) {
			this.cancel_btnText = (String) context.getText(cancel_btnText);
			this.cancel_btnClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String cancel_btnText, DialogInterface.OnClickListener listener) {
			this.cancel_btnText = cancel_btnText;
			this.cancel_btnClickListener = listener;
			return this;
		}

		@SuppressLint("WrongViewCast")
		public UpdateDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final UpdateDialog dialog = new UpdateDialog(context, R.style.update_style);
			View layout = inflater.inflate(R.layout.update_alertdialog, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			((TextView) layout.findViewById(R.id.title)).setText(title);
			if (confirm_btnText != null) {
				((Button) layout.findViewById(R.id.confirm_btn)).setText(confirm_btnText);
				if (confirm_btnClickListener != null) {
					((Button) layout.findViewById(R.id.confirm_btn)).setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							confirm_btnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			} else {
				layout.findViewById(R.id.confirm_btn).setVisibility(View.GONE);
			}
			if (cancel_btnText != null) {
				((Button) layout.findViewById(R.id.cancel_btn)).setText(cancel_btnText);
				if (cancel_btnClickListener != null) {
					((Button) layout.findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							cancel_btnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
			}
			// set the content message
			if (message != null) {
				TextView tv_content = (TextView) layout.findViewById(R.id.message);
				tv_content.setMovementMethod(new ScrollingMovementMethod());
				tv_content.setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.message)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.message)).addView(contentView,
						new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}
}
