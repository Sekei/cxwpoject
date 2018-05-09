package com.cxw.cxwproject.dialog;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.HomeActivity;
import com.cxw.cxwproject.activity.LoginActivity;
import com.cxw.cxwproject.activity.MechatActivity;
import com.cxw.cxwproject.activity.NoticeActivity;
import com.cxw.cxwproject.tool.SharePreferenceUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MoreDialog extends Dialog {
	private Window window;
	private Context mContext;

	public MoreDialog(Context context) {
		super(context, R.style.exhibit_Dialog);
		this.mContext = context;
		setContentView(R.layout.exhibit_hotspot_dialog);
		initView();
	}

	private void initView() {
		window = getWindow();
		window.setGravity(Gravity.TOP | Gravity.RIGHT);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.y = MyApp.getApp().dp2px(41);
		window.setAttributes(lp);
		window.setLayout(MyApp.getApp().getDisplayHightAndWightPx()[1] / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
		// 消息中心
		LinearLayout refresh_button = (LinearLayout) findViewById(R.id.refresh_button);
		refresh_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (new SharePreferenceUtil().getCookie() != null) {
					StartActivity(NoticeActivity.class);
				} else {
					StartActivity(LoginActivity.class);
				}
			}
		});
		// 返回首页
		LinearLayout details_button = (LinearLayout) findViewById(R.id.details_button);
		details_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent mIntent = new Intent(mContext, HomeActivity.class);
				mIntent.putExtra("NewIntent", "1000100");
				mContext.startActivity(mIntent);
				((Activity) mContext).finish();
			}
		});
		// 帮助中心
		LinearLayout help_btn = (LinearLayout) findViewById(R.id.help_btn);
		help_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				StartActivity(MechatActivity.class);
			}
		});
	}

	/*
	 * 页面调整方法
	 */
	private void StartActivity(Class<?> mclass) {
		dismiss();
		mContext.startActivity(new Intent(mContext, mclass));
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
