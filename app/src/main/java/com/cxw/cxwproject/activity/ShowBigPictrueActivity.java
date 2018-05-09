package com.cxw.cxwproject.activity;

import com.alibaba.fastjson.JSON;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.fragment.PictrueFragment;
import com.cxw.cxwproject.view.ChildViewPager.OnSingleTouchListener;
import com.cxw.cxwproject.view.HackyViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Window;

/**
 * 显示大图界面
 * 
 */
public class ShowBigPictrueActivity extends FragmentActivity {
	private HackyViewPager viewPager;
	/** 得到上一个界面点击图片的位置 */
	private int position = 0;
	private String[] strImg;
	OnSingleTouchListener mbtnListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_big_pictrue);
		position = getIntentObject(int.class);
		strImg = getIntentObject(String[].class, 1);
		initViewPager();
	}

	public <T> T getIntentObject(Class<T> cls) {
		return getIntentObject(cls, 0);
	}

	public <T> T getIntentObject(Class<T> cls, int positon) {
		String st = getIntent().getStringExtra("data_" + positon);
		if (st == null) {
			return null;
		}
		return JSON.parseObject(st, cls);
	}

	private void initViewPager() {
		viewPager = (HackyViewPager) findViewById(R.id.viewPager_show_bigPic);
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		// 跳转到第几个界面
		viewPager.setCurrentItem(position);
	}

	public void pageListener() {
		finish();
	}

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new PictrueFragment(position, strImg);
		}

		@Override
		public int getCount() {
			return strImg.length;
		}

	}
}
