package com.cxw.cxwproject.adapter;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.LoginActivity;
import com.cxw.cxwproject.activity.NationalPageActivity;
import com.cxw.cxwproject.activity.ShopSimpleActivity;
import com.cxw.cxwproject.bean.NationalBean;
import com.cxw.cxwproject.dialog.MoreDialog;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;
import com.cxw.cxwproject.tool.SharePreferenceUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NationalPageTopAdapter extends PagerAdapter implements OnClickListener {
	private NationalBean mNaModel;
	private int mChildCount = 0;
	private Context mContext;

	public NationalPageTopAdapter(Context mContext, NationalBean NaModel) {
		this.mContext = mContext;
		this.mNaModel = NaModel;
	}

	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.nation_view_header, container, false);
		// 店铺简介
		LinearLayout shopsimple = (LinearLayout) view.findViewById(R.id.shopsimple);
		shopsimple.setOnClickListener(this);
		
		container.addView(view);
		return view;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void onClick(View v) {
		Intent miIntent = new Intent(mContext, ShopSimpleActivity.class);
		miIntent.putExtra("introduction", mNaModel.getIntroduction());
		mContext.startActivity(miIntent);
	}
}
