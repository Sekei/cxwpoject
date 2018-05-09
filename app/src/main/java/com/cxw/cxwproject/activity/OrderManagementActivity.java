package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarFragmentActivity;
import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.OrderlPageAdapter;
import com.cxw.cxwproject.widget.CustomViewPager;
import com.cxw.cxwproject.widget.PagerSlidingTabStrip;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderManagementActivity extends ActionBarFragmentActivity
		implements OnClickListener, OnPageChangeListener {

	PagerSlidingTabStrip tabs;
	CustomViewPager pager;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_ordermanage;
	}


	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("订单管理");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		pager = (CustomViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setOnPageChangeListener(this);
		pager.setAdapter(new OrderlPageAdapter(getSupportFragmentManager()));
		tabs.setViewPager(pager);
		// 默认滚动到的下标
		pager.setCurrentItem(getIntent().getIntExtra("id", 0));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}

	/**
	 * 此方法是在状态改变的时候调用。 其中arg0这个参数有三种状态（0，1，2） arg0
	 * ==1的时表示正在滑动，arg0==2的时表示滑动完毕了，arg0==0的时表示什么都没做
	 * 当页面开始滑动的时候，三种状态的变化顺序为1-->2-->0
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	/**
	 * 页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直被调用。 其中三个参数的含义分别为： arg0 :当前页面，及你点击滑动的页面
	 * arg1:当前页面偏移的百分比 arg2:当前页面偏移的像素位置
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	/**
	 * 此方法是页面跳转完后被调用，arg0是你当前选中的页面的Position（位置编号）
	 */
	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == 1) {
			// mOnArticleSelectedListener.onArticleSelected();
		}
	}
}
