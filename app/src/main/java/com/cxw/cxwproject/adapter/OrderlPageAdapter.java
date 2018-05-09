package com.cxw.cxwproject.adapter;

import com.cxw.cxwproject.fragment.Order_Page1;
import com.cxw.cxwproject.fragment.Order_Page2;
import com.cxw.cxwproject.fragment.Order_Page3;
import com.cxw.cxwproject.fragment.Order_Page4;
import com.cxw.cxwproject.fragment.Order_Page5;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

public class OrderlPageAdapter extends FragmentStatePagerAdapter {
	private String[] titles = { "全部", "待付款", "待发货", "待收货", "待评价" };
	private Order_Page1 mpager1;
	private Order_Page2 mpager2;
	private Order_Page3 mpager3;
	private Order_Page4 mpager4;
	private Order_Page5 mpager5;

	public OrderlPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			if (mpager1 == null) {
				mpager1 = new Order_Page1();
			}
			return mpager1;
		case 1:
			if (mpager2 == null) {
				mpager2 = new Order_Page2();
			}
			return mpager2;
		case 2:
			if (mpager3 == null) {
				mpager3 = new Order_Page3();
			}
			return mpager3;
		case 3:
			if (mpager4 == null) {
				mpager4 = new Order_Page4();
			}
			return mpager4;
		case 4:
			if (mpager5 == null) {
				mpager5 = new Order_Page5();
			}
			return mpager5;
		default:
			return null;
		}
	}
}
