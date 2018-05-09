package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarFragmentActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.fragment.LoginFragment_Pager1;
import com.cxw.cxwproject.fragment.LoginFragment_Pager2;
import com.cxw.cxwproject.widget.PagerSlidingTabStrip;
import com.cxw.cxwproject.widget.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends ActionBarFragmentActivity implements OnClickListener {

	private String[] titles = { "快捷登录", "普通登录" };
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;

	private LoginFragment_Pager1 mpager1;
	private LoginFragment_Pager2 mpager2;

	public static LoginActivity instance = null;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
	}


	@Override
	protected void initView() {
		instance = this;
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("登录");
		// 右侧注册按钮
		TextView right_btn = (TextView) findViewById(R.id.right_btn);
		right_btn.setVisibility(View.VISIBLE);
		right_btn.setText("注册");
		right_btn.setOnClickListener(this);
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
		tabs.setViewPager(pager);
	}

	public class MyAdapter extends FragmentStatePagerAdapter {
		String[] _titles;

		public MyAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			_titles = titles;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return _titles[position];
		}

		@Override
		public int getCount() {
			return _titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (mpager1 == null) {
					mpager1 = new LoginFragment_Pager1();
				}
				return mpager1;
			case 1:
				if (mpager2 == null) {
					mpager2 = new LoginFragment_Pager2();
				}
				return mpager2;
			default:
				return null;
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (getIntentObject(int.class) == 0) {// 表示退出登录、直接返回首页
			Intent mIntent = new Intent(this, HomeActivity.class);
			mIntent.putExtra("NewIntent", "1000100");
			startActivity(mIntent);
		}
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:// 返回
			onBackPressed();
			break;
		case R.id.right_btn:
			startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
			break;
		default:
			break;
		}

	}
}
