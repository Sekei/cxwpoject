package com.cxw.cxwproject.adapter;

import java.util.List;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.HomeBean.NationalsBean;
import com.cxw.cxwproject.bean.HomesBean.HomeContent.Content.HomeData;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Context;

public class NationalsAdapter extends QuickAdapter<HomeData> {

	public NationalsAdapter(Context context, int layoutResId, List<HomeData> mData) {
		super(context, layoutResId, mData);
	}

	@Override
	protected void convert(BaseAdapterHelper helper, HomeData item) {
		int display = MyApp.getApp().getDisplayHightAndWightPx()[1];
		int width = display / 3;
		helper.setImageViewRelativeLayout(R.id.img, width, width);
		helper.setImageUrl(R.id.img, item.getImg_url());
	}

}
