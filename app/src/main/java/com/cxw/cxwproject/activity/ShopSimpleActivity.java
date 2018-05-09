package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopSimpleActivity extends ActionBarActivity implements OnClickListener {

	@Override
	protected int getLayoutId() {
		return R.layout.activity_shopsimple;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("民族简介");
		// 简介内容
		TextView introduction = (TextView) findViewById(R.id.introduction);
		introduction.setText(getIntent().getStringExtra("introduction"));
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}
}
