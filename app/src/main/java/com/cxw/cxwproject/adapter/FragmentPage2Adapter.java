package com.cxw.cxwproject.adapter;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.DetailsActivity;
import com.cxw.cxwproject.bean.CommodityBean;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickGridViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class FragmentPage2Adapter extends QuickGridViewAdapter<CommodityBean> {

	public FragmentPage2Adapter(Context context, int layoutResId) {
		super(context, layoutResId);
	}

	@Override
	protected void convert(BaseAdapterHelper helper, final CommodityBean item) {
		helper.setImageUrl(R.id.img, item.getImage());
		int display = MyApp.getApp().getDisplayHightAndWightPx()[1];
		int width = display / 2;
		helper.setImageViewLinearLayout(R.id.img, width, width);
		helper.setText(R.id.name, item.getName());
		helper.setText(R.id.price, item.getPrice());
		
		helper.setOnClickListener(R.id.item_ll, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(context, DetailsActivity.class);
				mIntent.putExtra("data_0", item.getProduct_id());
				context.startActivity(mIntent);
			}
		});
	}

}
