package com.cxw.cxwproject.adapter;

import java.util.List;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.HomesBean.HomeContent.Content.HomeData;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Context;

public class PushGridView extends QuickAdapter<HomeData> {

	public PushGridView(Context context, int layoutResId, List<HomeData> mData) {
		super(context, layoutResId, mData);
	}

	@Override
	protected void convert(BaseAdapterHelper helper, HomeData item) {
		int display = MyApp.getApp().getDisplayHightAndWightPx()[1];
		int width = display / 2;
		int height = (width * 6) / 10;
		helper.setImageViewRelativeLayout(R.id.img, height, width);
		helper.setImageUrl(R.id.img, item.getImg_url());
	}

}
