package com.cxw.cxwproject.adapter;

import com.cxw.cxwproject.fragment.National_Page1;
import com.cxw.cxwproject.fragment.National_Page2;
import com.cxw.cxwproject.fragment.National_Page3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class NationalPageAdapter extends FragmentStatePagerAdapter {
	private String[] titles = { "特产", "风情", "资讯" };
	private National_Page1 mpager1;
	private National_Page2 mpager2;
	private National_Page3 mpager3;
	private String nationId, culture_url;// 民族id

	public NationalPageAdapter(FragmentManager fm, String nationid, String culture_url) {
		super(fm);
		this.nationId = nationid;
		this.culture_url = culture_url;
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
				mpager1 = new National_Page1();
			}
			Bundle bundle1 = new Bundle();
			bundle1.putString("id", nationId);
			mpager1.setArguments(bundle1);
			return mpager1;
		case 1:
			if (mpager3 == null) {
				mpager3 = new National_Page3();
			}
			Bundle bundle3 = new Bundle();
			bundle3.putString("culture_url", culture_url);
			mpager3.setArguments(bundle3);
			return mpager3;
		case 2:
			if (mpager2 == null) {
				mpager2 = new National_Page2();
			}
			Bundle bundle2 = new Bundle();
			bundle2.putString("id", nationId);
			mpager2.setArguments(bundle2);
			return mpager2;
		default:
			return null;
		}
	}
}
