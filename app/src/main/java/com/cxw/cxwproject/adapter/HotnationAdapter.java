package com.cxw.cxwproject.adapter;

import java.util.List;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.HomeBean.HotnationBean;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Context;

public class HotnationAdapter extends QuickAdapter<HotnationBean> {

	public HotnationAdapter(Context context, int layoutResId, List<HotnationBean> mData) {
		super(context, layoutResId, mData);
	}

	@Override
	protected void convert(BaseAdapterHelper helper, HotnationBean item) {
		int display = MyApp.getApp().getDisplayHightAndWightPx()[1];
		int height = display / 3;
		helper.setImageViewLinearLayout(R.id.selected_img, height, display);
		helper.setImageUrl(R.id.selected_img, item.getImg_url());
	}

}
