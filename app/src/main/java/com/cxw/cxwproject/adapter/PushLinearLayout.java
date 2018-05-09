package com.cxw.cxwproject.adapter;

import java.util.List;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.HomeBean.NationalsBean;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.Context;

public class PushLinearLayout extends QuickAdapter<String> {

	public PushLinearLayout(Context context, int layoutResId, List<String> mData) {
		super(context, layoutResId, mData);
	}

	@Override
	protected void convert(BaseAdapterHelper helper, String item) {
		helper.setText(R.id.name, "你猜");
	}

}
