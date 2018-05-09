package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.kyleduo.switchbutton.SwitchButton;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsSetupActivity extends ActionBarActivity {
	private TextView title;
	private ImageView back;
	SharePreferenceUtil sputil;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_newssetup;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		title = (TextView) findViewById(R.id.title);
		title.setText("新消息通知");
		back = (ImageView) findViewById(R.id.back); // 返回
		// 开关监听
		SwitchButton newssetup_switch = (SwitchButton) findViewById(R.id.newssetup_switch);
		// 赋值，查看当前状态
		sputil = new SharePreferenceUtil();
		newssetup_switch.setChecked(sputil.getisNewsOff());
		newssetup_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sputil.setIsNewsOff(isChecked);
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
