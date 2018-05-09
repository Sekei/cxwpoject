package com.cxw.cxwproject.adapter;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.imagedisplay.ImageDisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Recommendadapter extends BaseAdapter {
	private Context context;
	private LayoutInflater listContainer;

	public class ListItemView {
		private ImageView img;
	}

	public Recommendadapter(Context mContext) {
		this.context = mContext;
		listContainer = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return 5;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.recommend_item, parent, false);
			listItemView.img = (ImageView) convertView.findViewById(R.id.img);
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) listItemView.img
					.getLayoutParams();
			int w = MyApp.getApp().getDisplayHightAndWightPx()[1];// 宽度
			params.height = w / 3;
			params.width = w;
			listItemView.img.setLayoutParams(params);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		if (position==0) {
			ImageDisplay.getSingleton().ImageLoader(listItemView.img, "http://shop.v89.com/Public/themes/images/mainbanner/2015120910555933127.jpg", false);
		}else if(position==1){
			ImageDisplay.getSingleton().ImageLoader(listItemView.img, "http://shop.v89.com/Public/themes/images/mainbanner/2015120910545885782.jpg", false);
		}else if(position==2){
			ImageDisplay.getSingleton().ImageLoader(listItemView.img, "http://shop.v89.com/Public/themes/images/mainbanner/2015120910531378206.jpg", false);
		}else if(position==3){
			ImageDisplay.getSingleton().ImageLoader(listItemView.img, "http://img.v89.com/group1/M05/04/37/rBAA11O2NPyADiagAABzWrAaHOQ701_640*360_213x120.jpg", false);
		}else{
			ImageDisplay.getSingleton().ImageLoader(listItemView.img, "http://img.v89.com/group1/M06/07/2C/rBAA11V4-ECAEoNaAACmLusumYw528_640*360_320x180.jpg", false);
		}
		return convertView;
	}
}
