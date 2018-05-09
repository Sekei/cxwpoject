package com.cxw.cxwproject.activity;

import java.util.ArrayList;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.util.ViewPageImage;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class NoviceActivity extends ActionBarActivity implements OnClickListener {
	// 游标是圆形还是长条，要是设置为0是长条，要是1就是圆形 默认是圆形
	public int stype = 1;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_novice;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_WHITE;
	}

	@Override
	protected void initView() {
		TextView back_btn = (TextView) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);
		ArrayList<Object> mImageUrl = new ArrayList<Object>();
		mImageUrl.add(R.drawable.guide_01);
		mImageUrl.add(R.drawable.guide_02);
		mImageUrl.add(R.drawable.guide_03);
		ViewPageImage viewPager = (ViewPageImage) findViewById(R.id.guide_viewpager);
		viewPager.setImageResources(mImageUrl, stype);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;
		}

	}
}
