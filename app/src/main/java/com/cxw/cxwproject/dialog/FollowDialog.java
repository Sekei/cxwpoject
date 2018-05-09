package com.cxw.cxwproject.dialog;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.dialog.base.BaseDialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class FollowDialog extends BaseDialog {
	/**
	 * 初始化接口变量
	 */
	ICoallBack icallBack = null;

	/**
	 * 自定义控件的自定义事件
	 * 
	 * 接口类型
	 */
	public void setonClick(ICoallBack iBack) {
		icallBack = iBack;
	}

	public FollowDialog(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_follow;
	}

	@Override
	protected void initView() {
		// 取消关注
		TextView cancelattention_btn = (TextView) findViewById(R.id.cancelattention_btn);
		cancelattention_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 使用回调实现
				icallBack.onClickButton();
				dismiss();
			}
		});
		// 取消
		TextView cancel = (TextView) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}

	/**
	 * 取消关注接口
	 */
	public interface ICoallBack {
		public void onClickButton();
	}
}
